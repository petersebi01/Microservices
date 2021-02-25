package microservices.user.models;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity extends AbstractModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }



}
