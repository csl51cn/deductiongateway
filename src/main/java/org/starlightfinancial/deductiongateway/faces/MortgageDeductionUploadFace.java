package org.starlightfinancial.deductiongateway.faces;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.service.ContractService;

import javax.annotation.PostConstruct;

@Service("mortgageDeductionUploadFace")
@Scope("view")
public class MortgageDeductionUploadFace extends BaseBean {

    Logger logger = LoggerFactory.getLogger(MortgageDeductionUploadFace.class);

    @Autowired
    private ContractService contractService;

    private UploadedFile uploadedFile;

    @PostConstruct
    public void init() {

    }

    public void importBlackCustomer() {
        try {
            if (uploadedFile == null
                    || (!StringUtils
                    .endsWith(uploadedFile.getFileName(), ".xls") && !StringUtils
                    .endsWith(uploadedFile.getFileName(), ".xlsx"))) {
                showMessage(0, "导入失败!");
                return;
            }

            if (uploadedFile != null && StringUtils.endsWith(uploadedFile.getFileName(), ".xls")) {
                contractService.deleteMortgageDeduction();
                systemService.importCusInsideBlack(uploadedFile.getInputstream(),
                        String.valueOf(sessionBean.getStaffId()), sessionBean.getOrgId());
                showMessage(0, "导入完毕!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(0, "导入失败!");
        }
    }
}
