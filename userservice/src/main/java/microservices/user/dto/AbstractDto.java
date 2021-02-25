package microservices.user.dto;

public abstract class AbstractDto {

    private Long id;
    private String uuid;

    public Long getId(){
        return id;
    }

    public String getUuid(){
        return uuid;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }
}
