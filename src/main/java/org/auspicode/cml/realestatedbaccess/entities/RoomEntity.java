package org.auspicode.cml.realestatedbaccess.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROOMS")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnoreProperties("rooms")
    @ManyToOne(optional = false)
    @JoinColumn(name = "UNIT_ID", referencedColumnName = "ID", nullable = false, updatable = false)
    private UnitEntity unitId;

    @NotNull
    @Column(name = "AVAILABLE", nullable = false)
    private Boolean isAvailable;

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;

    @NotNull
    @Max(2)
    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "IS_SUITE", nullable = false)
    private Boolean isSuite;
}
