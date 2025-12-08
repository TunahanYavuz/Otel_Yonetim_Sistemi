package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ymt_odev.AlertManager;
import ymt_odev.Database.DatabaseConnection;
import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DBDataUpdater;
import ymt_odev.Domain.Room;
import ymt_odev.Services.RoomService;
import ymt_odev.Utils.ConfigManager;
import ymt_odev.Utils.ConfigManager.DbConfig;
import ymt_odev.Utils.ConfigManager.PricingConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SettingsController extends BaseController {

    // Genel Ayarlar
    @FXML private TextField hotelNameField;
    @FXML private TextField hotelAddressField;
    @FXML private TextField hotelPhoneField;
    @FXML private CheckBox emailNotificationCheck;
    @FXML private CheckBox smsNotificationCheck;

    // Veritabanı Ayarları
    @FXML private TextField dbServerField;
    @FXML private TextField dbPortField;
    @FXML private TextField dbNameField;
    @FXML private TextField dbUsernameField;
    @FXML private PasswordField dbPasswordField;

    // Fiyatlandırma Ayarları - Oda Tipleri
    @FXML private TextField priceStandart;
    @FXML private TextField priceDeluxe;
    @FXML private TextField priceSuite;
    @FXML private TextField priceAile;
    @FXML private TextField pricePenthouse;

    // Fiyatlandırma Ayarları - Özellikler
    @FXML private TextField priceSeaView;
    @FXML private TextField priceBalcony;
    @FXML private TextField priceJacuzzi;
    @FXML private TextField priceKitchen;

    @Override
    protected void initialize() {
        super.initialize();
        loadSettings();
        loadDbConfig();
        loadPricingConfig();
    }

    private void loadSettings() {
        // Otel ayarlarını yükle (varsayılan değerler)
        if (hotelNameField != null) {
            hotelNameField.setText("Otel Yönetim Sistemi");
        }
        if (hotelAddressField != null) {
            hotelAddressField.setText("İstanbul, Türkiye");
        }
        if (hotelPhoneField != null) {
            hotelPhoneField.setText("+90 (212) 555-0000");
        }
    }

    /**
     * Veritabanı konfigürasyonunu JSON'dan yükler
     */
    private void loadDbConfig() {
        DbConfig config = ConfigManager.loadDbConfig();

        if (dbServerField != null) dbServerField.setText(config.server);
        if (dbPortField != null) dbPortField.setText(config.port);
        if (dbNameField != null) dbNameField.setText(config.databaseName);
        if (dbUsernameField != null) dbUsernameField.setText(config.username);
        if (dbPasswordField != null) dbPasswordField.setText(config.password);
    }

    /**
     * Fiyatlandırma konfigürasyonunu JSON'dan yükler
     */
    private void loadPricingConfig() {
        PricingConfig config = ConfigManager.loadPricingConfig();

        // Oda tipi fiyatları
        if (priceStandart != null) priceStandart.setText(String.valueOf(config.roomTypePrices.getOrDefault("Standart", 500.0)));
        if (priceDeluxe != null) priceDeluxe.setText(String.valueOf(config.roomTypePrices.getOrDefault("Deluxe", 800.0)));
        if (priceSuite != null) priceSuite.setText(String.valueOf(config.roomTypePrices.getOrDefault("Suite", 1200.0)));
        if (priceAile != null) priceAile.setText(String.valueOf(config.roomTypePrices.getOrDefault("Aile", 1000.0)));
        if (pricePenthouse != null) pricePenthouse.setText(String.valueOf(config.roomTypePrices.getOrDefault("Penthouse", 2500.0)));

        // Özellik fiyatları
        if (priceSeaView != null) priceSeaView.setText(String.valueOf(config.featurePrices.getOrDefault("seaView", 150.0)));
        if (priceBalcony != null) priceBalcony.setText(String.valueOf(config.featurePrices.getOrDefault("balcony", 100.0)));
        if (priceJacuzzi != null) priceJacuzzi.setText(String.valueOf(config.featurePrices.getOrDefault("jacuzzi", 200.0)));
        if (priceKitchen != null) priceKitchen.setText(String.valueOf(config.featurePrices.getOrDefault("kitchen", 120.0)));
    }

    @FXML
    private void saveChanges() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Ayarlar başarıyla kaydedildi!", "Başarılı", "");
    }

    @FXML
    private void revertDefaults() {
        loadSettings();
        loadDbConfig();
        loadPricingConfig();
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Varsayılan ayarlar yüklendi!", "Bilgi", "");
    }

    /**
     * Veritabanı konfigürasyonunu JSON dosyasına kaydeder
     */
    @FXML
    private void saveDbConfig() {
        DbConfig config = new DbConfig();

        if (dbServerField != null) config.server = dbServerField.getText();
        if (dbPortField != null) config.port = dbPortField.getText();
        if (dbNameField != null) config.databaseName = dbNameField.getText();
        if (dbUsernameField != null) config.username = dbUsernameField.getText();
        if (dbPasswordField != null) config.password = dbPasswordField.getText();

        boolean success = ConfigManager.saveDbConfig(config);

        if (success) {
            // Bağlantıyı sıfırla
            DatabaseConnection.resetInstance();

            AlertManager.Alert(Alert.AlertType.INFORMATION,
                    "Veritabanı ayarları kaydedildi!\n\nYeni ayarların tam olarak uygulanması için uygulamayı yeniden başlatmanız önerilir.",
                    "Başarılı", "");
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Veritabanı ayarları kaydedilemedi!", "Hata", "");
        }
    }

    /**
     * Fiyatlandırma konfigürasyonunu JSON dosyasına kaydeder
     */
    @FXML
    private void savePricingConfig() {
        try {
            PricingConfig config = new PricingConfig();

            // Oda tipi fiyatları
            if (priceStandart != null) config.roomTypePrices.put("Standart", Double.parseDouble(priceStandart.getText()));
            if (priceDeluxe != null) config.roomTypePrices.put("Deluxe", Double.parseDouble(priceDeluxe.getText()));
            if (priceSuite != null) config.roomTypePrices.put("Suite", Double.parseDouble(priceSuite.getText()));
            if (priceAile != null) config.roomTypePrices.put("Aile", Double.parseDouble(priceAile.getText()));
            if (pricePenthouse != null) config.roomTypePrices.put("Penthouse", Double.parseDouble(pricePenthouse.getText()));

            // Özellik fiyatları
            if (priceSeaView != null) config.featurePrices.put("seaView", Double.parseDouble(priceSeaView.getText()));
            if (priceBalcony != null) config.featurePrices.put("balcony", Double.parseDouble(priceBalcony.getText()));
            if (priceJacuzzi != null) config.featurePrices.put("jacuzzi", Double.parseDouble(priceJacuzzi.getText()));
            if (priceKitchen != null) config.featurePrices.put("kitchen", Double.parseDouble(priceKitchen.getText()));

            boolean success = ConfigManager.savePricingConfig(config);

            if (success) {
                AlertManager.Alert(Alert.AlertType.INFORMATION,
                        "Fiyatlandırma ayarları kaydedildi!", "Başarılı", "");
            } else {
                AlertManager.Alert(Alert.AlertType.ERROR,
                        "Fiyatlandırma ayarları kaydedilemedi!", "Hata", "");
            }
        } catch (NumberFormatException e) {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Lütfen geçerli sayısal değerler girin!", "Hata", "");
        }
    }

    /**
     * Tüm odaların fiyatlarını güncel ayarlara göre günceller
     */
    @FXML
    private void updateAllRoomPrices() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Fiyat Güncelleme");
        confirm.setHeaderText("Tüm odaların fiyatları güncellenecek!");
        confirm.setContentText("Bu işlem mevcut tüm odaların fiyatlarını yeni ayarlara göre yeniden hesaplayacak. Devam etmek istiyor musunuz?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Önce fiyatlandırma ayarlarını kaydet
                savePricingConfigSilent();

                // Tüm odaları güncelle
                int updatedCount = updateRoomPricesFromConfig();

                if (updatedCount >= 0) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION,
                            updatedCount + " odanın fiyatı başarıyla güncellendi!", "Başarılı", "");
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR,
                            "Oda fiyatları güncellenirken bir hata oluştu!", "Hata", "");
                }
            }
        });
    }

    /**
     * Fiyatlandırma ayarlarını sessizce kaydeder (alert göstermeden)
     */
    private void savePricingConfigSilent() {
        try {
            PricingConfig config = new PricingConfig();

            if (priceStandart != null) config.roomTypePrices.put("Standart", Double.parseDouble(priceStandart.getText()));
            if (priceDeluxe != null) config.roomTypePrices.put("Deluxe", Double.parseDouble(priceDeluxe.getText()));
            if (priceSuite != null) config.roomTypePrices.put("Suite", Double.parseDouble(priceSuite.getText()));
            if (priceAile != null) config.roomTypePrices.put("Aile", Double.parseDouble(priceAile.getText()));
            if (pricePenthouse != null) config.roomTypePrices.put("Penthouse", Double.parseDouble(pricePenthouse.getText()));

            if (priceSeaView != null) config.featurePrices.put("seaView", Double.parseDouble(priceSeaView.getText()));
            if (priceBalcony != null) config.featurePrices.put("balcony", Double.parseDouble(priceBalcony.getText()));
            if (priceJacuzzi != null) config.featurePrices.put("jacuzzi", Double.parseDouble(priceJacuzzi.getText()));
            if (priceKitchen != null) config.featurePrices.put("kitchen", Double.parseDouble(priceKitchen.getText()));

            ConfigManager.savePricingConfig(config);
        } catch (NumberFormatException e) {
            System.err.println("Fiyatlandırma kaydetme hatası: " + e.getMessage());
        }
    }

    /**
     * Veritabanındaki tüm odaların fiyatlarını güncel config'e göre günceller
     */
    private int updateRoomPricesFromConfig() {
        PricingConfig config = ConfigManager.loadPricingConfig();
        DBDataSelection selector = new DBDataSelection();
        DBDataUpdater updater = new DBDataUpdater();
        int updatedCount = 0;

        try {
            ResultSet rs = selector.selectData("Rooms", new String[]{"id", "roomType", "hasSeaView", "hasBalcony", "hasKitchen"});

            if (rs != null) {
                while (rs.next()) {
                    int roomId = rs.getInt("id");
                    String roomType = rs.getString("roomType");
                    boolean hasSeaView = rs.getBoolean("hasSeaView");
                    boolean hasBalcony = rs.getBoolean("hasBalcony");
                    boolean hasKitchen = rs.getBoolean("hasKitchen");

                    // Jakuzi kontrolü - kolon varsa kontrol et
                    boolean hasJacuzzi = false;
                    try {
                        hasJacuzzi = rs.getBoolean("hasJacuzzi");
                    } catch (SQLException ignored) {}

                    // Yeni fiyatı hesapla
                    double newPrice = config.calculatePrice(roomType, hasSeaView, hasBalcony, hasJacuzzi, hasKitchen);

                    // Fiyatı güncelle
                    boolean success = updater.updateDataWithCondition(
                            "Rooms",
                            new String[]{"pricePerNight"},
                            new String[]{"pricePerNight"},
                            new String[]{String.valueOf(newPrice)},
                            new String[]{"id"},
                            new Object[]{roomId}
                    );

                    if (success) updatedCount++;
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Oda fiyatları güncelleme hatası: " + e.getMessage());
            return -1;
        }

        // Config cache'ini temizle
        ConfigManager.clearPricingConfigCache();

        return updatedCount;
    }

    @FXML
    private void testConnection() {
        String server = dbServerField != null ? dbServerField.getText() : "localhost";
        String port = dbPortField != null ? dbPortField.getText() : "1433";
        String dbName = dbNameField != null ? dbNameField.getText() : "otel_db";
        String username = dbUsernameField != null ? dbUsernameField.getText() : "";
        String password = dbPasswordField != null ? dbPasswordField.getText() : "";

        String testUrl = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;integratedSecurity=false;",
            server, port, dbName
        );

        try {
            Connection testConn = DriverManager.getConnection(testUrl, username, password);
            boolean isValid = testConn.isValid(5);
            testConn.close();

            if (isValid) {
                AlertManager.Alert(Alert.AlertType.INFORMATION,
                        "Veritabanı bağlantısı başarılı!", "Başarılı", "");
            } else {
                AlertManager.Alert(Alert.AlertType.ERROR,
                        "Veritabanı bağlantısı doğrulanamadı!", "Hata", "");
            }
        } catch (SQLException e) {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Veritabanı bağlantısı başarısız!\n\nHata: " + e.getMessage(), "Hata", "");
        }
    }

    @FXML
    private void backupDatabase() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Veritabanı yedekleme özelliği yakında eklenecek!", "Bilgi", "");
    }

    @FXML
    private void restoreBackup() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Veritabanı geri yükleme özelliği yakında eklenecek!", "Bilgi", "");
    }

    @FXML
    private void purgeDatabase() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Veritabanı Temizleme");
        alert.setHeaderText("DİKKAT!");
        alert.setContentText("Tüm veriler silinecek. Devam etmek istediğinize emin misiniz?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AlertManager.Alert(Alert.AlertType.INFORMATION,
                        "Veritabanı temizleme özelliği güvenlik nedeniyle devre dışı!",
                        "Bilgi", "");
            }
        });
    }
}
