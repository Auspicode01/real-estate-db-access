package org.auspicode.cml.realestatedbaccess.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class TenantEntityId {

    @NotBlank
    @Size(min = 11, max = 11)
    @Column(name = "NIF", nullable = false, length = 11, updatable = false)
    private String nif;

    @NotBlank
    @Size(min = 8, max = 8)
    @Column(name = "ID_CARD_NUMBER", nullable = false, length = 8, updatable = false)
    private String idCardNumber;

    @NotBlank
    @Size(max = 80)
    @Column(name = "FULL_NAME", nullable = false, length = 80, updatable = false)
    private String fullName;
}
