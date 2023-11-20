package org.auspicode.cml.realestatedbaccess.exception;

import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
@Generated
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntryAlreadyInDbException.class})
    protected ResponseEntity<Object> handleEntryInDbException(EntryAlreadyInDbException ex) {
        HttpStatus status = HttpStatus.IM_USED;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {SQLException.class})
    protected ResponseEntity<Object> handleSQLServerException(SQLException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {AllRoomsCreatedForUnitException.class})
    protected ResponseEntity<Object> handleAllRoomsCreatedForUnitException(AllRoomsCreatedForUnitException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {NoSuchRoomException.class})
    protected ResponseEntity<Object> handleRoomNotAvailableException(NoSuchRoomException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {RoomIsOccupiedException.class})
    protected ResponseEntity<Object> handleRoomIsOccupiedException(RoomIsOccupiedException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<Object> handleBadRequestException(HttpClientErrorException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }

    @ExceptionHandler(value = {NoSuchMethodException.class})
    protected ResponseEntity<Object> handleNoSuchMethodException(NoSuchMethodException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new CustomException(ex.getMessage(), status, ex.getLocalizedMessage()), status);
    }
}
