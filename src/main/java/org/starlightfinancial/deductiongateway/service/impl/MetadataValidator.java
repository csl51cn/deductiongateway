package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.service.Decorator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by sili.chen on 2017/8/23
 */
@Component
public class MetadataValidator extends Decorator implements Validator, InitializingBean {

    private Object o;

    @Autowired
    private javax.validation.Validator validator;


    public MetadataValidator() {
    }

    public MetadataValidator(Object o) {
        this.o = o;
    }


    @Override
    public void doRoute() {
        super.doRoute();
        try {
            this.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.validate(o);
        System.out.println("Validator is working");
    }

    @Override
    public void validate(Object o) throws ValidationException {
        AutoBatchDeduction autoBatchDeduction = (AutoBatchDeduction) o;
        Set<ConstraintViolation<AutoBatchDeduction>> constraintViolationSet = validator.validate(autoBatchDeduction);
        if (constraintViolationSet.size() > 0) {
            StringBuilder message = new StringBuilder(autoBatchDeduction.getDateId());
            for (ConstraintViolation<AutoBatchDeduction> constraintViolation : constraintViolationSet) {
                message.append(constraintViolation.getMessage() + "\n");
            }
            throw new ValidationException(message.toString());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        validator = validatorFactory.usingContext().getValidator();
    }
}
