package com.qquique.jamm.application.service;

import com.qquique.jamm.application.dto.UserDTO;
import com.qquique.jamm.application.mapper.UserMapper;
import com.qquique.jamm.application.service.exception.ServiceException;
import com.qquique.jamm.infrastructure.database.repository.UserRepositoryImpl;
import com.qquique.jamm.domain.entity.User;
import com.qquique.jamm.infrastructure.database.exception.RepositoryException;
import com.qquique.jamm.application.util.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepositoryImpl repository;

    public UserService(UserRepositoryImpl userRepository) {
        this.repository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        try {
            List<User> userList = this.repository.findAll();
            List<UserDTO> userDTOList = new ArrayList<>();
            userList
                    .stream()
                    .map(UserMapper.INSTANCE::mapToDTO)
                    .forEach(userDTOList::add);
            return userDTOList;
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_retrieving_list");
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public UserDTO getUserById(Long id) {
        try {
            Optional<User> userOptional = this.repository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return UserMapper.INSTANCE.mapToDTO(user);
            }
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_retrieving_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
        return null;
    }

    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = UserMapper.INSTANCE.mapToDomain(userDTO);
            user = this.repository.save(user);
            return UserMapper.INSTANCE.mapToDTO(user);
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_creating_record");
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            Optional<User> userOptional = this.repository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserMapper.INSTANCE.updateUserFromDTo(userDTO, user);
                user = this.repository.save(user);
                return UserMapper.INSTANCE.mapToDTO(user);
            } else {
                return null;
            }
        } catch (IllegalStateException | RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_updating_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public void deleteUser(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_deleting_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }
}
