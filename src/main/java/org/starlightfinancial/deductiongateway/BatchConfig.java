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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.starlightfinancial.deductiongateway.common.MyJobListener;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRowMapper;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeductionRowMapper;
import org.starlightfinancial.deductiongateway.service.impl.AutoBatchItemWriter;
import org.starlightfinancial.deductiongateway.service.impl.ConcreteHandler;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

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

    @Bean(name = "autoDeduction")
    public Job autoDeduction(JobBuilderFactory jobs, @Qualifier("s1") Step s1) {
        return jobs.get("autoDeduction")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .listener(myJobListener())
                .build();
    }


    @Bean(name = "accountAutoBatchImport")
    public Job accountAutoBatchImport(JobBuilderFactory jobs, @Qualifier("s0") Step s0) {
        return jobs.get("accountAutoBatchImport")
                .incrementer(new RunIdIncrementer())
                .flow(s0)
                .end()
                .listener(myJobListener())
                .build();
    }


    @Bean(name = "s0")
    public Step step0(StepBuilderFactory stepBuilderFactory, ItemReader<AccountManager> reader, ItemWriter<AccountManager> writer
            , @Qualifier("localTransactionManager") PlatformTransactionManager tx) {
        return stepBuilderFactory.get("step0")
                .transactionManager(tx)
                .<AccountManager, AccountManager>chunk(65000)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean(name = "r0")
    public ItemReader<AccountManager> accountAutoBatchReader(@Qualifier("remoteDataSource") DataSource dataSource) {
        LocalDate now = LocalDate.now();
        LocalDate yesterday = now.minusDays(1);
        JdbcCursorItemReader jdbcCursorItemReader = new JdbcCursorItemReader();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setRowMapper(new AccountManagerRowMapper());
        jdbcCursorItemReader.setSql("SELECT " +
                " a.date_id  AS dateid , " +
                " d.合同编号 AS contractno, " +
                " d.业务编号 AS bizno, " +
                " f.Word AS certificatetype, " +
                " b.客户名称 AS accountName, " +
                " b.身份证号码 AS certificateno, " +
                " g.content AS account, " +
                " i.Word AS bankname, " +
                " ( " +
                "  CASE a.form_arrno " +
                "  WHEN 8 THEN " +
                "   1 " +
                "  WHEN 34 THEN " +
                "   2 " +
                "  WHEN 45 THEN " +
                "   3 " +
                "  WHEN 56 THEN " +
                "   4 " +
                "  WHEN 67 THEN " +
                "   5 " +
                "  ELSE " +
                "   5 " +
                "  END " +
                " ) AS sort " +
                "FROM " +
                " WorkData_Member a " +
                "LEFT JOIN Data_MemberInfo b ON b.id = a.member " +
                "LEFT JOIN Data_WorkInfo d ON d.Date_Id = a.date_id " +
                "LEFT JOIN WorkData_Dictionary e ON e.date_id = a.date_id  " +
                "LEFT JOIN Dictionary f ON f.id = e.content " +
                "LEFT JOIN WorkData_Text g ON g.date_id = a.date_id " +
                "LEFT JOIN WorkData_Dictionary h ON h.date_id = a.date_id  " +
                "LEFT JOIN Dictionary i ON i.id = h.content " +
                "WHERE " +
                " d.是否放款 = 485 AND " +
                " a.Flow_No IN ( " +
                "  SELECT " +
                "   Flow_No " +
                "  FROM " +
                "   WorkFlowConstruction " +
                "  WHERE " +
                "   Flow_Title = '现场签约' " +
                " ) " +
                "AND e.Flow_NO IN ( " +
                " SELECT " +
                "  Flow_No " +
                " FROM " +
                "  WorkFlowConstruction " +
                " WHERE " +
                "  Flow_Title = '现场签约' " +
                ") " +
                "AND g.Flow_NO IN ( " +
                " SELECT " +
                "  Flow_No " +
                " FROM " +
                "  WorkFlowConstruction " +
                " WHERE " +
                "  Flow_Title = '现场签约' " +
                ") " +
                "AND h.Flow_NO IN ( " +
                " SELECT " +
                "  Flow_No " +
                " FROM " +
                "  WorkFlowConstruction " +
                " WHERE " +
                "  Flow_Title = '现场签约' " +
                ") " +
                "AND ( " +
                " ( " +
                "  a.form_arrno = 8 " +
                "  AND e.form_arrno = 19 " +
                "  AND g.Form_Arrno = 11 " +
                "  AND h.form_arrno = 9 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 34 " +
                "  AND e.form_arrno = 36 " +
                "  AND g.Form_Arrno = 40 " +
                "  AND h.form_arrno = 42 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 45 " +
                "  AND e.form_arrno = 47 " +
                "  AND g.Form_Arrno = 51 " +
                "  AND h.form_arrno = 53 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 56 " +
                "  AND e.form_arrno = 58 " +
                "  AND g.Form_Arrno = 62 " +
                "  AND h.form_arrno = 64 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 67 " +
                "  AND e.form_arrno = 69 " +
                "  AND g.Form_Arrno = 73 " +
                "  AND h.form_arrno = 75 " +
                " ) " +
                ") " +
                "AND a.GoBackId = 0 " +
                "AND e.GoBackId = 0 " +
                "AND g.GoBackId = 0 " +
                "AND h.GoBackId = 0 " +
                "AND (d.代扣卡号 IS NOT NUll OR d.代扣卡号 <>  '') " +
//                "AND d.放款日期 <= '2017-08-29' " +
//                "AND d.放款日期 >= '2017-06-2'");
         "AND d.放款日期 >= " + yesterday.toString());
        // TODO: 2017-08-30   放款时间为昨天

        return jdbcCursorItemReader;
    }

    @Bean(name = "w0")
    public ItemWriter<AccountManager> accountAutoImportWriter(@Qualifier("localEntityManagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaItemWriter jpaItemWriter = new JpaItemWriter();
        jpaItemWriter.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return jpaItemWriter;
    }

    @Bean(name = "s1")
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<AutoBatchDeduction> reader, ItemWriter<List> writer,
                      ItemProcessor<AutoBatchDeduction, List> processor,
                      @Qualifier("localTransactionManager") PlatformTransactionManager tx) {
        return stepBuilderFactory.get("step1")
                .transactionManager(tx)
                .<AutoBatchDeduction, List>chunk(65000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "r1")
    public ItemReader<AutoBatchDeduction> reader(@Qualifier("remoteDataSource") DataSource dataSource) {
        JdbcCursorItemReader jdbcCursorItemReader = new JdbcCursorItemReader();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setRowMapper(new AutoBatchDeductionRowMapper());
        jdbcCursorItemReader.setSql("SELECT * FROM Temp_当前代扣数据 WHERE CONVERT (VARCHAR, 计划还款日, 1) = CONVERT (VARCHAR, GETDATE(), 1)");
        return jdbcCursorItemReader;
    }

    @Bean(name = "p1")
    public ItemProcessor<AutoBatchDeduction, List> processor() {
        ConcreteHandler concreteHandler = new ConcreteHandler();
        return concreteHandler;
    }

    @Bean(name = "w1")
    public ItemWriter<List> writer(@Qualifier("localEntityManagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        AutoBatchItemWriter autoBatchItemWriter = new AutoBatchItemWriter();
        autoBatchItemWriter.setJpaItemWriter(new JpaItemWriter());
        autoBatchItemWriter.setLocalContainerEntityManagerFactoryBean(localContainerEntityManagerFactoryBean);
        return autoBatchItemWriter;
    }

    @Bean
    public MyJobListener myJobListener() {
        return new MyJobListener();
    }
}
