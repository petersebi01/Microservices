package microservices.user.api.asemblers;

import microservices.user.dto.RoleDto;
import microservices.user.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleAsembler implements Asembler <Role, RoleDto> {

    @Override
    public Role dtoToModel(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setUuid(dto.getUuid());
        role.setRoleName(dto.getRoleName());
        role.setUser(dto.getUser());

        return role;
    }

    @Override
    public RoleDto modelToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setUuid(role.getUuid());
        roleDto.setRoleName(role.getRoleName());
        roleDto.setUser(role.getUser());

        return roleDto;
    }

    @Override
    public Role update(RoleDto dto) {
        return null;
    }
}
