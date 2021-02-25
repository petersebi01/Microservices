package microservices.user.api;

import microservices.user.api.asemblers.Asembler;
import microservices.user.api.asemblers.RoleAsembler;
import microservices.user.api.exceptions.ApiException;
import microservices.user.dto.RoleDto;
import microservices.user.models.Role;
import microservices.user.services.RoleService;
import microservices.user.services.exceptions.ServiceException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/roles")
//@DeclareRoles({UserRoles.CEO, UserRoles.EMPLOYEE, UserRoles.MANAGER})
public class RoleResource {

    private final RoleService roleService;
    private final Asembler<Role, RoleDto> roleAsembler;

    @Autowired
    private Logger logger;

    public RoleResource( final RoleService roleService, final RoleAsembler roleAsembler) {
        this.roleService = roleService;
        this.roleAsembler = roleAsembler;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        try {
            return ResponseEntity.ok(roleService.findAll().stream().map(roleAsembler::modelToDto).collect(Collectors.toList()));
        } catch (ServiceException e) {
            logger.error("No roles to list");
            throw new ApiException("No roles to list");
        }
    }

    //@GetMapping("/{Name}")
    //public ResponseEntity<Role>
}
