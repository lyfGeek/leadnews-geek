package com.geek.common.mysql.core;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mysql.core")
@PropertySource("classpath:mysql-core-jdbc.properties")
@MapperScan(basePackages = "com.geek.model.mappers", sqlSessionFactoryRef = "mysqlCoreSessionFactory")
public class MysqlCoreConfig {

    String jdbcDriver;
    String jdbcUrl;
    String jdbcUserName;// mysql.core.jdbc-user-name
    String jdbcPassword;
    String rootMapper;// mapper 文件在 classpath 下存放的根路径。
    String aliasesPackage;// 别名包。

    /**
     * 数据库的连接池。
     *
     * @return
     */
    @Bean("mysqlCoreDataSource")
    public DataSource mysqlCoreDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(this.getJdbcUserName());
        dataSource.setPassword(this.getRealPassword());
        dataSource.setJdbcUrl(this.getJdbcUrl());
        dataSource.setDriverClassName(this.getJdbcDriver());
        // 最大连接数。
        dataSource.setMaximumPoolSize(50);
        // 最小连接数。
        dataSource.setMinimumIdle(5);
        return dataSource;
    }

    /**
     * 密码反转操作。
     *
     * @return
     */
    public String getRealPassword() {
        String jdbcPassword = this.getJdbcPassword();// root
        return StringUtils.reverse(jdbcPassword);// toor
    }

    /**
     * 整合 mybatis。SqlSessionFactoryBean。
     *
     * @param dataSource
     * @return
     * @throws IOException
     */
    @Bean("mysqlCoreSessionFactory")
    public SqlSessionFactoryBean mysqlCoreSessionFactory(@Qualifier("mysqlCoreDataSource") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 数据源。
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 别名。
        sqlSessionFactoryBean.setTypeAliasesPackage(this.getAliasesPackage());
        // mapper 接口代理的扫描。类上。
//        @MapperScan(basePackages = "com.geek.model.mappers", sqlSessionFactoryRef = "mysqlCoreSessionFactory")
        // mapper 文件存储的位置。
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(this.getMapperFilePath());// classpath:mappers/**/*.xml。
        sqlSessionFactoryBean.setMapperLocations(resources);

        // 开启驼峰标识。user_name --》 userName。
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean;
    }

    /**
     * 拼接 mapper.xml 文件的存储位置。
     *
     * @return
     */
    public String getMapperFilePath() {
        return "classpath:" + this.getRootMapper() + "/**/*.xml";
    }

}
