package org.auspicode.cml.realestatedbaccess.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TENANTS")
public class TenantEntity {

    @EmbeddedId
    private TenantEntityId id;

    @NotNull
    @Column(name = "BIRTH_DATE", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Size(max = 100)
    @Column(name = "ORIGINAL_ADDRESS", length = 100)
    private String originalAddress;

    @NotBlank
    @Size(min = 25, max = 25)
    @Column(name = "NIB", nullable = false, length = 25)
    private String nib;

    @ManyToMany(mappedBy = "tenants", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tenants")
    private Set<ContractEntity> contracts;

    @OneToMany(mappedBy = "tenantEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("tenantEntity")
    private Set<TenantContactEntity> contacts;
}
