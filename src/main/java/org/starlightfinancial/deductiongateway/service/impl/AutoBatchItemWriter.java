package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.List;

/**
 * Created by sili.chen on 2017/9/11
 */
public class AutoBatchItemWriter implements ItemWriter {

    private JpaItemWriter jpaItemWriter;

    @Override
    public void write(List list) throws Exception {
        for (Object object : list) {
            jpaItemWriter.write((List) object);
        }
    }

    public void setJpaItemWriter(JpaItemWriter jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    public void setLocalContainerEntityManagerFactoryBean(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        jpaItemWriter.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
    }
}
