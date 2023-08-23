package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nif",
        "idCardNumber",
        "fullName",
        "birthDate",
        "age",
        "nib",
        "contacts",
        "contracts"
})
public class UserResponse {

    @NotNull
    @Size(min = 11, max = 11)
    @JsonProperty("nif")
    private String nif;

    @NotNull
    @Size(min = 8, max = 8)
    @JsonProperty("idCardNumber")
    private String idCardNumber;

    @NotNull
    @Size(max = 80)
    @JsonProperty("fullName")
    private String fullName;

    @NotNull
    @JsonProperty("birthDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @NotNull
    @JsonProperty("age")
    private int age;

    @NotBlank
    @Size(min = 25, max = 25)
    @JsonProperty("nib")
    private String nib;

    @JsonProperty("contacts")
    private Set<Contact> contacts;

    @JsonProperty("contracts")
    private Set<UserContractResponse> contracts;
}
