package org.auspicode.cml.realestatedbaccess.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONTRACTS")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "START_DATE", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull
    @Column(name = "END_DATE", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_ID", referencedColumnName = "ID", nullable = false, updatable = false)
    private UnitEntity unitId;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", nullable = false, updatable = false)
    private RoomEntity roomId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, updatable = false)
    private ContractType type;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TENANT_CONTRACT",
            joinColumns = @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID", nullable = false, updatable = false),
            inverseJoinColumns = {@JoinColumn(name = "TENANT_NIF", referencedColumnName = "NIF", nullable = false, updatable = false),
                    @JoinColumn(name = "TENANT_ID_CARD_NUMBER", referencedColumnName = "ID_CARD_NUMBER", nullable = false, updatable = false),
                    @JoinColumn(name = "TENANT_FULL_NAME", referencedColumnName = "FULL_NAME", nullable = false, updatable = false)})
    @JsonIgnoreProperties("contracts")
    private Set<TenantEntity> tenants;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "LANDLORD_CONTRACT",
            joinColumns = @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID", nullable = false, updatable = false),
            inverseJoinColumns = {@JoinColumn(name = "LANDLORD_NIF", referencedColumnName = "NIF", nullable = false, updatable = false),
                    @JoinColumn(name = "LANDLORD_ID_CARD_NUMBER", referencedColumnName = "ID_CARD_NUMBER", nullable = false, updatable = false),
                    @JoinColumn(name = "LANDLORD_FULL_NAME", referencedColumnName = "FULL_NAME", nullable = false, updatable = false)})
    @JsonIgnoreProperties("contracts")
    private Set<LandlordEntity> landlords;
}
