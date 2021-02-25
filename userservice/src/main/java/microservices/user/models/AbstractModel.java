package microservices.user.models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Objects;
import java.util.UUID;


@MappedSuperclass
public abstract class AbstractModel {

    @Column(name = "uuid", nullable = false, length = 36, unique = true)
    private String uuid;

    public String getUuid(){
        ensureUuid();
        return uuid;
    }

    public void setUuid(final String uuid){
        this.uuid = uuid;
    }

    @PrePersist
    private void beforePersist(){
        ensureUuid();
    }

    private void ensureUuid(){
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractModel that = (AbstractModel) o;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}
