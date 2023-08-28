package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.models.ContractResponse;
import org.auspicode.cml.realestatedbaccess.models.CreateContractRequest;
import org.auspicode.cml.realestatedbaccess.services.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Contract")
@AllArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @Operation(
            summary = "List Contracts",
            description = "Get all Contracts currently registered in the database"
    )
    @GetMapping(value = "/contract/list", produces = {"application/json"})
    public ResponseEntity<List<ContractResponse>> retrieveContracts() {
        return new ResponseEntity<>(contractService.retrieveContracts(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Contract by Primary Key",
            description = "Retrieve a Contract by its ID"
    )
    @GetMapping(value = "/contract", produces = {"application/json"})
    public ResponseEntity<ContractResponse> findOne(@RequestParam(name = "id", required = true) Long id) {
        return new ResponseEntity<>(contractService.findOne(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Contracts by Unit id",
            description = "Retrieve all Contracts by its associated Unit"
    )
    @GetMapping(value = "/contract/unit", produces = {"application/json"})
    public ResponseEntity<List<ContractResponse>> findByUnitId(@RequestParam(name = "unitId", required = true) String unitId) {
        return new ResponseEntity<>(contractService.findByUnitId(unitId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Contracts by Landlord nif",
            description = "Retrieve all Contracts by its associated landlord"
    )
    @GetMapping(value = "/contract/landlord", produces = {"application/json"})
    public ResponseEntity<List<ContractResponse>> findByLandlordNif(@RequestParam(name = "landlordNif", required = true) String landlordNif) {
        return new ResponseEntity<>(contractService.findByLandlordNif(landlordNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Contracts by Tenant nif",
            description = "Retrieve all Contracts by its associated tenant"
    )
    @GetMapping(value = "/contract/tenant", produces = {"application/json"})
    public ResponseEntity<List<ContractResponse>> findByTenantNif(@RequestParam(name = "tenantNif", required = true) String tenantNif) {
        return new ResponseEntity<>(contractService.findByTenantNif(tenantNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Contract by Room id",
            description = "Retrieve a Contract by its associated Room"
    )
    @GetMapping(value = "/contract/room", produces = {"application/json"})
    public ResponseEntity<ContractResponse> findByRoomId(@RequestParam(name = "roomId", required = true) Long roomId) {
        return new ResponseEntity<>(contractService.findByRoomId(roomId), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Contract",
            description = "Define a new Contract's fields and store it in the database"
    )
    @PostMapping(value = "/contract", produces = {"application/json"})
    public ResponseEntity<ContractResponse> createContract(@Valid @RequestBody CreateContractRequest contractRequest) {
        return new ResponseEntity<>(contractService.createContract(contractRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Contract",
            description = "Delete a Contract and all its associated information with the exception of the entities: Unit, Room, Tenant"
    )
    @DeleteMapping(value = "/contract", produces = {"application/json"})
    public ResponseEntity<ContractResponse> deleteContract(@RequestParam(name = "id", required = true) Long id) {
        return new ResponseEntity<>(contractService.deleteContract(id), HttpStatus.OK);
    }
}
