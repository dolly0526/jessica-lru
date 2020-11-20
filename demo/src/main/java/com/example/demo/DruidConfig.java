package com.example.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author yusenyang
 * @create 2020/11/11 10:39
 */
@Configuration
public class DruidConfig {

//    @Value("${spring.datasource.druid.url}")
//    private String dbUrl;
//    @Value("${spring.datasource.druid.username}")
//    private String username;
//    @Value("${spring.datasource.druid.password}")
//    private String password;
//    @Value("${spring.datasource.druid.driver-class-name}")
//    private String driverClassName;
//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//    @Value("${spring.datasource.minIdle}")
//    private int minIdle;
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//    @Value("${spring.datasource.maxWait}")
//    private int maxWait;
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.druid.filters}")
    private String filters;
//    @Value("${spring.datasource.connectionProperties}")
//    private String connectionProperties;
//    @Value("${spring.datasource.useGlobalDataSourceStat}")
//    private boolean useGlobalDataSourceStat;

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource bigdataAppDs() {
        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(this.dbUrl);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);

        //configuration
//        datasource.setInitialSize(initialSize);
//        datasource.setMinIdle(minIdle);
//        datasource.setMaxActive(maxActive);
//        datasource.setMaxWait(maxWait);
//        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        datasource.setValidationQuery(validationQuery);
//        datasource.setTestWhileIdle(testWhileIdle);
//        datasource.setTestOnBorrow(testOnBorrow);
//        datasource.setTestOnReturn(testOnReturn);
//        datasource.setPoolPreparedStatements(poolPreparedStatements);
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: "+ e);
        }
//        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    @Bean
    public JdbcTemplate bigdataApp(@Qualifier("bigdataAppDs") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 这里其实就相当于servlet的web.xml
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "adminQAZWSX");
        servletRegistrationBean.addInitParameter("allow", "");
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    // 再配置一个过滤器，Servlet按上面的方式注册Filter也只能这样
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }
}
