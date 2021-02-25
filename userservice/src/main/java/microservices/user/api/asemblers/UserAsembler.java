package microservices.user.api.asemblers;

import microservices.user.dto.UserDto;
import microservices.user.models.User;
import microservices.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAsembler implements Asembler<User, UserDto>{

    @Autowired
    private UserService userService;

    @Override
    public User dtoToModel(UserDto userDto) {
        User user = new User();

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setId(userDto.getId());
        user.setUuid(userDto.getUuid());
        user.setRoleId(userDto.getRoleId());

        return user;
    }

    @Override
    public UserDto modelToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setFirstname(user.getFirstName());
        userDto.setLastname(user.getLastname());
        userDto.setPassword(user.getPassword());
        userDto.setUsername(user.getUsername());
        userDto.setId(user.getId());
        userDto.setUuid(user.getUuid());
        userDto.setRoleId(user.getRoleId());

        return userDto;
    }

    @Override
    public User update(final UserDto dto) {
        Optional<User> userEntity = userService.findById(dto.getId());

        User user = userEntity.orElseGet(User::new);

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getFirstname() != null) {
            user.setFirstname(dto.getFirstname());
        }
        if (dto.getLastname() != null) {
            user.setLastname(dto.getLastname());
        }
        if (dto.getRoleId() != null) {
            user.setRoleId(dto.getRoleId());
        }

        return user;
    }
}
