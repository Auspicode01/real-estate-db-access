package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.services.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @Operation(
            summary = "List Units",
            description = "Get all Units currently registered in the database"
    )
    @GetMapping(value = "/unit/list", produces = {"application/json"})
    public ResponseEntity<List<UnitResponse>> retrieveUnits() {
        return new ResponseEntity<>(unitService.retrieveUnits(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Unit by Primary Key",
            description = "Retrieve a Unit by its ID"
    )
    @GetMapping(value = "/unit", produces = {"application/json"})
    public ResponseEntity<UnitResponse> findOne(@RequestParam(name = "id", required = true) String id) {
        return new ResponseEntity<>(unitService.findOne(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Unit",
            description = "Define a new Unit's fields and store it in the database"
    )
    @PostMapping(value = "/unit", produces = {"application/json"})
    public ResponseEntity<UnitEntity> createUnit(@Valid @RequestBody UnitEntity unitEntity) {
        return new ResponseEntity<>(unitService.createUnit(unitEntity), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Unit",
            description = "Update a Unit currently registered in the database. Only the following fields can be updated: unitId, town and typology"
    )
    @PutMapping(value = "/unit")
    public ResponseEntity<UnitResponse> updateTenant(@RequestParam(name = "unitId", required = true) String id, @Valid @RequestBody(required = true) UpdateUnitRequest updateUnitRequest) {
        return new ResponseEntity<>(unitService.updateUnit(id, updateUnitRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Unit",
            description = "Delete a Unit and all its associated information including all its Rooms"
    )
    @DeleteMapping(value = "/unit", produces = {"application/json"})
    public ResponseEntity<UnitResponse> deleteUnit(@RequestParam(name = "id", required = true) String id) {
        return new ResponseEntity<>(unitService.deleteUnit(id), HttpStatus.OK);
    }
}
