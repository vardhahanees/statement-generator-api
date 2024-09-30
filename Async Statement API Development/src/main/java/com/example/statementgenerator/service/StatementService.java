package com.example.statementgenerator.service;

import com.example.statementgenerator.client.CoreBankingSystemClient;
import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class StatementService {

    @Autowired
    private CoreBankingSystemClient coreBankingSystemClient;

    @Autowired
    private StatementRepository statementRepository;

    @Async
    public CompletableFuture<Long> initiateStatementGeneration(StatementEntity request) {
        // Create a new statement instance for processing
        StatementEntity statement = new StatementEntity();
        statement.setId(request.getId()); // Use the ID from the request
        statement.setStatus("PROCESSING");
        statementRepository.save(statement); // Save with PROCESSING status

        try {
            String statementData = coreBankingSystemClient.fetchStatementData(request.getId());
            String pdfUrl = generatePdf(statementData);

            // Update statement with COMPLETED status and PDF URL
            statement.setStatus("COMPLETED");
            statement.setPdfUrl(pdfUrl);
            statementRepository.save(statement); // Save with COMPLETED status

            // Notify the customer
            notifyCustomer(statement.getId(), pdfUrl);
        } catch (Exception e) {
            statement.setStatus("FAILED");
            statementRepository.save(statement); // Save with FAILED status
            e.printStackTrace();
            return CompletableFuture.failedFuture(e); // Return failure
        }

        return CompletableFuture.completedFuture(statement.getId());
    }

    private String generatePdf(String statementData) {
        return "http://localhost:8080/api/v1/statements/download/" + UUID.randomUUID().toString() + ".pdf";
    }

    private void notifyCustomer(Long statementId, String pdfUrl) {
        System.out.println("Notification sent: Your statement is ready. Download at: " + pdfUrl);
    }
}
