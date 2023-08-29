package org.auspicode.cml.realestatedbaccess.controllers;

import org.auspicode.cml.realestatedbaccess.exception.ApiExceptionHandler;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.services.TenantService;
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
@WebMvcTest(controllers = TenantController.class)
class TenantControllerTest {

    MockMvc mockMvc;

    @MockBean
    TenantService tenantService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TenantController(tenantService)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRetrieveTenants_ReturnOk() throws Exception {
        Mockito.when(tenantService.retrieveTenants()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetTenant_ReturnOk() throws Exception {
        Mockito.when(tenantService.findOne(any(String.class), any(String.class), any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .param("tenantIdCardNumber", "39284756")
                        .param("tenantFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetTenantWithoutTenantNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null)
                        .param("tenantIdCardNumber", "39284756")
                        .param("tenantFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenGetTenantWithoutTenantIdCardNumber_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .param("tenantIdCardNumber", (String) null)
                        .param("tenantFullName", "John Doe"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantIdCardNumber' is not present."));
    }

    @Test
    void whenGetTenantWithoutTenantFullName_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .param("tenantIdCardNumber", "39284756")
                        .param("tenantFullName", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantFullName' is not present."));
    }

    @Test
    void whenGetTenantByNif_ReturnOk() throws Exception {
        Mockito.when(tenantService.findByNif(any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant/nif")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenGetTenantByNifWithoutTenantNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant/nif")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenCreateTenant_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserValidRequest.json");
        Mockito.when(tenantService.createTenant(any(CreateUserRequest.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateTenantWithoutNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserWithoutNif.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantWithoutIdCardNumber_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserWithoutIdCardNumber.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantWithoutFullName_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserWithoutFullName.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantWithoutBirthDate_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserWithoutBirthDate.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantWithoutNib_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/users/createUserWithoutNib.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantContact_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        Mockito.when(tenantService.createContact(any(String.class), any(Contact.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateTenantContactWithoutTenantNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenCreateTenantContactWithoutContactType_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactType.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateTenantContactWithoutContactValue_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactValue.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenUpdateTenant_ReturnOk() throws Exception {
        Mockito.when(tenantService.updateTenant(any(String.class), any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.patch("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .param("tenantNib", "PT50002200003426584958601"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenUpdateTenantWithoutNib_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .param("tenantNib", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNib' is not present."));
    }

    @Test
    void whenUpdateTenantWithoutNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null)
                        .param("tenantNib", "PT50002200003426584958601"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenDeleteTenant_ReturnOk() throws Exception {
        Mockito.when(tenantService.deleteTenant(any(String.class))).thenReturn(UserResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteTenantWithoutTenantNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenDeleteTenantContact_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        Mockito.when(tenantService.deleteContact(any(String.class), any(Contact.class))).thenReturn(Boolean.TRUE);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteTenantContactWithoutTenantNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestValid.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenDeleteTenantContactWithoutContactType_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactType.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenDeleteTenantContactWithoutContactValue_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contacts/ContactRequestWithoutContactValue.json");
        mockMvc.perform(MockMvcRequestBuilders.delete("/tenant/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", USER_NIF)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }
}