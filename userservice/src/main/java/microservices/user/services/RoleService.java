package microservices.user.services;

import microservices.user.models.Role;
import microservices.user.services.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll() throws ServiceException;

    Optional<Role> findRoleByName(String name) throws ServiceException;
    //public Role findUsersRole(String username);
}
