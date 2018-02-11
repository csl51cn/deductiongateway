package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.DeductionTemplate;
import org.starlightfinancial.deductiongateway.domain.local.DeductionTemplateRepository;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.service.DeductionTemplateService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by senlin.deng on 2018-01-11.
 */
@Service
public class DeductionTemplateServiceImpl implements DeductionTemplateService {

    @Autowired
    private DeductionTemplateRepository deductionTemplateRepository;

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;

    /**
     * 根据条件查询代扣模板
     *
     * @param pageBean
     * @param isSuccess
     * @param contractNo
     * @param customerName
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public PageBean queryDeductionTemplate(PageBean pageBean, String isSuccess, String contractNo, String customerName, Date startDate, Date endDate) {

        //从前端传入的条件设置specification
        Specification<DeductionTemplate> specification = getSpecification(isSuccess, contractNo, customerName, startDate, endDate);
        long count = deductionTemplateRepository.count(specification);
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }

        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);
        Page<DeductionTemplate> deductionTemplatePage = deductionTemplateRepository.findAll(specification, pageRequest);
        if (deductionTemplatePage.hasContent()) {
            /**将代扣未扣完的代扣模板记录筛选出来,使用合同号,代扣起止日期查询已执行了的代扣记录,并计算当前已扣本息和服务费,
             然后比较计划扣款金额,更新代扣模板状态,剩余本息和服务费
             */
            ArrayList<DeductionTemplate> failContractNos = new ArrayList<>();
            for (DeductionTemplate deductionTemplate : deductionTemplatePage.getContent()) {
                if (StringUtils.equals("0", deductionTemplate.getIsSuccess())) {
                    failContractNos.add(deductionTemplate);
                }
            }
            //计算,更新操作
            updateDeductionStatus(failContractNos, startDate, endDate);
        }

        deductionTemplatePage = deductionTemplateRepository.findAll(specification, pageRequest);
        if (deductionTemplatePage.hasContent()) {
            pageBean.setTotal(deductionTemplatePage.getTotalElements());
            pageBean.setRows(deductionTemplatePage.getContent());
            return pageBean;
        }

        return null;
    }

    /**
     * 用代扣金额未扣完的记录(当日扣款总金额<应还总金额)的合同号,代扣起止日期,查询扣款记录,更新状态及当期剩余本息,当期剩余服务费
     *
     * @param failRecords  代扣金额未扣完的记录
     * @param startDate
     * @param endDate
     */
    public void updateDeductionStatus(List<DeductionTemplate> failRecords,Date startDate, Date endDate) {
        ArrayList<String> contractNos = new ArrayList<>();
        for (DeductionTemplate deductionTemplate: failRecords){
            contractNos.add(deductionTemplate.getContractNo());
        }

        //使用代扣金额未扣完的记录的合同号,代扣起止日期,查询已执行了的代扣记录
        Specification<MortgageDeduction> deductedSpecification = new Specification<MortgageDeduction>() {
            @Override
            public Predicate toPredicate(Root<MortgageDeduction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (contractNos.size() > 0) {
                    predicates.add(cb.in(root.get("contractNo")).value(contractNos));
                }
                predicates.add(cb.greaterThanOrEqualTo(root.get("payTime"), startDate));
                predicates.add(cb.lessThanOrEqualTo(root.get("payTime"), endDate));
                predicates.add(cb.equal(root.get("issuccess"), "1"));
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        List<MortgageDeduction> deductedRecords = mortgageDeductionRepository.findAll(deductedSpecification);
        //保存已扣本息的map
        HashMap<String, BigDecimal> bxMap = new HashMap<String, BigDecimal>();
        //保存已扣服务费的map
        HashMap<String, BigDecimal> fwfMap = new HashMap<String, BigDecimal>();
        for (MortgageDeduction mortgageDeduction : deductedRecords) {
            bxMap.merge(mortgageDeduction.getContractNo(), mortgageDeduction.getSplitData1(), (oldValue, newValue) -> oldValue.add(newValue));
            fwfMap.merge(mortgageDeduction.getContractNo(), mortgageDeduction.getSplitData2(), (oldValue, newValue) -> oldValue.add(newValue));
        }

        //已扣本息总额
        BigDecimal deductedBx = new BigDecimal(0);
        //已扣服务费总额
        BigDecimal deductedFwf = new BigDecimal(0);
        //剩余本息总额
        BigDecimal remainBx = new BigDecimal(0);
        //剩余服务费总额
        BigDecimal remainFwf = new BigDecimal(0);

        //待更新的代扣模板记录
        ArrayList<DeductionTemplate> toBeUpdateDeductionTemplate = new ArrayList<>();

        for (DeductionTemplate deductionTemplate : failRecords) {
            //如果没有扣款成功的记录,跳过循环
            if(!bxMap.containsKey(deductionTemplate.getContractNo())){
                continue;
            }
            deductedBx = bxMap.get(deductionTemplate.getContractNo());
            deductedFwf = fwfMap.get(deductionTemplate.getContractNo());
            remainBx = deductionTemplate.getBxAmount().subtract(deductedBx);
            remainFwf = deductionTemplate.getFwfAmount().subtract(deductedFwf);
            //应还本息+应还服务费 <= 已扣本息+已扣服务费,更新代扣状态为成功,未还本息/服务费为0
            if (deductionTemplate.getBxAmount().add(deductedFwf).doubleValue() <= deductedBx.add(deductedFwf).doubleValue()) {
                deductionTemplate.setIsSuccess("1");
                deductionTemplate.setBxRemain(new BigDecimal(0));
                deductionTemplate.setFwfRemain(new BigDecimal(0));
            } else {
                //应还本息+应还服务费 > 已扣本息+已扣服务费,更新未还本息/服务费

                //数据库中剩余的本息/服务费与新查询出来的剩余本息/服务费比较,相同时不需要做更新操作
                if(deductionTemplate.getBxRemain().compareTo(remainBx) != 0 && deductionTemplate.getFwfRemain().compareTo(remainFwf) != 0 ){
                    deductionTemplate.setBxRemain(deductionTemplate.getBxAmount().subtract(deductedBx));
                    deductionTemplate.setFwfRemain(deductionTemplate.getFwfAmount().subtract(deductedFwf));
                    toBeUpdateDeductionTemplate.add(deductionTemplate);
                }
            }
            deductionTemplateRepository.save(toBeUpdateDeductionTemplate);
        }
    }

    @Override
    public Workbook exportXLS(String isSuccess, String contractNo, String customerName, Date startDate, Date endDate) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //表头
        String[] headers = new String[]{"业务编号", "银行名称", "卡折标识", "银行卡号", "姓名", "证件类型", "证件号码", "分账户数据1",
                "分账户数据2", "服务费管理司", "业务号"};
        sheet.setDefaultColumnWidth(16);
        HSSFRow rowHead1 = sheet.createRow(0);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = rowHead1.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellStyle);
        }

        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFCellStyle numericStyle = workbook.createCellStyle();
        numericStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        int i = 1;
        Specification<DeductionTemplate> specification = getSpecification(isSuccess, contractNo, customerName, startDate, endDate);
        List<DeductionTemplate> deductionTemplates = deductionTemplateRepository.findAll(specification);
        for (DeductionTemplate deductionTemplate : deductionTemplates) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(deductionTemplate.getContractNo());
            cell = row.createCell(1);
            cell.setCellValue(deductionTemplate.getBankName());
            cell = row.createCell(2);
            cell.setCellValue(deductionTemplate.getCardAndPassbook());
            cell = row.createCell(3);
            cell.setCellValue(deductionTemplate.getAccount());
            cell = row.createCell(4);
            cell.setCellValue(deductionTemplate.getCustomerName());
            cell = row.createCell(5);
            cell.setCellValue(deductionTemplate.getCertificateType());
            cell = row.createCell(6);
            cell.setCellValue(deductionTemplate.getCertificateNo().toUpperCase());
            cell = row.createCell(7);
            cell.setCellValue(deductionTemplate.getBxRemain().doubleValue());
            cell = row.createCell(8);
            cell.setCellValue(deductionTemplate.getFwfRemain().doubleValue());
            cell = row.createCell(9);
            cell.setCellValue(deductionTemplate.getFwfCompamny());
            cell = row.createCell(10);
            cell.setCellValue(deductionTemplate.getBizNo());
            i = i + 1;
        }
        return workbook;

    }


    private Specification<DeductionTemplate> getSpecification(String isSuccess, String contractNo, String customerName, Date startDate, Date endDate) {
        Specification<DeductionTemplate> specification = new Specification<DeductionTemplate>() {
            @Override
            public Predicate toPredicate(Root<DeductionTemplate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                String[] isSuccessArr = isSuccess.split(",");
                predicates.add(cb.in(root.get("isSuccess")).value(Arrays.asList(isSuccessArr)));

                if (StringUtils.isNotBlank(customerName)) {
                    predicates.add(cb.equal(root.get("customerName"), customerName));
                }

                if (StringUtils.isNotBlank(contractNo)) {
                    predicates.add(cb.equal(root.get("contractNo"), contractNo));
                }


                predicates.add(cb.greaterThanOrEqualTo(root.get("planDate"), startDate));
                predicates.add(cb.lessThanOrEqualTo(root.get("planDate"), endDate));

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        return specification;
    }
}