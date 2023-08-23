package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UnitMapper {

    UnitResponse toModel(UnitEntity unitEntity);

    List<UnitResponse> toModel(List<UnitEntity> unitEntityList);

    void updateUnitToEntity(UpdateUnitRequest updateUnitRequest, @MappingTarget UnitEntity unitEntity);
}
