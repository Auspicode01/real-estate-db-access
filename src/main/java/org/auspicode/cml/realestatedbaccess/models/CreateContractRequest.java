package org.auspicode.cml.realestatedbaccess.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "startDate",
        "endDate",
        "tenantsNif",
        "landlordsNif",
        "unitId",
        "roomId",
        "type"
})
public class CreateContractRequest {

    @JsonProperty("startDate")
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonProperty("endDate")
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @JsonProperty("tenantsNif")
    @NotNull
    private List<String> tenantsNif;

    @JsonProperty("landlordsNif")
    @NotNull
    private List<String> landlordsNif;

    @JsonProperty("unitId")
    @NotNull
    private String unitId;

    @JsonProperty("roomId")
    @NotNull
    private Long roomId;

    @JsonProperty("type")
    @NotNull
    private String type;
}
