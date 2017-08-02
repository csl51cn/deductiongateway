package org.starlightfinancial.deductiongateway.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.*;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 批量扣款
 *
 * @author sili.chen
 */
@Service
public class MortgageDeductionServiceImpl implements MortgageDeductionService {

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    SysDictRepository sysDictRepository;

    private static final Logger log = LoggerFactory.getLogger(MortgageDeductionService.class);

    public void importCustomerData(MultipartFile uploadedFile, int staffId) {
        try {
            if (null == uploadedFile) {
                log.info("");
                return;
            }
            if (!StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xls") && !StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xlsx")) {
                log.info("");
                return;
            }

            if (StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xls")) {
                this.readXls(uploadedFile, staffId);
            } else if (StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xlsx")) {
                this.readXlsx(uploadedFile, staffId);
            }
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
    }

    private void readXls(MultipartFile uploadedFile, int staffId) throws IllegalAccessException, NoSuchFieldException, IOException {
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(uploadedFile.getInputStream());
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

            Map<String, String> colNameMap = new HashMap<>();
            colNameMap.put("业务编号", "contractNo");
            colNameMap.put("银行名称", "param1");
            colNameMap.put("卡折标识", "param2");
            colNameMap.put("银行卡号", "param3");
            colNameMap.put("姓名", "param4");
            colNameMap.put("证件类型", "param5");
            colNameMap.put("证件号码", "param6");
            colNameMap.put("分账户数据1", "splitData1");
            colNameMap.put("分账户数据2", "splitData2");
            colNameMap.put("服务费管理司", "target");

            Map<Integer, String> colIndexMap = new HashMap<>();
            if (null != hssfSheet) {
                HSSFRow firstRow = hssfSheet.getRow(hssfSheet.getFirstRowNum());
                for (int i = firstRow.getFirstCellNum(); i < firstRow.getLastCellNum(); i++) {
                    HSSFCell hssfCell = firstRow.getCell(i);
                    if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING && colNameMap.containsKey(StringUtils.trimToEmpty(hssfCell.getStringCellValue()))) {
                        String columnName = colNameMap.get(StringUtils.trimToEmpty(hssfCell.getStringCellValue()));
                        if (StringUtils.isNotBlank(columnName)) {
                            colIndexMap.put(i, columnName);
                        }
                    }
                }

                for (int i = hssfSheet.getFirstRowNum() + 1; i <= hssfSheet.getPhysicalNumberOfRows(); i++) {
                    HSSFRow hssfRow = hssfSheet.getRow(i);
                    if (null != hssfRow) {
                        MortgageDeduction mortgageDeduction = new MortgageDeduction();
                        for (int j = hssfRow.getFirstCellNum(); j < hssfRow.getLastCellNum(); j++) {
                            HSSFCell hssfCell = hssfRow.getCell(j);
                            if (colIndexMap.containsKey(j)) {
                                String fieldName = colIndexMap.get(j);
                                Field field = MortgageDeduction.class.getDeclaredField(fieldName);
                                field.setAccessible(true);

                                if (field.getType() == String.class) {
                                    String value = "";
                                    if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                        value = StringUtils.trimToEmpty(hssfCell.getStringCellValue());
                                    } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                        value = hssfCell.getNumericCellValue() + "";
                                        if ("0.0".equals(value)) {
                                            value = "0";
                                        }
                                    }
                                    field.set(mortgageDeduction, value);
                                }
                                if (field.getType() == BigDecimal.class) {
                                    BigDecimal value = BigDecimal.valueOf(hssfCell.getNumericCellValue());
                                    field.set(mortgageDeduction, value);
                                }
                            }
                        }

                        mortgageDeduction.setType("1");
                        mortgageDeduction.setPlanNo(-1);
                        mortgageDeduction.setCreateDate(new Date());
                        mortgageDeduction.setSplitType("0001");
                        mortgageDeduction.setCreatId(staffId);
                        mortgageDeduction.setCustomerName(mortgageDeduction.getParam4());

                        if (StringUtils.isNotBlank(mortgageDeduction.getParam2()) && "卡".equals(mortgageDeduction.getParam2().trim())) {
                            mortgageDeduction.setParam2("0");
                        } else {
                            mortgageDeduction.setParam2("1");
                        }

                        //处理开户行
                        List<SysDict> sysDicts = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
                        for (int k = 0; k < sysDicts.size(); k++) {
                            if (mortgageDeduction.getParam1().equals(sysDicts.get(k).getDicValue())) {
                                mortgageDeduction.setParam1(sysDicts.get(k).getDicKey());
                            }
                        }

                        //处理证件类型
                        sysDicts = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
                        for (int k = 0; k < sysDicts.size(); k++) {
                            if (mortgageDeduction.getParam5().equals(sysDicts.get(k).getDicValue())) {
                                mortgageDeduction.setParam5(sysDicts.get(k).getDicKey());
                            }
                        }

                        //处理服务费管理公司
                        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && "铠岳".equals(mortgageDeduction.getTarget().trim())) {
                            mortgageDeduction.setTarget("00145112");
                        } else {
                            mortgageDeduction.setTarget("00160808");
                        }

