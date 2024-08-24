package com.qquique.jamm.infrastructure.database.config;

import org.hibernate.cfg.Configuration;

public class HibernateConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        // Configure Hibernate properties
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/mydatabase");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "password");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        // ... other Hibernate properties ...

        // Add annotated entity classes
        //configuration.addAnnotatedClass(Customer.class);
        //configuration.addAnnotatedClass(Order.class);
        // ... other entity classes ...

        return configuration;
    }
}

