package ymt_odev.Controllers;

import javafx.fxml.FXML;
import ymt_odev.Database.DatabaseConnection;

import java.sql.Connection;

/**
 * Provides shared lifecycle hooks and utilities for all controllers.
 */
public abstract class BaseController {

    private static boolean connectionVerified;

    @FXML
    protected void initialize() {
        if (connectionVerified) {
            return;
        }

        try {
            DatabaseConnection dbManager = DatabaseConnection.getInstance();
            Connection connection = dbManager.getConnection();

            if (connection != null && dbManager.isConnectionValid(connection)) {
                System.out.println("✅ Veritabanı bağlantısı başarılı!");
            } else {
                System.err.println("⚠️ Veritabanı bağlantısı başarısız!");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Veritabanı bağlantı hatası: " + e.getMessage());
        }

        connectionVerified = true;
    }
}

