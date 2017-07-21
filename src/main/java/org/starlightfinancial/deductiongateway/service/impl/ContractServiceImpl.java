package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.dao.MortgageDeductionDao;
import org.starlightfinancial.deductiongateway.service.ContractService;

@Service
public class ContractServiceImpl extends BaseServiceImpl implements
        ContractService {


    @Autowired
    private MortgageDeductionDao mortgageDeductionDao;


    public void deleteMortgageDeduction() {
        mortgageDeductionDao.executeUpdate("delete from CUS_MORTGAGEDEUCTION where type='1'");
    }


}
