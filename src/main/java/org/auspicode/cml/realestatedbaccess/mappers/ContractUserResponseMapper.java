package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.ContractUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = TenantMapper.class)
public interface ContractUserResponseMapper {

    @Mapping(target = "nif", source = "id.nif")
    @Mapping(target = "idCardNumber", source = "id.idCardNumber")
    @Mapping(target = "fullName", source = "id.fullName")
    @Mapping(target = "age", expression = "java(calculateAge(tenantEntity.getBirthDate()))")
    ContractUserResponse tenantEntityToModel(TenantEntity tenantEntity);

    @Mapping(target = "nif", source = "id.nif")
    @Mapping(target = "idCardNumber", source = "id.idCardNumber")
    @Mapping(target = "fullName", source = "id.fullName")
    @Mapping(target = "age", expression = "java(calculateAge(landlordEntity.getBirthDate()))")
    ContractUserResponse landlordEntityToModel(LandlordEntity landlordEntity);

    default int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
