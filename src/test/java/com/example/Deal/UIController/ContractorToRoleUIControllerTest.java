package com.example.Deal.UIController;

import com.example.Deal.Controller.ContractorToRoleController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(ContextSetup.class)
public class ContractorToRoleUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContractorToRoleController contractorToRoleController;

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testAddPermission() throws Exception {
        mockMvc.perform(post("/ui/deal/contractor-to-role/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testAddDenial() throws Exception {
        mockMvc.perform(post("/ui/deal/contractor-to-role/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testDeletePermission() throws Exception {
        mockMvc.perform(delete("/ui/deal/contractor-to-role/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testDeleteDenial() throws Exception {
        mockMvc.perform(delete("/ui/deal/contractor-to-role/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

}
