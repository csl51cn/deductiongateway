package org.starlightfinancial.deductiongateway.faces;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.model.CreApMainServiceData;
import org.starlightfinancial.deductiongateway.model.GoPayBean;
import org.starlightfinancial.deductiongateway.model.SysAutoNum;
import org.starlightfinancial.deductiongateway.service.ContractService;
import org.starlightfinancial.deductiongateway.service.CreApMainServiceDataService;
import org.starlightfinancial.deductiongateway.service.SystemService;
import org.starlightfinancial.deductiongateway.utility.HashType;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.MerSeq;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 批量扣款
 *
 * @author sili.chen
 */
@Service("mortgageDeductionFaces")
public class MortgageDeductionFaces extends BaseBean {

    @Autowired
    private ContractService contractService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private CreApMainServiceDataService creApMainServiceDataService;

    private HttpClientUtil httpClientUtil;

    private List<HashType> listCustomer;

    /**
     * 批量扣款的方法
     *
     * @return
     */
    public String saveMortgageDeductions() {
        listCustomer = new ArrayList<HashType>();
        HashType hashType = new HashType();
        hashType.setKey("280610");
        hashType.setValue11("123");
        hashType.setValue2("123");
        hashType.setValue("123");
        hashType.setValue10("123");
        hashType.setValue12("");
        hashType.setValue7("0.1");
        hashType.setValue8("0");

        listCustomer.add(hashType);

//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("WEB-INF\\classes");
        String path = "D:\\ideaProjects\\deductiongateway\\src\\main\\resources";
        List<HashType> list = new ArrayList<HashType>();
        list.add(listCustomer.get(0));
        int[] ids = getIntParameters("deleteIds");
        String reIds = "";
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < listCustomer.size(); i++) {
                String id = listCustomer.get(i).getValue12();
                for (int j = 0; j < ids.length; j++) {
                    if (Integer.parseInt(id) == ids[j]) {
                        list.add(listCustomer.get(i));
                        break;
                    }
                }
            }
        }
        List<GoPayBean> messages = new ArrayList<GoPayBean>();
        String splitData;
        boolean flag;
        HashType loanRePlan;
        for (int i = 0; i < list.size(); i++) {
            String contactNo = "";
            loanRePlan = list.get(i);
            GoPayBean goPayBean = new GoPayBean();
            goPayBean.setContractId(loanRePlan.getValue11());//设置合同编号
            //计算账户管理费
            goPayBean.setCustomerNo(loanRePlan.getValue2());//设置客户编号
            goPayBean.setCustomerName(loanRePlan.getValue());//设置客户名称
            goPayBean.setContractNo(loanRePlan.getValue10());//设置合同编号
            goPayBean.setOrgManagerId(contactNo);//设置服务费的管理公司
            goPayBean.setRePlanId(loanRePlan.getValue12());//设置还款计划的id
            String amount1 = loanRePlan.getValue7();
            String amount2 = loanRePlan.getValue8();
            int m1 = 0;
            int m2 = 0;
            if (amount1 != null && !"".equals(amount1)) {
                m1 = new BigDecimal(amount1).movePointRight(2).intValue();
            }
            if (amount2 != null && !"".equals(amount2)) {
                m2 = new BigDecimal(amount2).movePointRight(2).intValue();
            }
            goPayBean.setOrdAmt(m1 + m2 + "");
            //  splitData="00145111^"+m1+";00145112^"+m2+";";
            int orgId = -1;
//            CreApMainServiceData creApMainServiceData = creApMainServiceDataService.getCreApMainServiceData(Integer.parseInt(loanRePlan.getKey()));
            CreApMainServiceData creApMainServiceData = null;
            if (creApMainServiceData != null) {
                orgId = creApMainServiceData.getOrgManagerId();
            }
            //取商户号
//            SysAutoNum sysAutoNum = systemService.loadSysAutoNum(orgId, "5");
            SysAutoNum sysAutoNum = new SysAutoNum();

            if (sysAutoNum != null) {
                contactNo = sysAutoNum.getNowNo();
                contactNo = "00160808";
            }
            //商户分账数据
            splitData = "00145111^" + m1;
            if (contactNo != null) {
                splitData = splitData + ";" + contactNo + "^" + m2 + ";";
            } else {
                contactNo = "00145112";
                splitData = splitData + ";" + contactNo + "^" + m2 + ";";
            }

            goPayBean.setSplitData1(new BigDecimal(amount1));
            if (!"".equals(amount2))
                goPayBean.setSplitData2(new BigDecimal(amount2));
            goPayBean.setBusiId("");
            goPayBean.setOrdId(MerSeq.tickOrder());
            goPayBean.setOrgManagerId(contactNo);//设置服务费的管理公司
            goPayBean.setMerId(Utility.SEND_BANK_MERID);//商户号
            goPayBean.setCuryId(Utility.SEND_BANK_CURYID);//订单交易币种
            goPayBean.setVersion(Utility.SEND_BANK_VERSION);//版本号
            goPayBean.setBgRetUrl(Utility.SEND_BANK_BGRETURL);//后台交易接收URL地址
            goPayBean.setPageRetUrl(Utility.SEND_BANK_PAGERETURL);//页面交易接收URL地址
            goPayBean.setGateId(Utility.SEND_BANK_GATEID);//支付网关号
//            goPayBean.setParam1(loanRePlan.getParam1());//开户行号
//            goPayBean.setParam2(loanRePlan.getParam2());//卡折标志
//            goPayBean.setParam3(loanRePlan.getParam3());//卡号/折号
//            goPayBean.setParam4(loanRePlan.getParam4());//持卡人姓名
//            goPayBean.setParam5(loanRePlan.getParam5());//证件类型
//            goPayBean.setParam6(loanRePlan.getParam6()); //证件号
            goPayBean.setParam1("0410");//开户行号
            goPayBean.setParam2("0");//卡折标志
            goPayBean.setParam3("6216261000000000018");//卡号/折号
            goPayBean.setParam4("全渠道");//持卡人姓名
            goPayBean.setParam5("01");//证件类型
            goPayBean.setParam6("341126197709218366"); //证件号
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
        }
        if (httpClientUtil == null) {
            httpClientUtil = new HttpClientUtil();
        }

        try {
            List<Map> result = httpClientUtil.sendInformation(messages, path, contractService, 1);
        } catch (Exception e) {
            e.printStackTrace();
            super.showMessage(0, "代扣款失败！");
        }
        return "";
    }
}
