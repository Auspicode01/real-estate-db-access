package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
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
@AllArgsConstructor
public class UnitController {

    private final UnitService unitService;

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
    public ResponseEntity<UnitResponse> findOne(@RequestParam(name = "unitId", required = true) String unitId) {
        return new ResponseEntity<>(unitService.findOne(unitId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Units by Landlord Nif",
            description = "Retrieve all Units with a given LANDLORD_NIF"
    )
    @GetMapping(value = "/unit/landlord", produces = {"application/json"})
    public ResponseEntity<List<UnitResponse>> findByLandlordNif(@RequestParam(name = "landlordNif", required = true) String landlordNif) {
        return new ResponseEntity<>(unitService.findByLandlordNif(landlordNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Unit",
            description = "Define a new Unit's fields and store it in the database"
    )
    @PostMapping(value = "/unit", produces = {"application/json"})
    public ResponseEntity<UnitResponse> createUnit(@RequestParam(name = "landlordNif", required = true) String landlordNif,
                                                   @Valid @RequestBody CreateUnitRequest createUnitRequest) {
        return new ResponseEntity<>(unitService.createUnit(landlordNif, createUnitRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Unit",
            description = "Update a Unit currently registered in the database. Only the following fields can be updated: unitId, town and typology"
    )
    @PutMapping(value = "/unit")
    public ResponseEntity<UnitResponse> updateUnit(@RequestParam(name = "unitId", required = true) String unitId, @Valid @RequestBody(required = true) UpdateUnitRequest updateUnitRequest) {
        return new ResponseEntity<>(unitService.updateUnit(unitId, updateUnitRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Unit",
            description = "Delete a Unit and all its associated information including all its Rooms"
    )
    @DeleteMapping(value = "/unit", produces = {"application/json"})
    public ResponseEntity<UnitResponse> deleteUnit(@RequestParam(name = "unitId", required = true) String unitId) {
        return new ResponseEntity<>(unitService.deleteUnit(unitId), HttpStatus.OK);
    }
}
