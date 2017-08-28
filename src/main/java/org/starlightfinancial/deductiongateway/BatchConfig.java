package org.starlightfinancial.deductiongateway;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.starlightfinancial.deductiongateway.common.MyJobListener;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeductionRowMapper;
import org.starlightfinancial.deductiongateway.service.impl.ConcreteHandler;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by sili.chen on 2017/8/23
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    @Bean
    public JobRepository jobRepository(@Qualifier("localDataSource") DataSource dataSource,
                                       @Qualifier("localTransactionManager") PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.SQLSERVER.toString());
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(@Qualifier("localDataSource") DataSource dataSource,
                                         @Qualifier("localTransactionManager") PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
        return jobLauncher;
    }

    @Bean
    public Job autoDeduction(JobBuilderFactory jobs, Step s1) {
        return jobs.get("autoDeduction")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .listener(myJobListener())
                .build();
    }

    @Bean(name = "s1")
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<AutoBatchDeduction> reader, ItemWriter<AutoBatchDeduction> writer
            , ItemProcessor<AutoBatchDeduction, AutoBatchDeduction> processor) {
        return stepBuilderFactory.get("step1")
                .<AutoBatchDeduction, AutoBatchDeduction>chunk(65000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<AutoBatchDeduction> reader(@Qualifier("remoteDataSource") DataSource dataSource) {
        StoredProcedureItemReader storedProcedureItemReader = new StoredProcedureItemReader();
        storedProcedureItemReader.setDataSource(dataSource);
        storedProcedureItemReader.setProcedureName("testReturn");
        storedProcedureItemReader.setRowMapper(new AutoBatchDeductionRowMapper());
        return storedProcedureItemReader;
    }

    @Bean
    public ItemProcessor<AutoBatchDeduction, AutoBatchDeduction> processor() {
        ConcreteHandler concreteHandler = new ConcreteHandler();
        return concreteHandler;
    }

    @Bean
    public ItemWriter<AutoBatchDeduction> writer(@Qualifier("localDataSource") DataSource dataSource) {
        JpaItemWriter jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(new EntityManagerFactory() {
            @Override
            public EntityManager createEntityManager() {
                return null;
            }

            @Override
            public EntityManager createEntityManager(Map map) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
                return null;
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return null;
            }

            @Override
            public Metamodel getMetamodel() {
                return null;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public Map<String, Object> getProperties() {
                return null;
            }

            @Override
            public Cache getCache() {
                return null;
            }

            @Override
            public PersistenceUnitUtil getPersistenceUnitUtil() {
                return null;
            }

            @Override
            public void addNamedQuery(String s, Query query) {

            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {

            }
        });
        return jpaItemWriter;
    }

    @Bean
    public MyJobListener myJobListener() {
        return new MyJobListener();
    }
}
