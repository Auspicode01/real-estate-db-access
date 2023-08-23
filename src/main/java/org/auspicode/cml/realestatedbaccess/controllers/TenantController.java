package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.services.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Tenant")
@AllArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @Operation(
            summary = "List Tenants",
            description = "Get all Tenants currently registered in the database"
    )
    @GetMapping(value = "/tenant/list")
    public ResponseEntity<List<UserResponse>> listTenants() {
        return new ResponseEntity<>(tenantService.retrieveTenants(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Tenant by Primary Key",
            description = "Get Tenant by all Primary Key fields (NIF, Id Card Number, Full Name)"
    )
    @GetMapping(value = "/tenant")
    public ResponseEntity<UserResponse> getTenant(@RequestParam(name = "tenantNif", required = true) String tenantNif,
                                                  @RequestParam(name = "tenantIdCardNumber", required = true) String tenantIdCardNumber,
                                                  @RequestParam(name = "tenantFullName", required = true) String tenantFullName) {
        return new ResponseEntity<>(tenantService.findOne(tenantNif, tenantIdCardNumber, tenantFullName), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Tenant by NIF",
            description = "Retrieve a Tenant by NIF"
    )
    @GetMapping(value = "/tenant/nif")
    public ResponseEntity<UserResponse> getTenantByNif(@RequestParam(name = "tenantNif", required = true) String tenantNif) {
        return new ResponseEntity<>(tenantService.findByNif(tenantNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Tenant",
            description = "Define a new Tenant's fields and store it in the database"
    )
    @PostMapping(value = "/tenant")
    public ResponseEntity<TenantEntity> createTenant(@RequestBody(required = true) @Valid CreateUserRequest tenant) {
        return new ResponseEntity<>(tenantService.createTenant(tenant), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create Tenant Contact",
            description = "Define a new Tenant's Contact and store it in the database"
    )
    @PostMapping(value = "/tenant/contact")
    public ResponseEntity<UserResponse> createTenantContact(@RequestParam(name = "tenantNif", required = true) String tenantNif, @RequestBody(required = true) @Valid Contact contact) {
        return new ResponseEntity<>(tenantService.createContact(tenantNif, contact), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Tenant",
            description = "Update a Tenant currently registered in the database. Only the nib field can be updated"
    )
    @PatchMapping(value = "/tenant")
    public ResponseEntity<UserResponse> updateTenant(@RequestParam(name = "tenantNif", required = true) String tenantNif, @RequestParam(name = "tenantNib", required = true) String tenantNib) {
        return new ResponseEntity<>(tenantService.updateTenant(tenantNif, tenantNib), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Tenant",
            description = "Delete a Tenant and all its associated information"
    )
    @DeleteMapping(value = "/tenant")
    public ResponseEntity<TenantEntity> deleteTenant(@RequestParam(name = "tenantNif", required = true) String tenantNif) {
        return new ResponseEntity<>(tenantService.deleteTenant(tenantNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Tenant Contact",
            description = "Delete a Tenant's Contact by its nif, type and value"
    )
    @DeleteMapping(value = "/tenant/contact")
    public ResponseEntity<Boolean> deleteTenantContact(@RequestParam(name = "tenantNif", required = true) String tenantNif, @RequestBody(required = true) @Valid Contact contact) {
        return new ResponseEntity<>(tenantService.deleteContact(tenantNif, contact), HttpStatus.OK);
    }
}
