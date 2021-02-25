package microservices.user.services.impl;

import microservices.user.models.User;
import microservices.user.repository.UserRepository;
import microservices.user.services.UserService;
import microservices.user.services.exceptions.ServiceException;
import microservices.user.util.PasswordHasher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private Logger logger;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(final User user) {
        try {
            String password = user.getPassword();
            Optional<User> savedUser = userRepository.findByUsername(user.getUsername());
            if (savedUser.isPresent()) {
                logger.info("Logging in with user id: " + savedUser.get().getId());
                if (savedUser.get().getPassword().equals(password)) {
                    return userRepository.findByUsername(user.getUsername());
                }
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            logger.error("Error while logging in.", e);
            throw new ServiceException("Error while logging in with user id: " + user.getId());
        }
    }

    @Override
    public User findRolebyUsername(String username) throws ServiceException {
        try {
            return userRepository.findRoleByUsername(username);
        } catch (DataAccessException e) {
            logger.error("User's role not found.");
            throw new ServiceException("User's role not found.", e);
        }
    }

    @Override
    public User createUser(final User user) {
        try {
            logger.info("Saving user in service");
            user.setPassword(passwordHasher.hash(user.getPassword(), user.getUuid()));
            return userRepository.save(user);
        } catch (DataAccessException e) {
            logger.error("Error while creating user", e);
            throw new ServiceException("Something went wrong! Error while creating user " + user, e);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            if (!user.getPassword().equals("")) {
                user.setPassword(passwordHasher.hash(user.getPassword(), user.getUuid()));
            }
            return userRepository.save(user);
        } catch (DataAccessException e) {
            logger.error("Updating user was not successful.", e);
            throw new ServiceException("Updating user was not successful.", e);
        }
    }

    public void deleteUser(final Long id){
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e){
            logger.error("Deleting user was not successful.", e);
            throw new ServiceException("Something went wrong. Deleting user was not successful.", e);
        }
    }

    public List<User> findAll() {
        try {
            return userRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Something went wrong. Cannot find Users.", e);
            throw new ServiceException("Something went wrong while finding users", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) throws ServiceException {
        try {
            return userRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Something went wrong. Cannot find User with id " + id, e);
            throw new ServiceException("Something went wrong while getting user with id " + id, e);
        }
    }

    @Override
    public Optional<User> findByUsername(String name) throws ServiceException {
        try {
            return userRepository.findByUsername(name);
        } catch (DataAccessException e) {
            logger.error("Something went wrong. Cannot find User with name " + name, e);
            throw new ServiceException("Something went wrong while getting user with name " + name, e);
        }
    }
}
