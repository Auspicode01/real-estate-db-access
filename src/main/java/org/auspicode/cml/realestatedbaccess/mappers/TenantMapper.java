package org.auspicode.cml.realestatedbaccess.mappers;


import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.CreateTenantRequest;
import org.auspicode.cml.realestatedbaccess.models.TenantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TenantMapper {

    @Mapping(target = "id.nif", source = "nif")
    @Mapping(target = "id.idCardNumber", source = "idCardNumber")
    @Mapping(target = "id.fullName", source = "fullName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "nib", source = "nib")
    @Mapping(target = "originalAddress", source = "originalAddress")
    TenantEntity createTenantRequestToEntity(CreateTenantRequest createTenantRequest);

    @Mapping(target = "nif", source = "id.nif")
    @Mapping(target = "idCardNumber", source = "id.idCardNumber")
    @Mapping(target = "fullName", source = "id.fullName")
    @Mapping(target = "age", expression = "java(calculateAge(tenantEntity.getBirthDate()))")
    @Mapping(target = "nib", source = "nib")
    @Mapping(target = "contacts")
    TenantResponse toModel(TenantEntity tenantEntity);

    List<TenantResponse> toModel(List<TenantEntity> tenantEntityList);

    default int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
