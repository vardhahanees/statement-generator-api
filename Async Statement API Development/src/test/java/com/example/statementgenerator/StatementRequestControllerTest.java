package com.example.statementgenerator.controller;

import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.service.StatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StatementRequestControllerTest {

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementRequestController statementRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestStatement_Success() throws Exception {
        StatementEntity request = new StatementEntity();
        request.setId(1L); // Mocking ID

        when(statementService.initiateStatementGeneration(any(StatementEntity.class)))
                .thenReturn(CompletableFuture.completedFuture(1L)); // Mock successful response

        ResponseEntity<String> response = statementRequestController.requestStatement(request).get();

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Request accepted with ID: 1", response.getBody());
    }

    @Test
    void testRequestStatement_Failure() throws Exception {
        StatementEntity request = new StatementEntity();
        request.setId(1L); // Mocking ID

        when(statementService.initiateStatementGeneration(any(StatementEntity.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Error occurred"))); // Mock failure

        ResponseEntity<String> response = statementRequestController.requestStatement(request).get();

        assertEquals(500, response.getStatusCodeValue());
    }
}
