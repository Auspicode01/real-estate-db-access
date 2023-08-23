package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.models.RoomRequest;
import org.auspicode.cml.realestatedbaccess.models.RoomResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateRoomRequest;
import org.auspicode.cml.realestatedbaccess.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(
            summary = "List Rooms",
            description = "Get all Rooms currently registered in the database"
    )
    @GetMapping(value = "/room/list", produces = {"application/json"})
    public ResponseEntity<List<RoomResponse>> retrieveRooms() {
        return new ResponseEntity<>(roomService.retrieveRooms(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Room by Primary Key",
            description = "Retrieve a Room by its ID"
    )
    @GetMapping(value = "/room", produces = {"application/json"})
    public ResponseEntity<RoomResponse> findOne(@RequestParam(name = "id", required = true) Long id) {
        return new ResponseEntity<>(roomService.findOne(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Rooms by Unit id",
            description = "Retrieve all Rooms by its associated Unit"
    )
    @GetMapping(value = "/room/unit", produces = {"application/json"})
    public ResponseEntity<List<RoomResponse>> findByUnitId(@RequestParam(name = "unitId", required = true) String unitId) {
        return new ResponseEntity<>(roomService.findByUnitId(unitId), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Room",
            description = "Define a new Room's fields and store it in the database"
    )
    @PostMapping(value = "/room", produces = {"application/json"})
    public ResponseEntity<RoomEntity> createRoom(@Valid @RequestBody RoomRequest roomRequest) {
        return new ResponseEntity<>(roomService.createRoom(roomRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Room",
            description = "Update a Room currently registered in the database. Only the following fields can be updated: unitId, price, capacity and isSuite"
    )
    @PutMapping(value = "/room", produces = {"application/json"})
    public ResponseEntity<RoomResponse> updateRoom(@RequestParam Long id, @Valid @RequestBody UpdateRoomRequest updateRoomRequest) {
        return new ResponseEntity<>(roomService.updateRoom(id, updateRoomRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Room",
            description = "Delete a Room and all its associated information"
    )
    @DeleteMapping(value = "/room", produces = {"application/json"})
    public ResponseEntity<RoomEntity> deleteRoom(@RequestParam Long id) {
        return new ResponseEntity<>(roomService.deleteRoom(id), HttpStatus.OK);
    }
}
