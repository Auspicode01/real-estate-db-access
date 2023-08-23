package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Size;
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
        "id",
        "isAvailable",
        "price",
        "capacity",
        "isSuite"
})
public class UnitRoomsResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @JsonProperty("price")
    private Integer price;

    @Size(max = 2)
    @JsonProperty("capacity")
    private Integer capacity;

    @JsonProperty("isSuite")
    private Boolean isSuite;
}