                        if (mortgageDeduction.getParam3() != null && !"".equals(mortgageDeduction.getParam3())) {

                            mortgageDeductionRepository.save(mortgageDeduction);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void readXlsx(MultipartFile uploadedFile, int staffId) {

    }

    public List<Map> saveMortgageDeductions(List<MortgageDeduction> list) {

//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("WEB-INF\\classes");
        String path = "D:\\sili.chen\\IdeaProjects\\deductiongateway\\src\\main\\resources";
        List<GoPayBean> messages = new ArrayList<GoPayBean>();
        String splitData;
        for (int i = 0; i < list.size(); i++) {
            GoPayBean goPayBean = new GoPayBean();
            MortgageDeduction mortgageDeduction = list.get(i);
            goPayBean.setContractId(String.valueOf(mortgageDeduction.getApplyMainId()));//设置合同编号
            goPayBean.setCustomerNo(mortgageDeduction.getCustomerNo());//设置客户编号
            goPayBean.setCustomerName(mortgageDeduction.getCustomerName());//设置客户名称
            goPayBean.setContractNo(mortgageDeduction.getContractNo());//设置合同编号
            goPayBean.setOrgManagerId(mortgageDeduction.getTarget());//设置服务费的管理公司
            goPayBean.setRePlanId("");//设置还款计划的id

            //计算账户管理费
            int m1 = 0;
            int m2 = 0;
            String amount1 = mortgageDeduction.getSplitData1().toString();
            String amount2 = mortgageDeduction.getSplitData2().toString();
            if (StringUtils.isNotBlank(amount1)) {
                m1 = new BigDecimal(amount1).movePointRight(2).intValue();
            }
            if (StringUtils.isNotBlank(amount2)) {
                m2 = new BigDecimal(amount2).movePointRight(2).intValue();
            }
            goPayBean.setOrdAmt(m1 + m2 + "");
            // 商户分账数据
            splitData = "00145111^" + m1;
            if (StringUtils.isNotBlank(goPayBean.getOrgManagerId())) {
                splitData += ";" + goPayBean.getOrgManagerId() + "^" + m2 + ";";
            }
//            splitData = "00010001^1;00010002^1";

            goPayBean.setSplitData1(new BigDecimal(amount1));
            goPayBean.setSplitData2(new BigDecimal(amount2));
            goPayBean.setBusiId("");
            goPayBean.setOrdId(MerSeq.tickOrder());
            goPayBean.setMerId(Utility.SEND_BANK_MERID);//商户号
            goPayBean.setCuryId(Utility.SEND_BANK_CURYID);//订单交易币种
            goPayBean.setVersion(Utility.SEND_BANK_VERSION);//版本号
            goPayBean.setBgRetUrl(Utility.SEND_BANK_BGRETURL);//后台交易接收URL地址
            goPayBean.setPageRetUrl(Utility.SEND_BANK_PAGERETURL);//页面交易接收URL地址
            goPayBean.setGateId(Utility.SEND_BANK_GATEID);//支付网关号
//            goPayBean.setParam1("0410");//开户行号
//            goPayBean.setParam2("0");//卡折标志
//            goPayBean.setParam3("6216261000000000018");//卡号/折号
//            goPayBean.setParam4("全渠道");//持卡人姓名
//            goPayBean.setParam5("01");//证件类型
//            goPayBean.setParam6("341126197709218366"); //证件号
            goPayBean.setParam1(mortgageDeduction.getParam1());//开户行号
            goPayBean.setParam2(mortgageDeduction.getParam2());//卡折标志
            goPayBean.setParam3(mortgageDeduction.getParam3());//卡号/折号
            goPayBean.setParam4(mortgageDeduction.getParam4());//持卡人姓名
            goPayBean.setParam5(mortgageDeduction.getParam5());//证件类型
            goPayBean.setParam6(mortgageDeduction.getParam6()); //证件号
            goPayBean.setParam7("");
            goPayBean.setParam8("");
            goPayBean.setParam9("");
            goPayBean.setParam10("");
            goPayBean.setOrdDesc("批量代扣款");
            goPayBean.setShareType(Utility.SEND_BANK_TYPE);//分账类型
            goPayBean.setShareData(splitData);
            goPayBean.setPriv1("");
            goPayBean.setCustomIp("");
            messages.add(goPayBean);
            this.goPayBeanToMortgageDeduction(mortgageDeduction, goPayBean);
        }

        HttpClientUtil httpClientUtil = new HttpClientUtil();
        try {
            List<Map> result = httpClientUtil.sendInformation(messages, path, 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PageBean queryDeductionData(Date startDate, Date endDate, String customerName, PageBean pageBean, String type, int creatid) {
        PageRequest pageRequest = buildPageRequest(pageBean);
        Specification<MortgageDeduction> specification = new Specification<MortgageDeduction>() {
            @Override
            public Predicate toPredicate(Root<MortgageDeduction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotEmpty(customerName)) {
                    predicates.add(criteriaBuilder.equal(root.get("customerName"), customerName));
                }

                predicates.add(criteriaBuilder.in(root.get("type")).value(Arrays.asList(type.split(","))));
                predicates.add(criteriaBuilder.equal(root.get("creatId"), creatid));
                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));

            }
        };
        Page<MortgageDeduction> page = mortgageDeductionRepository.findAll(specification, pageRequest);

        long count = mortgageDeductionRepository.count(specification);

        if (page.hasContent()) {
            List<MortgageDeduction> mortgageDeductionList = page.getContent();
            List<SysDict> sysDicts = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
            for (MortgageDeduction mortgageDeduction : mortgageDeductionList) {

                //处理证件类型
                for (SysDict sysDict : sysDicts) {
                    if (mortgageDeduction.getParam5().equals(sysDict.getDicKey())) {
                        mortgageDeduction.setParam5(sysDict.getDicValue());
                    }
                }

                //处理服务费管理公司
                if (mortgageDeduction.getTarget() != null && "00145112".equals(mortgageDeduction.getTarget().trim())) {
                    mortgageDeduction.setTarget("铠岳");
                } else {
                    mortgageDeduction.setTarget("润坤");
                }
            }
            pageBean.setRows(mortgageDeductionList);
            pageBean.setTotal(count);
            return pageBean;
        }
        return null;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(PageBean pageBean) {
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


    private void goPayBeanToMortgageDeduction(MortgageDeduction mortgageDeduction, GoPayBean goPayBean) {
        mortgageDeduction.setOrdId(goPayBean.getOrdId());
        mortgageDeduction.setMerId(goPayBean.getMerId());
        mortgageDeduction.setCuryId(goPayBean.getCuryId());
        mortgageDeduction.setOrderDesc(goPayBean.getOrdDesc());
        mortgageDeduction.setPlanNo(0);
        mortgageDeduction.setType("0");
        mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
    }
}
