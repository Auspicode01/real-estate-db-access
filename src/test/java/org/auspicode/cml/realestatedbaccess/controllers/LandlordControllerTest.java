package org.auspicode.cml.realestatedbaccess.controllers;

import org.auspicode.cml.realestatedbaccess.exception.ApiExceptionHandler;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.services.LandlordService;
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
@WebMvcTest(controllers = LandlordController.class)
class LandlordControllerTest {

    MockMvc mockMvc;

    @MockBean
    LandlordService landlordService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LandlordController(landlordService)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRetrieveLandlords_ReturnOk() throws Exception {
        Mockito.when(landlordService.retrieveLandlords()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetLandlord_ReturnOk() throws Exception {
        Mockito.when(landlordService.findOne(any(String.class), any(String.class), any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .param("landlordIdCardNumber", "39284756")
                        .param("landlordFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetLandlordWithoutLandlordNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null)
                        .param("landlordIdCardNumber", "39284756")
                        .param("landlordFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenGetLandlordWithoutLandlordIdCardNumber_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .param("landlordIdCardNumber", (String) null)
                        .param("landlordFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordIdCardNumber' is not present."));
    }

    @Test
    void whenGetLandlordWithoutLandlordFullName_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .param("landlordIdCardNumber", "39284756")
                        .param("landlordFullName", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordFullName' is not present."));
    }

    @Test
    void whenGetLandlordByNif_ReturnOk() throws Exception {
        Mockito.when(landlordService.findByNif(any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord/nif")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetLandlordByNifWithoutLandlordNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/landlord/nif")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenCreateLandlord_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordValidRequest.json");
        Mockito.when(landlordService.createLandlord(any(CreateUserRequest.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateLandlordWithoutNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordWithoutNif.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateWithoutIdCardNumber_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordWithoutIdCardNumber.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateWithoutFullName_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordWithoutFullName.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateWithoutBirthDate_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordWithoutBirthDate.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateWithoutNib_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/landlords/createLandlordWithoutNib.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateLandlordContact_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        Mockito.when(landlordService.createContact(any(String.class), any(Contact.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateLandlordContactWithoutLandlordNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenCreateLandlordContactWithoutContactType_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactType.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateLandlordContactWithoutContactValue_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactValue.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenUpdateLandlord_ReturnOk() throws Exception {
        Mockito.when(landlordService.updateLandlord(any(String.class), any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.patch("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .param("landlordNib", "PT50002200003426584958601"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenUpdateLandlordWithoutNib_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .param("landlordNib", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNib' is not present."));
    }

    @Test
    void whenUpdateLandlordWithoutNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null)
                        .param("landlordNib", "PT50002200003426584958601"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenDeleteLandlord_ReturnOk() throws Exception {
        Mockito.when(landlordService.deleteLandlord(any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteLandlord_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenDeleteLandlordContact_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        Mockito.when(landlordService.deleteContact(any(String.class), any(Contact.class))).thenReturn(Boolean.TRUE);
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteLandlordContactWithoutLandlordNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenDeleteLandlordContactWithoutContactType_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactType.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenDeleteLandlordContactWithoutContactValue_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactValue.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/landlord/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }
}