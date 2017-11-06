package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ReconciliationServiceImpl.class);

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
        if (ordState == null) {
            log.info("对账时订单状态未获取到,订单号:" + ordId);
            return;
        }

        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findByOrdId(ordId);
        if (mortgageDeduction != null) {
            if (StringUtils.isBlank(mortgageDeduction.getErrorResult())) {//应对扣款失败无返回页面的情况
                mortgageDeduction.setResult(ordState);
                mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(ordState));
                if ("1001".equals(ordState)) {
                    mortgageDeduction.setIssuccess("1");
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeduction.setCheckState("1");
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } else {//应对正常返回扣款后页面的情况
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
}
