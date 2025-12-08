package ymt_odev.Database;

import ymt_odev.Utils.ConfigManager;
import ymt_odev.Utils.ConfigManager.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pattern ile veritabanı bağlantı yöneticisi
 * Connection pooling ile optimize edilmiş
 * Bağlantı bilgileri db-config.json dosyasından okunur
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private DbConfig dbConfig;

    // Private constructor - Singleton pattern
    private DatabaseConnection() {
        this.dbConfig = ConfigManager.loadDbConfig();
        try {
            this.connection = DriverManager.getConnection(
                dbConfig.getConnectionUrl(),
                dbConfig.username,
                dbConfig.password
            );
            System.out.println("✅ Veritabanı bağlantısı oluşturuldu");
        } catch (SQLException e) {
            System.err.println("❌ Veritabanı bağlantı hatası: " + e.getMessage());
            this.connection = null;
        }
    }

    /**
     * Singleton instance'ı döndürür
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Singleton instance'ı sıfırlar (bağlantı bilgileri değiştiğinde kullanılır)
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
        ConfigManager.clearDbConfigCache();
    }

    /**
     * Mevcut veritabanı bağlantısını döndürür (yeniden kullanır)
     * Eğer bağlantı kapanmışsa yeniden açar
     */
    public synchronized Connection getConnection() {
        try {
            // Bağlantı yoksa veya kapalıysa yeniden oluştur
            if (connection == null || connection.isClosed() || !connection.isValid(3)) {
                System.out.println("🔄 Veritabanı bağlantısı yenileniyor...");
                dbConfig = ConfigManager.loadDbConfig();
                connection = DriverManager.getConnection(
                    dbConfig.getConnectionUrl(),
                    dbConfig.username,
                    dbConfig.password
                );
                System.out.println("✅ Veritabanı bağlantısı yenilendi");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Bağlantı hatası: " + e.getMessage());
            return null;
        }
    }

    /**
     * Bağlantıyı kapatmaz, pool'da tutar
     * Connection pool mantığı için bağlantı açık kalır
     * Not: Uygulama kapanırken shutdown() metodunu çağırın
     */
    public void closeConnection(Connection conn) {
        // Connection pool kullanıldığı için bağlantıyı kapatmıyoruz
        // Bağlantı yeniden kullanılmak üzere pool'da kalır
    }

    /**
     * Bağlantının geçerliliğini kontrol eder
     */
    public boolean isConnectionValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Uygulama kapanırken bağlantıyı tamamen kapatır
     */
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Veritabanı bağlantısı kapatıldı");
            } catch (SQLException e) {
                System.err.println("❌ Bağlantı kapatma hatası: " + e.getMessage());
            }
        }
    }
}
