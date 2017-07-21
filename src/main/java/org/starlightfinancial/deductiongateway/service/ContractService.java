package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.model.MortgageDeduction;

import java.math.BigDecimal;
import java.util.Date;

public interface ContractService extends BaseService {


    public void deleteMortgageDeduction();

    public MortgageDeduction addMortgageDeduction(int applyMainId,
                                                  String OrdId, String customerNo, String customerName,
                                                  String contractNo, String Param1, String Param2, String Param3,
                                                  String Param4, String Param5, String Param6, String merId,
                                                  BigDecimal OrdAmt, String CuryId, String orderDesc,
                                                  String splitType, BigDecimal splitData, int creat_id, Date create_date,
                                                  String issuccess, String errorResult, String isoffs, String type, String target, BigDecimal rsData1, BigDecimal rsData2, String result);


}
