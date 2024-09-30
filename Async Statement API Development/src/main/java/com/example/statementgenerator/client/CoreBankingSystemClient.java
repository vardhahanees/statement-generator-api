package com.example.statementgenerator.client;

import org.springframework.stereotype.Component;

@Component
public class CoreBankingSystemClient {

    public String fetchStatementData(Long accountId) throws InterruptedException {
        // Simulate a delay in getting data
        Thread.sleep(5000);

        // Simulate a failure for a specific account ID
        if (accountId == -1L) { // Use a specific long value to simulate failure
            throw new RuntimeException("Core banking service unavailable");
        }

        // Return a simplified JSON response for success
        return "{ \"accountId\": \"" + accountId + "\", \"transactions\": [...] }"; // Use accountId directly
    }
}
