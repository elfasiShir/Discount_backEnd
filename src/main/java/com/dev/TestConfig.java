package com.dev;

import com.dev.objects.UserObject;
import com.dev.objects.ShopObject;
import com.dev.objects.DiscountObject;
import com.dev.objects.OrganizationObject;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import java.util.*;

/**
 * Test config that turn on H2 in-memory database.
 * This mode is more convenient for fast starting.
 * Change property spring.profiles.active to "test" for run application in this mode.
 */

@Configuration
@Profile("production")
public class TestConfig {

    @Bean
    public Properties dataSource() throws Exception {
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/ashcollege?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "1234");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
        return settings;
    }

    @Bean
    public SessionFactory sessionFactory() throws Exception {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(dataSource());
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.dev.objects"))));
        Set<Class<? extends Object>> entities = reflections.getSubTypesOf(Object.class);
        for (Class<? extends Object> clazz : entities) {
            configuration.addAnnotatedClass(clazz);
        }
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    @Bean
    public HibernateTransactionManager transactionManager() throws Exception{
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }


}
