package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "street",
        "postalCode",
        "town",
        "fraction",
        "typology",
})
@Generated
public class ContractUnitResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("street")
    private String street;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("town")
    private String town;

    @JsonProperty("fraction")
    private String fraction;

    @JsonProperty("typology")
    private String typology;
}
