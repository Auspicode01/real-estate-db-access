package org.auspicode.cml.realestatedbaccess.controllers;

import org.auspicode.cml.realestatedbaccess.exception.ApiExceptionHandler;
import org.auspicode.cml.realestatedbaccess.models.RoomRequest;
import org.auspicode.cml.realestatedbaccess.models.RoomResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateRoomRequest;
import org.auspicode.cml.realestatedbaccess.services.RoomService;
import org.auspicode.cml.realestatedbaccess.testUtils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {

    MockMvc mockMvc;

    @MockBean
    RoomService roomService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RoomController(roomService)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRetrieveRooms_ReturnOk() throws Exception {
        Mockito.when(roomService.retrieveRooms()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/room/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneRoom_ReturnOk() throws Exception {
        Mockito.when(roomService.findOne(any(Long.class))).thenReturn(RoomResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", String.valueOf(ROOM_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneRoomWithoutRoomId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'roomId' is not present."));
    }

    @Test
    void whenFindRoomByUnitId_ReturnOk() throws Exception {
        Mockito.when(roomService.findByUnitId(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/room/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", UNIT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindRoomByUnitIdWithoutUnitId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/room/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'unitId' is not present."));
    }

    @Test
    void whenCreateRoom_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/rooms/createRoomValidRequest.json");
        Mockito.when(roomService.createRoom(any(RoomRequest.class))).thenReturn(RoomResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateRoomWithoutUnitId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/rooms/createRoomWithoutUnitId.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateRoomWithoutPrice_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/rooms/createRoomWithoutPrice.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenUpdateRoom_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/rooms/updateRoomValidRequest.json");
        Mockito.when(roomService.updateRoom(any(Long.class), any(UpdateRoomRequest.class))).thenReturn(RoomResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.put("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", String.valueOf(ROOM_ID))
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenUpdateRoomWithoutRoomId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/rooms/updateRoomValidRequest.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'roomId' is not present."));
    }

    @Test
    void whenUpdateRoomWithoutBody_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", String.valueOf(ROOM_ID)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(FAILED_TO_READ_REQUEST));
    }

    @Test
    void whenDeleteRoom_ReturnOk() throws Exception {
        Mockito.when(roomService.deleteRoom(any(Long.class))).thenReturn(RoomResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", String.valueOf(ROOM_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteRoomWithoutRoomId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'roomId' is not present."));
    }
}