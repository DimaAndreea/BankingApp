package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// handles csv logging for executed actions
public class AuditService {

    private static final String AUDIT_FILE = "audit.csv";
    private static AuditService instance;

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // writes an audit entry with timestamp
    public void logAction(String action) {
        try (FileWriter writer = new FileWriter(AUDIT_FILE, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.append(action).append(", ").append(timestamp).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to audit file.", e);
        }
    }
}
