package org.auspicode.cml.realestatedbaccess.controllers;

import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.ApiExceptionHandler;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.services.UnitService;
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
@WebMvcTest(controllers = UnitController.class)
class UnitControllerTest {

    MockMvc mockMvc;

    @MockBean
    UnitService unitService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UnitController(unitService)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRetrieveUnits_ReturnOk() throws Exception {
        Mockito.when(unitService.retrieveUnits()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/unit/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneUnit_ReturnOk() throws Exception {
        Mockito.when(unitService.findOne(any(String.class))).thenReturn(UnitResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", UNIT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneUnitWithoutUnitId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'unitId' is not present."));
    }

    @Test
    void whenCreateUnit_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitValidRequest.json");
        Mockito.when(unitService.createUnit(any(String.class), any(CreateUnitRequest.class))).thenReturn(UnitResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateUnitWithoutId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutId.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutStreet_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutStreet.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutPostalCode_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutPostalCode.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutArticle_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutArticle.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutRegisterNumber_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutRegisterNumber.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutTown_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutTown.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateUnitWithoutTypology_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/createUnitWithoutTypology.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenUpdateUnit_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/updateUnitValidRequest.json");
        Mockito.when(unitService.updateUnit(any(String.class), any(UpdateUnitRequest.class))).thenReturn(UnitResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.put("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", UNIT_ID)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenUpdateUnitWithoutUnitId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/units/updateUnitValidRequest.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'unitId' is not present."));
    }

    @Test
    void whenUpdateUnitWithoutBody_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", UNIT_ID))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(FAILED_TO_READ_REQUEST));
    }

    @Test
    void whenDeleteUnit_ReturnOk() throws Exception {
        Mockito.when(unitService.deleteUnit(any(String.class))).thenReturn(UnitResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", UNIT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteUnitWithoutUnitId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'unitId' is not present."));
    }
}