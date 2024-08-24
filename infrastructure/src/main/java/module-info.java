module infrastructure {
    requires java.naming;
    requires jakarta.persistence;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires domain;

    exports com.qquique.jamm.infrastructure.database.exception;
    exports com.qquique.jamm.infrastructure.database.repository;
}
