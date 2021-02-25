package microservices.user.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity{

    @OneToMany(targetEntity = User.class, mappedBy = "roleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<User> user = new ArrayList<User>();

    @Column(name = "Name")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Collection<User> getUser() {
        return user;
    }

    public void setUser(Collection<User> user) {
        this.user = user;
    }
}
