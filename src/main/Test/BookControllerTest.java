package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setup() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book Title 1");
        book1.setAuthor("Author 1");
        book1.setPublicationYear(2020);
        book1.setIsbn("1234567890");

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Title 2");
        book2.setAuthor("Author 2");
        book2.setPublicationYear(2021);
        book2.setIsbn("0987654321");
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Book Title 1")))
                .andExpect(jsonPath("$[1].title", is("Book Title 2")));
    }

    @Test
    public void testGetBookById() throws Exception {
        Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Book Title 1")));
    }

    @Test
    public void testAddBook() throws Exception {
        Mockito.when(bookService.addBook(ArgumentMatchers.any(Book.class))).thenReturn(book1);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Book Title 1\",\"author\":\"Author 1\",\"publicationYear\":2020,\"isbn\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Book Title 1")));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Mockito.when(bookService.updateBook(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Book.class))).thenReturn(book1);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Book Title\",\"author\":\"Updated Author\",\"publicationYear\":2020,\"isbn\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Book Title 1")));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
