package org.auspicode.cml.realestatedbaccess.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UNITS")
public class UnitEntity {

    @NotBlank
    @Id
    @Size(max = 80)
    @Column(name = "ID", nullable = false, length = 80, updatable = false)
    private String id;

    @NotBlank
    @Size(max = 80)
    @Column(name = "STREET", nullable = false, length = 80)
    private String street;

    @NotBlank
    @Size(min = 8, max = 8)
    @Column(name = "POSTAL_CODE", nullable = false, length = 8, updatable = false)
    private String postalCode;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "ARTICLE", nullable = false, length = 4, updatable = false)
    private String article;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "REGISTER_NUMBER", nullable = false, length = 4, updatable = false)
    private String registerNumber;

    @NotBlank
    @Size(max = 50)
    @Column(name = "TOWN", nullable = false, length = 50, updatable = false)
    private String town;

    @Size(max = 6)
    @Column(name = "FRACTION", length = 6)
    private String fraction;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPOLOGY", nullable = false)
    private Typology typology;

    @JsonIgnoreProperties("unitId")
    @OneToMany(mappedBy = "unitId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RoomEntity> rooms;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANDLORD_NIF", referencedColumnName = "NIF", nullable = false, updatable = false)
    @JoinColumn(name = "LANDLORD_ID_CARD_NUMBER", referencedColumnName = "ID_CARD_NUMBER", nullable = false, updatable = false)
    @JoinColumn(name = "LANDLORD_FULL_NAME", referencedColumnName = "FULL_NAME", nullable = false, updatable = false)
    @JsonIgnoreProperties({"contracts", "units"})
    private LandlordEntity landlordEntity;
}
