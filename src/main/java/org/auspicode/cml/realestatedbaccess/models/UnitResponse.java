package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "street",
        "postalCode",
        "article",
        "registerNumber",
        "town",
        "fraction",
        "typology",
        "rooms"
})
public class UnitResponse {


    @NotNull
    @JsonProperty("id")
    private String id;

    @NotBlank
    @Size(max = 80)
    @JsonProperty("street")
    private String street;

    @NotBlank
    @Size(min = 8, max = 8)
    @JsonProperty("postalCode")
    private String postalCode;

    @NotBlank
    @Size(min = 4, max = 4)
    @JsonProperty("article")
    private String article;

    @NotBlank
    @Size(min = 4, max = 4)
    @JsonProperty("registerNumber")
    private String registerNumber;

    @NotBlank
    @Size(max = 50)
    @JsonProperty("town")
    private String town;

    @Size(max = 6)
    @JsonProperty("fraction")
    private String fraction;

    @NotBlank
    @JsonProperty("typology")
    private String typology;

    @JsonProperty("rooms")
    private List<UnitRoomsResponse> rooms;
}
