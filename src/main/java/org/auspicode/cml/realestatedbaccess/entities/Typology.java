package org.auspicode.cml.realestatedbaccess.entities;

import lombok.Getter;

@Getter
public enum Typology {

    T0(0),
    T1(1),
    T2(2),
    T3(3);

    private final Integer numberOfRooms;

    Typology(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
}
