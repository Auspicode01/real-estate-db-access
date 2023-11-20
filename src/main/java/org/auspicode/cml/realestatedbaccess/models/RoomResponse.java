package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "unitId",
        "price",
        "isAvailable",
        "capacity",
        "isSuite"
})
@Generated
public class RoomResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("unitId")
    private String unitId;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @Max(2)
    @JsonProperty("capacity")
    private Integer capacity;

    @JsonProperty("isSuite")
    private Boolean isSuite;
}
