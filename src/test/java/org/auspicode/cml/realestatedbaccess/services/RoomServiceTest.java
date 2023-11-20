package org.auspicode.cml.realestatedbaccess.services;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.exception.AllRoomsCreatedForUnitException;
import org.auspicode.cml.realestatedbaccess.exception.RoomIsOccupiedException;
import org.auspicode.cml.realestatedbaccess.models.RoomRequest;
import org.auspicode.cml.realestatedbaccess.models.RoomResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateRoomRequest;
import org.auspicode.cml.realestatedbaccess.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.*;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.ROOM_ID;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.UNIT_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DBRider
@DBUnit(allowEmptyFields = true)
@SpringBootTest
class RoomServiceTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenRetrieveRooms_ReturnRoomsInDB() {
        List<RoomResponse> roomResponseList = roomService.retrieveRooms();

        assertThat(roomResponseList.size()).isEqualTo(3);
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenFindOneRoom_ReturnRoom() {
        RoomResponse roomResponse = roomService.findOne(ROOM_ID);

        assertThat(roomResponse.getId()).isEqualTo(ROOM_ID);
    }

    @Test
    void whenFindOneRoomNotInDB_ReturnRoomNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            roomService.findOne(ROOM_ID);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(ROOM_NOT_IN_DB);
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenFindByUnitId_ReturnRoom() {
        List<RoomResponse> roomResponseList = roomService.findByUnitId(UNIT_ID);

        assertThat(roomResponseList.size()).isEqualTo(2);
        assertThat(roomResponseList.get(1).getUnitId()).isEqualTo(UNIT_ID);
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenFindByUnitIdNotInDB_ReturnEmptyList() {
        List<RoomResponse> roomResponseList = roomService.findByUnitId(UNIT_ID);

        assertThat(roomResponseList.size()).isEqualTo(0);
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenCreateRoom_SaveRoomInDB() {
        RoomRequest roomToSave = RoomRequest.builder()
                .unitId("leirinhas")
                .price(250)
                .capacity(1)
                .isSuite(false)
                .build();

        RoomResponse savedRoom = roomService.createRoom(roomToSave);

        Optional<RoomEntity> result = roomRepository.findById(4L);
        List<RoomResponse> roomResponseList = roomService.retrieveRooms();

        assertThat(roomResponseList.size()).isEqualTo(4);
        assertThat(savedRoom.getId()).isEqualTo(result.get().getId());
        assertThat(savedRoom.getUnitId()).isEqualTo(result.get().getUnitId().getId());
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenCreateRoomToFullUnit_ReturnAllRoomsCreatedForUnitException() {
        RoomRequest roomToSave = RoomRequest.builder()
                .unitId(UNIT_ID)
                .price(250)
                .capacity(1)
                .isSuite(false)
                .build();


        AllRoomsCreatedForUnitException allRoomsCreatedForUnitException = assertThrows(AllRoomsCreatedForUnitException.class, () -> {
            roomService.createRoom(roomToSave);
        });

        assertThat(allRoomsCreatedForUnitException.getMessage()).isEqualTo(ALL_ROOMS_ALREADY_IN_DB);
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenUpdateRoom_changeAllUpdatableValues() {
        UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
                .price(290)
                .capacity(2)
                .isSuite(true)
                .build();
        RoomResponse updatedRoom = roomService.updateRoom(ROOM_ID, updateRoomRequest);

        assertThat(updatedRoom.getPrice()).isEqualTo(updateRoomRequest.getPrice());
        assertThat(updatedRoom.getCapacity()).isEqualTo(updateRoomRequest.getCapacity());
        assertThat(updatedRoom.getIsSuite()).isEqualTo(updateRoomRequest.getIsSuite());
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenUpdateRoom_changePriceValue() {
        UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
                .price(290)
                .build();
        RoomResponse updatedRoom = roomService.updateRoom(ROOM_ID, updateRoomRequest);

        assertThat(updatedRoom.getPrice()).isEqualTo(updateRoomRequest.getPrice());
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenUpdateRoom_changeCapacityValue() {
        UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
                .capacity(2)
                .build();
        RoomResponse updatedRoom = roomService.updateRoom(ROOM_ID, updateRoomRequest);

        assertThat(updatedRoom.getCapacity()).isEqualTo(updateRoomRequest.getCapacity());
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenUpdateRoom_changeIsSuiteValue() {
        UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
                .isSuite(true)
                .build();
        RoomResponse updatedRoom = roomService.updateRoom(ROOM_ID, updateRoomRequest);

        assertThat(updatedRoom.getIsSuite()).isEqualTo(updateRoomRequest.getIsSuite());
    }

    @Test
    @DataSet(value = "datasets/rooms/rooms.yml", cleanAfter = true)
    void whenDeleteRoom_DeleteRoomFromDB() {
        roomService.deleteRoom(ROOM_ID);

        List<RoomResponse> result = roomService.retrieveRooms();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DataSet(value = "datasets/contracts/contracts.yml", cleanAfter = true)
    void whenDeleteRoomInUse_ReturnRoomIsOccupiedExceptionException() {
        RoomIsOccupiedException roomIsOccupiedException = assertThrows(RoomIsOccupiedException.class, () -> {
            roomService.deleteRoom(2L);
        });

        assertThat(roomIsOccupiedException.getMessage()).isEqualTo(ROOM_CAN_NOT_BE_DELETED);
    }
}