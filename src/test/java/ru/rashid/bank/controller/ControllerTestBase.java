package ru.rashid.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.model.output.AccountOutputModel;
import ru.rashid.bank.data.model.output.TransferOutputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;
import ru.rashid.bank.helper.TestAccountHelper;
import ru.rashid.bank.helper.TestCheckBalanceHelper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class ControllerTestBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ACCOUNT_PATH = "account";
    private static final String TRANSFER_PATH = "transferMoney";

    @Autowired
    TestAccountHelper testAccountHelper;
    @Autowired
    TestCheckBalanceHelper checkBalanceHelper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    AccountOutputModel callCreateUserPositive(AccountInputModel input) {
        return callPost(input, HttpStatus.OK, AccountOutputModel.class, ACCOUNT_PATH);
    }

    ErrorDescription callCreateUserNegative(AccountInputModel input, HttpStatus status) {
        return callPost(input, status, ErrorDescription.class, ACCOUNT_PATH);
    }

    TransferOutputModel callTransferMoneyPositive(TransferInputModel input) {
        return callPost(input, HttpStatus.OK, TransferOutputModel.class, TRANSFER_PATH);
    }

    ErrorDescription callTransferMoneyNegative(TransferInputModel input, HttpStatus status) {
        return callPost(input, status, ErrorDescription.class, TRANSFER_PATH);
    }

    AccountOutputModel callGetAccount(Long id) {
        return callGet(id, AccountOutputModel.class, ACCOUNT_PATH);
    }

    /**
     * Обертка, чтобы не таскать за собой Exception
     */
    private <I, O> O callPost(I input, HttpStatus status, Class<O> tClass, String path) {
        try {
            return callPostWithException(input, status, tClass, path);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обертка, чтобы не таскать за собой Exception
     */
    private <O> O callGet(Long id, Class<O> tClass, String path) {
        try {
            return callGetWithException(id, tClass, path);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <I, O> O callPostWithException(I input, HttpStatus status, Class<O> tClass, String path) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/" + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(input))
        )
                .andExpect(status().is(status.value()))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return MAPPER.readValue(content, tClass);
    }

    private <O> O callGetWithException(Long id, Class<O> tClass, String path) throws Exception {
        MvcResult result = mockMvc.perform(get("/" + path + "/{id}", id))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return MAPPER.readValue(content, tClass);
    }
}
