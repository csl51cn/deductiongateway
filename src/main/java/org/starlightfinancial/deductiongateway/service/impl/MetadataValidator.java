package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.service.Decorator;

/**
 * Created by sili.chen on 2017/8/23
 */
@Component
public class MetadataValidator extends Decorator implements Validator, InitializingBean {

    @Override
    public void doRoute() {
        super.doRoute();
        System.out.println("Validator is working");
    }

    @Override
    public void validate(Object o) throws ValidationException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
