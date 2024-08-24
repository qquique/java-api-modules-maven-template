package com.qquique.jamm.application.mapper;

import com.qquique.jamm.application.dto.DetailDTO;
import com.qquique.jamm.domain.entity.Detail;
import com.qquique.jamm.domain.entity.Header;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DetailMapper {
    DetailMapper INSTANCE = Mappers.getMapper(DetailMapper.class);

    @Mapping(target = "detail.lastDateCreated", ignore = true)
    @Mapping(target = "detail.lastDateModified", ignore = true)
    @Mapping(target = "headerId", source = "header.id")
    DetailDTO mapToDTO(Detail detail);

    @Mapping(target = "lastDateCreated", ignore = true)
    @Mapping(target = "lastDateModified", ignore = true)
    @Mapping(target = "header", ignore = true)
    Detail mapToDomain(DetailDTO detailDTO);

    @Mapping(target = "lastDateCreated", ignore = true)
    @Mapping(target = "lastDateModified", ignore = true)
    @Mapping(target = "header", source = "headerId", qualifiedByName = "mapHeader")
    Detail mapToDomainWithHeader(DetailDTO detailDTO, @Context Header header);

    @Mapping(target = "lastDateCreated", ignore = true)
    @Mapping(target = "lastDateModified", ignore = true)
    @Mapping(target = "header", source = "headerId", qualifiedByName = "mapHeader")
    void updateDetailFromDTO(DetailDTO detailDTO, @MappingTarget Detail detail, @Context Header header);

    @Named("mapHeader")
    default Header mapHeader(Long headerId, @Context Header header) {
        return header;
    }

}
