package org.auspicode.cml.realestatedbaccess.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TENANT_CONTACTS")
public class TenantContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("contacts")
    @JoinColumn(name = "TENANT_NIF", referencedColumnName = "NIF", nullable = false, updatable = false)
    @JoinColumn(name = "TENANT_ID_CARD_NUMBER", referencedColumnName = "ID_CARD_NUMBER", nullable = false, updatable = false)
    @JoinColumn(name = "TENANT_FULL_NAME", referencedColumnName = "FULL_NAME", nullable = false, updatable = false)
    private TenantEntity tenantEntity;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "TYPE", nullable = false, updatable = false)
    private ContactType contactType;

    @NotBlank
    @Size(max = 80)
    @Column(name = "`VALUE`", length = 80, nullable = false)
    private String value;
}
