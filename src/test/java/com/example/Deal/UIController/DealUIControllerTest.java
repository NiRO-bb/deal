package com.example.Deal.UIController;

import com.example.Deal.AbstractContainer;
import com.example.Deal.Controller.DealController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealUIControllerTest extends AbstractContainer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DealController dealController;

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testSavePermission() throws Exception {
        mockMvc.perform(put("/ui/deal/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testSaveDenial() throws Exception {
        mockMvc.perform(put("/ui/deal/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testChangePermission() throws Exception {
        mockMvc.perform(patch("/ui/deal/change/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testChangeDenial() throws Exception {
        mockMvc.perform(patch("/ui/deal/change/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testGetPermission() throws Exception {
        mockMvc.perform(get("/ui/deal/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetDenial() throws Exception {
        mockMvc.perform(get("/ui/deal/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "CREDIT_USER")
    public void testSearchPermission1() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "type": ["CREDIT"]
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "OVERDRAFT_USER")
    public void testSearchPermission2() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "type": ["OVERDRAFT"]
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testSearchPermission3() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "CREDIT_USER")
    public void testSearchDenial1() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "type": ["OVERDRAFT"]
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "OVERDRAFT_USER")
    public void testSearchDenial2() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "type": ["CREDIT"]
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testSearchDenial3() throws Exception {
        mockMvc.perform(post("/ui/deal/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DEAL_SUPERUSER")
    public void testExportPermission() throws Exception {
        mockMvc.perform(post("/ui/deal/search/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testExportDenial() throws Exception {
        mockMvc.perform(post("/ui/deal/search/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

}
