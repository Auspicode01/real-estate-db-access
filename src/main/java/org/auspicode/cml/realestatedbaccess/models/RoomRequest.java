package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "unitId",
        "price",
        "capacity",
        "isSuite"
})
public class RoomRequest {

    @NotBlank
    @JsonProperty("unitId")
    private String unitId;

    @NotNull
    @JsonProperty("price")
    private Integer price;

    @Max(2)
    @JsonProperty("capacity")
    private Integer capacity;

    @JsonProperty("isSuite")
    private Boolean isSuite;
}
