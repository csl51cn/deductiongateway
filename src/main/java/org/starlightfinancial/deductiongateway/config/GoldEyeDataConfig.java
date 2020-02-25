package org.starlightfinancial.deductiongateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 数据中心数据库配置
 * @date: Created in 2020/2/19 9:34
 * @Modified By:
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "goldeyeEntityManagerFactory",
        transactionManagerRef = "goldeyeTransactionManager",
        basePackages = {"org.starlightfinancial.deductiongateway.domain.goldeye"}) //设置Repository所在位置
public class GoldEyeDataConfig {


    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "goldeyeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.goldeye")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "goldeyeEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder, @Qualifier("goldeyeDataSource") DataSource dataSource) {
        return entityManagerFactory(builder, dataSource).getObject().createEntityManager();
    }

    @Bean(name = "goldeyeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("goldeyeDataSource") DataSource dataSource) {
        Map<String, String> hibernateProperties = jpaProperties.getHibernateProperties(dataSource);
        hibernateProperties.put("hibernate.physical_naming_strategy", "org.starlightfinancial.deductiongateway.dao.strategy.UpperTableStrategy");
        return builder
                .dataSource(dataSource)
                .properties(hibernateProperties)
                //设置实体类所在位置
                .packages("org.starlightfinancial.deductiongateway.domain.goldeye")
                .persistenceUnit("goldeyePersistenceUnit")
                .build();
    }

    @Bean(name = "goldeyeTransactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder, @Qualifier("goldeyeDataSource") DataSource dataSource) {
        return new JpaTransactionManager(entityManagerFactory(builder, dataSource).getObject());
    }



}
