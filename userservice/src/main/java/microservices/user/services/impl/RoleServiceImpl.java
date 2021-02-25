package microservices.user.services.impl;

import microservices.user.models.Role;
import microservices.user.repository.RoleRepository;
import microservices.user.services.RoleService;
import microservices.user.services.exceptions.ServiceException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    private Logger logger;

    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll(){
        try {
            return roleRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Something went wrong. Cannot find roles.", e);
            throw new ServiceException("Something went wrong while finding user roles", e);
        }
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        try {
            return roleRepository.findByRoleName(name);
        } catch (DataAccessException e) {
            logger.error("Role not found");
            throw new ServiceException("Role not found");
        }
    }
}
