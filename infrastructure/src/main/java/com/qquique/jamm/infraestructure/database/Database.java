package com.qquique.jamm.infrastructure.database;

import com.qquique.jamm.infrastructure.database.config.XmlConfigurationProvider;
import com.qquique.jamm.infrastructure.database.exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.qquique.jamm.infrastructure.database.config.ConfigurationProvider;
import com.qquique.jamm.infrastructure.util.ErrorMessages;

public class Database {
    private static final Logger logger = LogManager.getLogger(Database.class);

    private final SessionFactory sessionFactory;

    public Database(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory(ConfigurationProvider configurationProvider) {
        try {
            Configuration configuration = configurationProvider.getConfiguration();
            return configuration.buildSessionFactory();
        } catch (HibernateException ex) {
            String msg = ErrorMessages.getErrorMessage("database.error_session_factory");
            logger.error(msg, ex);
            throw new RepositoryException(msg, ex);
        }
    }

    public void close() {
        sessionFactory.close();
    }

    public static Database createWithXmlConfiguration() {
        SessionFactory sessionFactory;
        ConfigurationProvider configurationProvider;
        try {
            configurationProvider = new XmlConfigurationProvider();
            sessionFactory = configurationProvider.getConfiguration().buildSessionFactory();
        } catch (HibernateException ex) {
            String msg = ErrorMessages.getErrorMessage("database.error_session_factory");
            logger.error(msg, ex);
            throw new RepositoryException(msg, ex);
        }
        return new Database(sessionFactory);
    }
}
