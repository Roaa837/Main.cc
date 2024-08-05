package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PatronController.class)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setup() {
        patron1 = new Patron();
        patron1.setId(1L);
        patron1.setName("John Doe");
        patron1.setContactInfo("john.doe@example.com");

        patron2 = new Patron();
        patron2.setId(2L);
        patron2.setName("Jane Smith");
        patron2.setContactInfo("jane.smith@example.com");
    }

    @Test
    public void testGetAllPatrons() throws Exception {
        Mockito.when(patronService.getAllPatrons()).thenReturn(Arrays.asList(patron1, patron2));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    public void testGetPatronById() throws Exception {
        Mockito.when(patronService.getPatronById(1L)).thenReturn(Optional.of(patron1));

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testAddPatron() throws Exception {
        Mockito.when(patronService.addPatron(ArgumentMatchers.any(Patron.class))).thenReturn(patron1);

        mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"contactInfo\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testUpdatePatron() throws Exception {
        Mockito.when(patronService.updatePatron(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Patron.class))).thenReturn(patron1);

        mockMvc.perform(put("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe Updated\",\"contactInfo\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testDeletePatron() throws Exception {
        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isNoContent());
    }
}
