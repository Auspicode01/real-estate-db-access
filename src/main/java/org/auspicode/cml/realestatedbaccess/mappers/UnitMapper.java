package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UnitMapper {

    @Mapping(target = "id", source = "createUnitRequest.id")
    @Mapping(target = "street", source = "createUnitRequest.street")
    @Mapping(target = "postalCode", source = "createUnitRequest.postalCode")
    @Mapping(target = "article", source = "createUnitRequest.article")
    @Mapping(target = "registerNumber", source = "createUnitRequest.registerNumber")
    @Mapping(target = "town", source = "createUnitRequest.town")
    @Mapping(target = "fraction", source = "createUnitRequest.fraction")
    @Mapping(target = "typology", source = "createUnitRequest.typology")
    @Mapping(target = "landlordEntity", source = "landlordEntity")
    @Mapping(target = "rooms", ignore = true)
    UnitEntity toEntity(CreateUnitRequest createUnitRequest, LandlordEntity landlordEntity);

    @Mapping(target = "landlordEntity", source = "landlordEntity.id")
    UnitResponse toModel(UnitEntity unitEntity);

    List<UnitResponse> toModel(List<UnitEntity> unitEntityList);

    void updateUnitToEntity(UpdateUnitRequest updateUnitRequest, @MappingTarget UnitEntity unitEntity);
}
