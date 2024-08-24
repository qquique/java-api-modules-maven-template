package com.qquique.jamm.infrastructure.database.repository;

import com.qquique.jamm.domain.entity.User;
import com.qquique.jamm.infrastructure.database.exception.RepositoryException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.qquique.jamm.infrastructure.util.ErrorMessages;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements Repository<User> {
    private static final Logger logger = LogManager.getLogger(UserRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        try (Session session = this.sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            criteriaQuery.from(User.class);
            Query<User> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_retrieving_list"), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = this.sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_retrieving_id", id), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public User save(User user) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (user.getId() == null) {
                session.persist(user);
            } else {
                user = session.merge(user);
            }
            transaction.commit();
            return user;
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_saving"), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = this.sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                Transaction transaction = session.beginTransaction();
                session.remove(user);
                transaction.commit();
            }
        } catch (HibernateException e) {
            logger.error(ErrorMessages.getErrorMessage("repository.error_deleting_id", id), e);
            throw new RepositoryException(ErrorMessages.getErrorMessage("repository.error_database"), e);
        }
    }
}
