package com.example.oauth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void testUserPage() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(view().name("user"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminPage() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk()).andExpect(view().name("admin"));
    }
}
