package com.fsd.exp5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsd.exp5.dto.PostRequest;
import com.fsd.exp5.filter.LoggingFilter;
import com.fsd.exp5.interceptor.CorrelationInterceptor;
import com.fsd.exp5.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PostControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LoggingFilter loggingFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(loggingFilter)
                .build();
    }

    @Test
    @DisplayName("POST /api/posts - Success creates post and returns 201 with correlation ID")
    void testCreatePost_Success() throws Exception {
        PostRequest request = new PostRequest("Spring Boot in Action", "A great guide to building microservices.");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(CorrelationInterceptor.CORRELATION_ID_HEADER))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Post created successfully")))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.content", is("Spring Boot in Action")))
                .andExpect(jsonPath("$.data.text", is("A great guide to building microservices.")))
                .andExpect(jsonPath("$.data.scheduled", is(false)));
    }

    @Test
    @DisplayName("POST /api/posts - Validation failure on blank content returns 400 Bad Request")
    void testCreatePost_ValidationFailure_BlankContent() throws Exception {
        PostRequest request = new PostRequest("", "Some valid text description.");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(CorrelationInterceptor.CORRELATION_ID_HEADER))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.data.content", is("Content must not be empty")));
    }

    @Test
    @DisplayName("POST /api/posts - Validation failure on text exceeding 280 characters returns 400")
    void testCreatePost_ValidationFailure_TextTooLong() throws Exception {
        String longText = "A".repeat(281);
        PostRequest request = new PostRequest("Valid Title", longText);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.data.text", is("Content exceeds limit")));
    }

    @Test
    @DisplayName("GET /api/posts/{id} - Missing post returns 404 Not Found")
    void testGetPostById_NotFound() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(CorrelationInterceptor.CORRELATION_ID_HEADER))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Post not found with id: 999")));
    }

    @Test
    @DisplayName("GET /api/posts - Retrieves all posts successfully")
    void testGetAllPosts_Success() throws Exception {
        PostRequest post1 = new PostRequest("First Post", "Details 1");
        PostRequest post2 = new PostRequest("Second Post", "Details 2");

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post1)));

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post2)));

        mockMvc.perform(get("/api/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @DisplayName("PUT /api/posts/{id} - Updates existing post successfully")
    void testUpdatePost_Success() throws Exception {
        PostRequest initial = new PostRequest("Original Title", "Original Text");

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andReturn().getResponse().getContentAsString();

        Number id = com.jayway.jsonpath.JsonPath.read(response, "$.data.id");

        PostRequest updateReq = new PostRequest("Updated Title", "Updated Text");
        mockMvc.perform(put("/api/posts/{id}", id.longValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.content", is("Updated Title")))
                .andExpect(jsonPath("$.data.text", is("Updated Text")));
    }

    @Test
    @DisplayName("DELETE /api/posts/{id} - Deletes existing post successfully")
    void testDeletePost_Success() throws Exception {
        PostRequest initial = new PostRequest("To be deleted", "Some text");

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andReturn().getResponse().getContentAsString();

        Number id = com.jayway.jsonpath.JsonPath.read(response, "$.data.id");

        mockMvc.perform(delete("/api/posts/{id}", id.longValue()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Post deleted successfully")));

        // Verify it is gone
        mockMvc.perform(get("/api/posts/{id}", id.longValue()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/posts/{id}/schedule - Schedules post with future timestamp")
    void testSchedulePost_Success() throws Exception {
        PostRequest initial = new PostRequest("Schedule Me", "Upcoming event announcement");

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andReturn().getResponse().getContentAsString();

        Number id = com.jayway.jsonpath.JsonPath.read(response, "$.data.id");

        LocalDateTime futureTime = LocalDateTime.now().plusDays(5).withNano(0);
        String formattedFuture = futureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        String scheduleJson = String.format("{\"scheduledAt\":\"%s\"}", formattedFuture);

        mockMvc.perform(post("/api/posts/{id}/schedule", id.longValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.scheduled", is(true)))
                .andExpect(jsonPath("$.data.scheduledAt", is(formattedFuture)));
    }

    @Test
    @DisplayName("POST /api/posts/{id}/schedule - Validation error when scheduledAt is in the past")
    void testSchedulePost_PastDate_ValidationFailure() throws Exception {
        PostRequest initial = new PostRequest("Past Test", "Text");

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andReturn().getResponse().getContentAsString();

        Number id = com.jayway.jsonpath.JsonPath.read(response, "$.data.id");

        LocalDateTime pastTime = LocalDateTime.now().minusDays(1).withNano(0);
        String formattedPast = pastTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        String scheduleJson = String.format("{\"scheduledAt\":\"%s\"}", formattedPast);

        mockMvc.perform(post("/api/posts/{id}/schedule", id.longValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.data.scheduledAt", is("Scheduled time must be in the future")));
    }
}
