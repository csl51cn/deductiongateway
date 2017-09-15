package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.ErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.service.ReconciliationService;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.ExcelReader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;

/**
 * Created by sili.chen on 2017/9/13
 */
@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    @Qualifier("mortgageDeductionRepository")
    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Override
    @Transactional
    public void unionPayDataImport(MultipartFile file) throws IOException {
        try {
            ExcelReader excelReader = new ExcelReader();
            Map<Integer, String> map = excelReader.readExcelContent(file.getInputStream());
            for (int i = 3; i <= map.size(); i++) {
                String[] result = map.get(i).split("    ");
                this.accountCheck(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accountCheck(String[] result) {
        String ordId = result[3]; //订单号
        String ordAmt = result[5]; //订单金额
        String ordState = result[10].split("-")[0]; //订单状态
        ordState = ErrorCodeEnum.getCodeByNewCode(ordState);

        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findByOrdId(ordId);
        if (mortgageDeduction != null) {
            if (mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2()).doubleValue() == Double.valueOf(ordAmt)
                    && mortgageDeduction.getResult().equals(ordState)) {
                mortgageDeductionRepository.setCheckStateFor(Constant.CHECK_SUCESS, ordId);
            } else {
                if (StringUtils.equals(Constant.SUCCESS, ordState)) {
                    mortgageDeduction.setResult(Constant.SUCCESS);
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(ordState));
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                }
                mortgageDeductionRepository.setCheckStateFor(Constant.CHECK_FAILURE, ordId);
            }
        }
    }
}
