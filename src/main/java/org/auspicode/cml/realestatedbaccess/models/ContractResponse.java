package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "startDate",
        "endDate",
        "landlords",
        "tenants",
        "unit",
        "room",
        "type"
})
public class ContractResponse {

    @NotNull
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("startDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull
    @JsonProperty("endDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotNull
    @JsonProperty("tenants")
    private Set<ContractUserResponse> tenants;

    @NotNull
    @JsonProperty("landlords")
    private Set<ContractUserResponse> landlords;

    @NotNull
    @JsonProperty("unit")
    private ContractUnitResponse unit;

    @NotNull
    @JsonProperty("room")
    private RoomResponse room;

    @NotNull
    @JsonProperty("type")
    private String type;
}
