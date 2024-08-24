package com.qquique.jamm.infrastructure.database.config;

import org.hibernate.cfg.Configuration;

public class XmlConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration() {
        // Load configuration from hibernate.cfg.xml
        return new Configuration().configure();
    }
}

