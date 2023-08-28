package org.auspicode.cml.realestatedbaccess.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.services.LandlordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Landlord")
@AllArgsConstructor
public class LandlordController {


    private final LandlordService landlordService;

    @Operation(
            summary = "List Landlords",
            description = "Get all Landlords currently registered in the database"
    )
    @GetMapping(value = "/landlord/list")
    public ResponseEntity<List<UserResponse>> listLandlords() {
        return new ResponseEntity<>(landlordService.retrieveLandlords(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Landlord by Primary Key",
            description = "Get Landlord by all Primary Key fields (NIF, Id Card Number, Full Name)"
    )
    @GetMapping(value = "/landlord")
    public ResponseEntity<UserResponse> getLandlord(@RequestParam(name = "landlordNif", required = true) String landlordNif,
                                                    @RequestParam(name = "landlordIdCardNumber", required = true) String landlordIdCardNumber,
                                                    @RequestParam(name = "landlordFullName", required = true) String landlordFullName) {
        return new ResponseEntity<>(landlordService.findOne(landlordNif, landlordIdCardNumber, landlordFullName), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Landlord by NIF",
            description = "Retrieve a Landlord by NIF"
    )
    @GetMapping(value = "/landlord/nif")
    public ResponseEntity<UserResponse> getLandlordByNif(@RequestParam(name = "landlordNif", required = true) String landlordNif) {
        return new ResponseEntity<>(landlordService.findByNif(landlordNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Create Landlord",
            description = "Define a new Landlord's fields and store it in the database"
    )
    @PostMapping(value = "/landlord")
    public ResponseEntity<UserResponse> createLandlord(@RequestBody(required = true) @Valid CreateUserRequest landlord) {
        return new ResponseEntity<>(landlordService.createLandlord(landlord), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create Landlord Contact",
            description = "Define a new Landlord's Contact and store it in the database"
    )
    @PostMapping(value = "/landlord/contact")
    public ResponseEntity<UserResponse> createLandlordContact(@RequestParam(name = "landlordNif", required = true) String landlordNif, @RequestBody(required = true) @Valid Contact contact) {
        return new ResponseEntity<>(landlordService.createContact(landlordNif, contact), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Landlord",
            description = "Update a Landlord currently registered in the database. Only the nib field can be updated"
    )
    @PatchMapping(value = "/landlord")
    public ResponseEntity<UserResponse> updateLandlord(@RequestParam(name = "landlordNif", required = true) String landlordNif, @RequestParam(name = "landlordNib", required = true) String landlordNib) {
        return new ResponseEntity<>(landlordService.updateLandlord(landlordNif, landlordNib), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Landlord",
            description = "Delete a Landlord and all its associated information"
    )
    @DeleteMapping(value = "/landlord")
    public ResponseEntity<UserResponse> deleteLandlord(@RequestParam(name = "landlordNif", required = true) String landlordNif) {
        return new ResponseEntity<>(landlordService.deleteLandlord(landlordNif), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Landlord Contact",
            description = "Delete a Landlord's Contact by its nif, type and value"
    )
    @DeleteMapping(value = "/landlord/contact")
    public ResponseEntity<Boolean> deleteLandlordContact(@RequestParam(name = "landlordNif", required = true) String landlordNif, @RequestBody(required = true) @Valid Contact contact) {
        return new ResponseEntity<>(landlordService.deleteContact(landlordNif, contact), HttpStatus.OK);
    }
}
