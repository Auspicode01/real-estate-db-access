package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
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
public class UnitUserResponse {

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
}
