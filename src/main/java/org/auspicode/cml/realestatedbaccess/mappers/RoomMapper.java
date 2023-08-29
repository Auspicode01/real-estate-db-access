package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.models.RoomRequest;
import org.auspicode.cml.realestatedbaccess.models.RoomResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "unitId", source = "unitId.id")
    RoomResponse toModel(RoomEntity roomEntity);

    List<RoomResponse> toModel(List<RoomEntity> roomsList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitId", source = "unitEntity")
    @Mapping(target = "isAvailable", constant = "true")
    @Mapping(target = "capacity", source = "roomRequest.capacity", defaultExpression = "java(1)")
    @Mapping(target = "isSuite", source = "roomRequest.isSuite", defaultExpression = "java(false)")
    RoomEntity toEntity(RoomRequest roomRequest, UnitEntity unitEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitId", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    void updateRoomToEntity(UpdateRoomRequest updateRoomRequest, @MappingTarget RoomEntity roomEntity);
}
