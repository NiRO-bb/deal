package com.example.Deal.UIController;

import com.example.Deal.Controller.DealContractorController;
import com.example.Deal.ContextSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(ContextSetup.class)
public class DealContractorUIControllerTest {

    @MockitoBean
    private DealContractorController dealContractorController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testSavePermission() throws Exception {
        mockMvc.perform(put("/ui/deal/deal-contractor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testSaveDenial() throws Exception {
        mockMvc.perform(put("/ui/deal/deal-contractor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testDeletePermission() throws Exception {
        mockMvc.perform(delete("/ui/deal/deal-contractor/delete/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testDeleteDenial() throws Exception {
        mockMvc.perform(delete("/ui/deal/deal-contractor/delete/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

}
