package microservices.user.services;

import microservices.user.models.User;
import microservices.user.services.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user) throws ServiceException; // ez kapja paraméterként a user-t amit le akarunk menteni

    User updateUser(User user) throws ServiceException;

    void deleteUser(Long id) throws ServiceException;

    Optional<User> findByUsername(String username) throws ServiceException;

    List<User> findAll() throws ServiceException;

    Optional<User> findById(final Long id) throws ServiceException;

    Optional<User> login(User user) throws ServiceException;

    User findRolebyUsername(String username) throws ServiceException;
}

