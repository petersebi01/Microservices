package microservices.user.api.asemblers;

import microservices.user.dto.AbstractDto;
import microservices.user.models.BaseEntity;

public interface Asembler <MODEL extends BaseEntity, DTO extends AbstractDto>{

    MODEL dtoToModel(DTO dto);
    DTO modelToDto(MODEL model);
    MODEL update(DTO dto);
}
