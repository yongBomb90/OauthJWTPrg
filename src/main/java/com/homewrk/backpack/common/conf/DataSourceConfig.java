package com.homewrk.backpack.common.conf;


import com.homewrk.backpack.common.database.RoutingDataBase;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * Data Base 설정 및 JPA 설정 처리
 */
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.homewrk.backpack"})
public class DataSourceConfig {

    /**
     * Master Data Base 설정
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * Slave Data Base 설정
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * Routing Data Base 생성
     * @param masterDataSource
     * @param slaveDataSource
     * @return
     */
    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        RoutingDataBase routingDataSource = new RoutingDataBase();

        Map dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        
        //기본값 데이터 베이스 설정
        routingDataSource.setDefaultTargetDataSource(masterDataSource); 

        return routingDataSource;
    }


    /**
     * 최우선 DataSource 빈 생성
     * @param routingDataSource
     * @return
     */
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource); // 읽기일 경우 컨넥션 여부 확인 필요
    }



}
