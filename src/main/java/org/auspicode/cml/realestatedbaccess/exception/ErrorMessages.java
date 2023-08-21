package org.auspicode.cml.realestatedbaccess.exception;

public class ErrorMessages {

    public static final String BAD_REQUEST = "Bad Request";

    public static final String UNIT_ALREADY_IN_DB = "Unit already in database";

    public static final String TENANT_ALREADY_IN_DB = "Tenant already in database";

    public static final String CONTRACT_NOT_IN_DB = "Contract doesn't exist";

    public static final String UNIT_NOT_IN_DB = "Unit doesn't exist";

    public static final String UNIT_CAN_NOT_BE_DELETED = "Unit has occupied Rooms, can not delete";

    public static final String TENANT_NOT_IN_DB = "Tenant doesn't exist";

    public static final String ROOM_NOT_IN_DB = "Room doesn't exist";

    public static final String ALL_ROOMS_ALREADY_IN_DB = "This Unit doesn't allow for more Rooms to be created";

    public static final String ROOM_NOT_AVAILABLE = "This Room is already taken";

    public static final String ROOM_DOES_NOT_MATCH_UNIT = "The specified Room doesn't belong to this Unit";

    public static final String ROOM_CAN_NOT_BE_DELETED = "This Room has an associated contract, it can't be deleted";
}
