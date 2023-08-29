package org.auspicode.cml.realestatedbaccess.controllers;

import org.auspicode.cml.realestatedbaccess.exception.ApiExceptionHandler;
import org.auspicode.cml.realestatedbaccess.models.ContractResponse;
import org.auspicode.cml.realestatedbaccess.models.CreateContractRequest;
import org.auspicode.cml.realestatedbaccess.services.ContractService;
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

import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.BAD_REQUEST;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.INVALID_REQUEST_CONTENT;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ContractController.class)
class ContractControllerTest {

    MockMvc mockMvc;

    @MockBean
    ContractService contractService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ContractController(contractService)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void whenRetrieveContracts_ReturnOk() throws Exception {
        Mockito.when(contractService.retrieveContracts()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneContract_ReturnOk() throws Exception {
        Mockito.when(contractService.findOne(any(Long.class))).thenReturn(ContractResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("contractId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindOneContractWithoutId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("contractId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'contractId' is not present."));
    }

    @Test
    void whenFindByUnitId_ReturnContract() throws Exception {
        Mockito.when(contractService.findByUnitId(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", "leirinhas"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindByUnitIdWithoutUnitId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'unitId' is not present."));
    }

    @Test
    void whenFindByTenantNif_ReturnContract() throws Exception {
        Mockito.when(contractService.findByTenantNif(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", "123123123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindByTenantNifWithoutTenantNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'tenantNif' is not present."));
    }

    @Test
    void whenFindByLandlordNif_ReturnContract() throws Exception {
        Mockito.when(contractService.findByLandlordNif(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", "123123123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindByLandlordNifWithoutLandlordNif_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/landlord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("landlordNif", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'landlordNif' is not present."));
    }

    @Test
    void whenFindByRoomId_ReturnContract() throws Exception {
        Mockito.when(contractService.findByRoomId(any(Long.class))).thenReturn(ContractResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenFindByRoomIdWithoutRoomId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("roomId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'roomId' is not present."));
    }

    @Test
    void whenCreateContract_ReturnOk() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractValidRequest.json");
        Mockito.when(contractService.createContract(any(CreateContractRequest.class))).thenReturn(ContractResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenCreateContractWithoutStartDate_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutStartDate.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutEndDate_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutEndDate.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutLandlordsNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutLandlordsNif.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutTenantsNif_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutTenantsNif.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutUnitId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutUnitId.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutRoomId_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutRoomId.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenCreateContractWithoutType_ReturnBadRequestError() throws Exception {
        String requestBody = TestUtils.readJsonStringFromResourceFile("/json/contracts/createContractWithoutType.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value(INVALID_REQUEST_CONTENT));
    }

    @Test
    void whenDeleteContract_ReturnOk() throws Exception {
        Mockito.when(contractService.deleteContract(any(Long.class))).thenReturn(ContractResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("contractId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void whenDeleteContractWithoutId_ReturnBadRequestError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("contractId", (String) null))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.detail").value("Required parameter 'contractId' is not present."));
    }
}