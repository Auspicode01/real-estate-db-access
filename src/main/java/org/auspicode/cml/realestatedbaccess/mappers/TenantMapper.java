package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserContractResponse;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = ContractUserResponseMapper.class)
public interface TenantMapper {

    @Mapping(target = "id.nif", source = "nif")
    @Mapping(target = "id.idCardNumber", source = "idCardNumber")
    @Mapping(target = "id.fullName", source = "fullName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "nib", source = "nib")
    @Mapping(target = "originalAddress", source = "originalAddress")
    TenantEntity createTenantRequestToEntity(CreateUserRequest createUserRequest);

    @Mapping(target = "nif", source = "id.nif")
    @Mapping(target = "idCardNumber", source = "id.idCardNumber")
    @Mapping(target = "fullName", source = "id.fullName")
    @Mapping(target = "age", expression = "java(calculateAge(tenantEntity.getBirthDate()))")
    @Mapping(target = "units", ignore = true)
    UserResponse toModel(TenantEntity tenantEntity);

    List<UserResponse> toModel(List<TenantEntity> tenantEntityList);

    @Mapping(target = "unit.id", source = "unitId.id")
    @Mapping(target = "unit.street", source = "unitId.street")
    @Mapping(target = "unit.postalCode", source = "unitId.postalCode")
    @Mapping(target = "unit.town", source = "unitId.town")
    @Mapping(target = "unit.fraction", source = "unitId.fraction")
    @Mapping(target = "unit.typology", source = "unitId.typology")
    @Mapping(target = "room.id", source = "roomId.id")
    @Mapping(target = "room.unitId", ignore = true)
    @Mapping(target = "room.isAvailable", source = "roomId.isAvailable")
    @Mapping(target = "room.price", source = "roomId.price")
    @Mapping(target = "room.capacity", source = "roomId.capacity")
    @Mapping(target = "room.isSuite", source = "roomId.isSuite")
    @Mapping(target = "otherParty", source = "landlords")
    UserContractResponse contractEntityToModel(ContractEntity contractEntity);

    default int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
