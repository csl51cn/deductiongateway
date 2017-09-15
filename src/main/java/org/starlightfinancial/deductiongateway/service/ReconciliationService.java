package org.starlightfinancial.deductiongateway.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by sili.chen on 2017/9/13
 */
public interface ReconciliationService {
    public void unionPayDataImport(MultipartFile file) throws IOException;

    public void accountCheck(String[] result);
}
