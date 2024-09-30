package com.example.statementgenerator.service;

import com.example.statementgenerator.client.CoreBankingSystemClient;
import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StatementServiceTest {

    @Mock
    private CoreBankingSystemClient coreBankingSystemClient;

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private StatementService statementService;

    private StatementEntity statementEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        statementEntity = new StatementEntity();
        statementEntity.setId(1L);  // Assuming a Long ID
        statementEntity.setStatus("NEW");
    }

    @Test
    public void testInitiateStatementGeneration_Success() throws InterruptedException {
        // Arrange
        when(statementRepository.save(any(StatementEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(coreBankingSystemClient.fetchStatementData(1L)).thenReturn("{ \"accountId\": \"1\", \"transactions\": [...] }");

        // Act
        CompletableFuture<Long> result = statementService.initiateStatementGeneration(statementEntity);

        // Assert
        assertEquals(1L, result.join());

        // Verify status changes
        ArgumentCaptor<StatementEntity> captor = ArgumentCaptor.forClass(StatementEntity.class);
        verify(statementRepository, times(2)).save(captor.capture());
        List<StatementEntity> savedEntities = captor.getAllValues();

        // Assert that the saved entity's statuses are as expected
        assertEquals("COMPLETED", savedEntities.get(1).getStatus());

        verify(coreBankingSystemClient, times(1)).fetchStatementData(1L);
    }

    @Test
    public void testInitiateStatementGeneration_Failure() throws InterruptedException {
        // Arrange
        when(statementRepository.save(any(StatementEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(coreBankingSystemClient.fetchStatementData(1L)).thenThrow(new RuntimeException("Core banking service unavailable"));

        // Act
        CompletableFuture<Long> result = statementService.initiateStatementGeneration(statementEntity);

        // Assert
        assertThrows(RuntimeException.class, result::join);

        // Verify status changes
        ArgumentCaptor<StatementEntity> captor = ArgumentCaptor.forClass(StatementEntity.class);
        verify(statementRepository, times(2)).save(captor.capture());
        List<StatementEntity> savedEntities = captor.getAllValues();

        // Assert that the saved entity's statuses are as expected
        assertEquals("FAILED", savedEntities.get(1).getStatus());

        verify(coreBankingSystemClient, times(1)).fetchStatementData(1L);
    }
}
