package microservices.user.api;

import microservices.user.amqp.AmqpQueue;
import microservices.user.api.asemblers.Asembler;
import microservices.user.api.asemblers.UserAsembler;
import microservices.user.api.exceptions.ApiException;
import microservices.user.dto.UserDto;
import microservices.user.models.User;
import microservices.user.services.UserService;
import microservices.user.services.exceptions.ServiceException;
import microservices.user.util.UserRoles;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/users")
@DeclareRoles({UserRoles.CEO, UserRoles.EMPLOYEE, UserRoles.MANAGER})
public class UserResource {

    private final UserService userService;
    private final Asembler<User, UserDto> userAsembler;

    @Autowired
    private Logger logger;

    @Autowired
    public UserResource(final UserService userService, final UserAsembler userAsembler){
        this.userService = userService;
        this.userAsembler = userAsembler;
    }

    @PermitAll
    @GetMapping("/login")
    public ResponseEntity<UserDto> userAuth(@RequestBody UserDto userDto) {
        try {
            logger.info("Logging in...");
            Optional<User> loggedInUser = userService.login(userAsembler.dtoToModel(userDto));
            return loggedInUser.map(user -> ResponseEntity.ok(userAsembler.modelToDto(user)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ServiceException e) {
            logger.error("Invalid username or password");
            throw new ApiException("Invalid username or password", e);
        }
    }

    @GetMapping("/userdata")
    public ResponseEntity startSendData(){
        try {
            logger.info("Sending back entities");
            AmqpQueue amqpQueue = new AmqpQueue(userService.findAll());
            amqpQueue.sendtoQueue();
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            logger.error("Cannot send userdata.");
            throw new ApiException("Cannot find users.");
        }
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        try {
            logger.info("Getting all users.");
            return ResponseEntity.ok(userService.findAll().stream().map(userAsembler::modelToDto).collect(Collectors.toList()));
        } catch (ServiceException e) {
            logger.error("Cannot find users.");
            throw new ApiException("Cannot find users.");
        }
    }

    @RolesAllowed({UserRoles.MANAGER, UserRoles.CEO})
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable final String username){
        try {
            final Optional<User> userByName = userService.findByUsername(username);
            return userByName.map(user -> ResponseEntity.ok(userAsembler.modelToDto(user)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (final ServiceException e) {
            logger.error("Cannot fetch user by name: " + username);
            throw new ApiException("Cannot fetch user by name: " + username);
        }
    }

    @RolesAllowed({UserRoles.MANAGER, UserRoles.CEO})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable final Long id){
        try {
            final Optional<User> userById = userService.findById(id);
            return userById.map(user -> ResponseEntity.ok(userAsembler.modelToDto(user)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (final ServiceException e) {
            logger.error("Cannot fetch user by id: " + id);
            throw new ApiException("Cannot fetch user by id: " + id);
        }
    }

    @RolesAllowed({UserRoles.MANAGER, UserRoles.CEO})
    @PostMapping
    public ResponseEntity<UserDto> createOrUpdate(@RequestBody final UserDto userToSave){
        try{
            logger.info("Saving user in resource");
            final User savedUser = userService.createUser(userAsembler.dtoToModel(userToSave));
            return ResponseEntity.ok(userAsembler.modelToDto(savedUser));
        } catch (final ServiceException e) {
            logger.error("Cannot save user");
            throw new ApiException("Cannot save user", e);
        }
    }

    @RolesAllowed({UserRoles.CEO})
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserData(@PathVariable final Long id, @RequestBody final UserDto userToUpdate){
        try {
            logger.debug("Finding user");
            final Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                logger.debug("User is present. Updating...");
                final User updatedUser = userService.updateUser(userAsembler.update(userToUpdate));
                return ResponseEntity.ok(userAsembler.modelToDto(updatedUser));
            } else {
                logger.error("User not found.");
                throw new ApiException("User not found");
            }
        } catch (final ServiceException e) {
            logger.error("Cannot update user.", e);
            throw new ApiException("Cannot update user.", e);
        }
    }

    @RolesAllowed({UserRoles.MANAGER, UserRoles.CEO})
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable final Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (final ServiceException e) {
            logger.error("Cannot delete user.");
            throw new ApiException("Cannot delete user");
        }
    }
}
