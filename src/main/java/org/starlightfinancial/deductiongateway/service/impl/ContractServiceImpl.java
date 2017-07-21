package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.dao.MortgageDeductionDao;
import org.starlightfinancial.deductiongateway.model.MortgageDeduction;
import org.starlightfinancial.deductiongateway.service.ContractService;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ContractServiceImpl extends BaseServiceImpl implements
        ContractService {


    @Autowired
    private MortgageDeductionDao mortgageDeductionDao;


    public void deleteMortgageDeduction() {
        mortgageDeductionDao.executeUpdate("delete from CUS_MORTGAGEDEUCTION where type='1'");
    }

    @Override
    public MortgageDeduction addMortgageDeduction(int applyMainId,
                                                  String ordId, String customerNo, String customerName,
                                                  String contractNo, String param1, String param2, String param3,
                                                  String param4, String param5, String param6, String merId,
                                                  BigDecimal ordAmt, String curyId, String orderDesc,
                                                  String splitType, BigDecimal splitData, int creat_id, Date create_date,
                                                  String issuccess, String errorResult, String isoffs, String type, String target, BigDecimal rsData1, BigDecimal rsData2, String result) {
        MortgageDeduction mortgageDeduction = new MortgageDeduction();
        mortgageDeduction.setApplyMainId(applyMainId);
        mortgageDeduction.setOrdId(ordId);
        mortgageDeduction.setCustomerNo(customerNo);
        mortgageDeduction.setCustomerName(customerName);
        mortgageDeduction.setContractNo(contractNo);
        mortgageDeduction.setParam1(param1);
        mortgageDeduction.setParam2(param2);
        mortgageDeduction.setParam3(param3);
        mortgageDeduction.setParam4(param4);
        mortgageDeduction.setParam5(param5);
        mortgageDeduction.setParam6(param6);
        mortgageDeduction.setMerId(merId);
        mortgageDeduction.setSplitData1(ordAmt);
        mortgageDeduction.setCuryId(curyId);
        mortgageDeduction.setOrderDesc(orderDesc);
        mortgageDeduction.setSplitType(splitType);
        mortgageDeduction.setSplitData2(splitData);
        mortgageDeduction.setCreatId(creat_id);
        mortgageDeduction.setCreateDate(create_date);
        mortgageDeduction.setIssuccess(issuccess);
        mortgageDeduction.setErrorResult(errorResult);
        mortgageDeduction.setIsoffs(isoffs);
        mortgageDeduction.setType(type);
        mortgageDeduction.setTarget(target);
        mortgageDeduction.setRsplitData1(rsData1);
        mortgageDeduction.setRsplitData2(rsData2);
        mortgageDeduction.setResult(result);
        mortgageDeduction.setPlanNo(0);

        return mortgageDeductionDao.save(mortgageDeduction);
    }


}
