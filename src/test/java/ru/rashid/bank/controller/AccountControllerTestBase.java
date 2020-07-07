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
import ru.rashid.bank.data.model.output.AccountOutputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;
import ru.rashid.bank.helper.TestAccountHelper;
import ru.rashid.bank.helper.TestCheckBalanceHelper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class AccountControllerTestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

    AccountOutputModel callCreateUserPositive(AccountInputModel input) throws Exception {
        return callCreateUser(input, HttpStatus.OK, AccountOutputModel.class);
    }

    ErrorDescription callCreateUserNegative(AccountInputModel input, HttpStatus status) throws Exception {
        return callCreateUser(input, status, ErrorDescription.class);
    }

    private <T> T callCreateUser(AccountInputModel input, HttpStatus status, Class<T> tClass) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(input))
        )
                .andExpect(status().is(status.value()))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return MAPPER.readValue(content, tClass);
    }
}
