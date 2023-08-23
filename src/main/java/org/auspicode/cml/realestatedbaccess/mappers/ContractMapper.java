package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.models.ContractResponse;
import org.auspicode.cml.realestatedbaccess.models.CreateContractRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = ContractUserResponseMapper.class)
public interface ContractMapper {

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
    ContractResponse toModel(ContractEntity contractEntity);

    List<ContractResponse> toModel(List<ContractEntity> contractEntityList);

    @Mapping(target = "startDate", source = "contractRequest.startDate")
    @Mapping(target = "endDate", source = "contractRequest.endDate")
    @Mapping(target = "tenants", ignore = true)
    @Mapping(target = "unitId", source = "unitEntity")
    @Mapping(target = "roomId", source = "roomEntity")
    @Mapping(target = "type", source = "contractRequest.type")
    @Mapping(target = "id", ignore = true)
    ContractEntity createContractRequestToEntity(CreateContractRequest contractRequest, UnitEntity unitEntity, RoomEntity roomEntity);
}
