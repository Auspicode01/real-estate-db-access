package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateTenantRequest;
import org.auspicode.cml.realestatedbaccess.models.TenantResponse;
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
    public ResponseEntity<List<TenantResponse>> listTenants() {
        return new ResponseEntity<>(tenantService.retrieveTenants(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Tenant by Primary Key",
            description = "Get Tenant by all Primary Key fields (NIF, Id Card Number, Full Name)"
    )
    @GetMapping(value = "/tenant")
    public ResponseEntity<TenantResponse> getTenant(@RequestParam(required = true) String nif,
                                                    @RequestParam(required = true) String idCardNumber,
                                                    @RequestParam(required = true) String fullName) {
        return new ResponseEntity<>(tenantService.findOne(nif, idCardNumber, fullName), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Tenant by NIF",
            description = "Retrieve a Tenant by NIF"
    )
    @GetMapping(value = "/tenant/nif")
    public ResponseEntity<TenantResponse> getTenantByNif(@RequestParam(required = true) String nif) {
        return new ResponseEntity<>(tenantService.findByNif(nif), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Tenant",
            description = "Define a new Tenant's fields and store it in the database"
    )
    @PostMapping(value = "/tenant")
    public ResponseEntity<TenantEntity> createTenant(@RequestBody(required = true) @Valid CreateTenantRequest tenant) {
        return new ResponseEntity<>(tenantService.createTenant(tenant), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create Tenant Contact",
            description = "Define a new Tenant's Contact and store it in the database"
    )
    @PostMapping(value = "/tenant/contact")
    public ResponseEntity<TenantResponse> createTenantContact(@RequestParam(required = true) String nif, @RequestBody(required = true) @Valid Contact contact) {
        return new ResponseEntity<>(tenantService.createContact(nif, contact), HttpStatus.CREATED);
    }

  /*@Operation(
      summary = "Update Tenant",
      description = "Update a Tenant currently registered in the database. Only the following fields can be updated: nib, cellPhone and email"
  )
  @PutMapping(value = "/tenant")
  public ResponseEntity<TenantResponse> updateTenant(@RequestParam(name = "nif", required = true) String nif, @RequestParam(name = "nib", required = true) String nib) {
    return new ResponseEntity<>(tenantService.updateTenant(nif), HttpStatus.OK);
  }*/

    @Operation(
            summary = "Delete Tenant",
            description = "Delete a Tenant and all its associated information"
    )
    @DeleteMapping(value = "/tenant")
    public ResponseEntity<TenantEntity> deleteTenant(@RequestParam(required = true) String nif) {
        return new ResponseEntity<>(tenantService.deleteTenant(nif), HttpStatus.OK);
    }
}
