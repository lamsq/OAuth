package com.example.oauth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicRoutes() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/error")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminRoute_AsAdmin() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAdminRoute_AsUser() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testAuthenticatedRoutes() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isOk());
    }

    @Test
    void testAuthenticatedRoutes_Unauthenticated() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().is3xxRedirection());
    }
}
