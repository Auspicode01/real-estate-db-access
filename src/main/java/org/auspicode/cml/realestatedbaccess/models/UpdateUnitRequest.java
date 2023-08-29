package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "street",
        "fraction",
        "typology"
})
public class UpdateUnitRequest {

    @Size(max = 80)
    @JsonProperty("street")
    private String street;

    @Size(max = 6)
    @JsonProperty("fraction")
    private String fraction;

    @JsonProperty("typology")
    private String typology;
}
