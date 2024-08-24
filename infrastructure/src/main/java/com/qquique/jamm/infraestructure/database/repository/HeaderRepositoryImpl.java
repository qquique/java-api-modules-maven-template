package com.qquique.jamm.infrastructure.database.repository;

import com.qquique.jamm.domain.entity.Header;
import com.qquique.jamm.infrastructure.database.exception.RepositoryException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.Optional;

import com.qquique.jamm.infrastructure.util.ErrorMessages;

public class HeaderRepositoryImpl implements Repository<Header> {
    private static final Logger logger = LogManager.getLogger(HeaderRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public HeaderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Header> findAll() {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Header> criteriaQuery = builder.createQuery(Header.class);
            criteriaQuery.from(Header.class);
            Query<Header> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_retrieving_list"), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public Optional<Header> findById(Long id) {
        try (Session session = this.sessionFactory.openSession()) {
            Header header = session.find(Header.class, id);
            return Optional.ofNullable(header);
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_retrieving_id", id), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public Header save(Header header) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (header.getId() == null) {
                session.persist(header);
            } else {
                header = session.merge(header);
            }
            transaction.commit();
            return header;
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_saving"), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = this.sessionFactory.openSession()) {
            Header header = session.find(Header.class, id);
            if (header != null) {
                Transaction transaction = session.beginTransaction();
                session.remove(header);
                transaction.commit();
            }
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_deleting_id", id), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }
}
