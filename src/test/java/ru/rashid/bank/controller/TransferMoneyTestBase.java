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
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.model.output.TransferMoneyOutputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;
import ru.rashid.bank.helper.TestAccountHelper;
import ru.rashid.bank.helper.TestCheckBalanceHelper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class TransferMoneyTestBase {
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

    TransferMoneyOutputModel callTransferMoneyPositive(TransferInputModel input) throws Exception {
        return callTransferMoney(input, HttpStatus.OK, TransferMoneyOutputModel.class);
    }

    ErrorDescription callTransferMoneyNegative(TransferInputModel input, HttpStatus status) throws Exception {
        return callTransferMoney(input, status, ErrorDescription.class);
    }

    private <T> T callTransferMoney(TransferInputModel input, HttpStatus status, Class<T> tClass) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/transferMoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(input))
        )
                .andExpect(status().is(status.value()))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return MAPPER.readValue(content, tClass);
    }
}
