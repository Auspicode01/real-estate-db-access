package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.auspicode.cml.realestatedbaccess.entities.ContactType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "contactType",
        "contact"
})
public class Contact {

    @NotNull
    @JsonProperty("contactType")
    private ContactType contactType;

    @NotBlank
    @Size(max = 80)
    @JsonProperty("contact")
    private String contact;
}
