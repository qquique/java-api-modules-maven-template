package com.qquique.jamm.application.mapper;
import com.qquique.jamm.application.dto.HeaderDTO;
import com.qquique.jamm.domain.entity.Header;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DetailMapper.class})
public interface HeaderMapper {
    HeaderMapper INSTANCE = Mappers.getMapper(HeaderMapper.class);

    HeaderDTO mapToDTO(Header header);

    @Mapping(target="lastDateCreated", ignore=true)
    @Mapping(target="lastDateModified", ignore=true)
    Header mapToDomain(HeaderDTO headerDTO);

    @Mapping(target = "lastDateCreated", ignore = true)
    @Mapping(target = "lastDateModified", ignore = true)
    @Mapping(target = "details", ignore = true)
    void updateHeaderFromDTO(HeaderDTO headerDTO, @MappingTarget Header header);
}

