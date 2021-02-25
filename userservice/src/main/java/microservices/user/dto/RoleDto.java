package microservices.user.dto;

import microservices.user.models.User;

import java.util.Collection;

public class RoleDto extends AbstractDto{

    private String roleName;

    private Collection<User> user;

    public Collection<User> getUser() {
        return user;
    }

    public void setUser(Collection<User> user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        roleName = roleName;
    }
}
