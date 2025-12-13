package ymt_odev.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON konfigürasyon dosyalarını okuma/yazma işlemlerini yöneten sınıf
 */
public class ConfigManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String DB_CONFIG_FILE = "db-config.json";
    private static final String PRICING_CONFIG_FILE = "pricing-config.json";
    private static final String INFO_CONFIG_FILE = "info-config.json";

    private static DbConfig cachedDbConfig = null;
    private static PricingConfig cachedPricingConfig = null;
    private static InfoConfig cachedInfoConfig = null;


    public static class InfoConfig{
        public String hotel_name;
        public String hotel_address;
        public String hotel_phone;
        public String hotel_email;
        public InfoConfig(){
            hotel_name = "Hamsi Hotel";
            hotel_address = "Kırklareli/Türkiye";
            hotel_phone = "+90 (212) 555-0000";
            hotel_email = "hamsikirklareli@hotel.com";
        }
    }

    /**
     * Veritabanı konfigürasyon modeli
     */
    public static class DbConfig {
        public String server = "localhost";
        public String port = "1433";
        public String databaseName = "otel_db";
        public String username = "otel";
        public String password = "123456";

        public String getConnectionUrl() {
            return String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;integratedSecurity=false;",
                server, port, databaseName
            );
        }
    }

    /**
     * Fiyatlandırma konfigürasyon modeli
     */
    public static class PricingConfig {
        public Map<String, Double> roomTypePrices = new HashMap<>();
        public Map<String, Double> featurePrices = new HashMap<>();

        public PricingConfig() {
            // Varsayılan değerler
            roomTypePrices.put("Standart", 500.0);
            roomTypePrices.put("Deluxe", 800.0);
            roomTypePrices.put("Suite", 1200.0);
            roomTypePrices.put("Aile", 1000.0);
            roomTypePrices.put("Penthouse", 2500.0);

            featurePrices.put("seaView", 150.0);
            featurePrices.put("balcony", 100.0);
            featurePrices.put("jacuzzi", 200.0);
            featurePrices.put("kitchen", 120.0);
        }

        /**
         * Oda tipi ve özelliklere göre fiyat hesaplar
         */
        public double calculatePrice(String roomType, boolean hasSeaView, boolean hasBalcony,
                                     boolean hasJacuzzi, boolean hasKitchen) {
            double basePrice = roomTypePrices.getOrDefault(roomType, 500.0);

            if (hasSeaView) basePrice += featurePrices.getOrDefault("seaView", 0.0);
            if (hasBalcony) basePrice += featurePrices.getOrDefault("balcony", 0.0);
            if (hasJacuzzi) basePrice += featurePrices.getOrDefault("jacuzzi", 0.0);
            if (hasKitchen) basePrice += featurePrices.getOrDefault("kitchen", 0.0);

            return basePrice;
        }
    }

    /**
     * Konfigürasyon dosyasının yolunu döndürür
     */
    private static Path getConfigPath(String fileName) {
        // Önce çalışma dizininde ara
        Path workingDirPath = Paths.get(fileName);
        if (Files.exists(workingDirPath)) {
            return workingDirPath;
        }

        // Sonra resources klasöründe ara
        URL resourceUrl = ConfigManager.class.getClassLoader().getResource(fileName);
        if (resourceUrl != null) {
            try {
                // JAR içindeyse veya resources'taysa, kullanıcı dizinine kopyala
                Path userConfigPath = Paths.get(System.getProperty("user.dir"), fileName);
                if (!Files.exists(userConfigPath)) {
                    try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)) {
                        if (is != null) {
                            Files.copy(is, userConfigPath);
                        }
                    }
                }
                return userConfigPath;
            } catch (IOException e) {
                System.err.println("Konfigürasyon dosyası kopyalanamadı: " + e.getMessage());
            }
        }

        // Hiçbiri yoksa çalışma dizininde oluştur
        return Paths.get(System.getProperty("user.dir"), fileName);
    }

    public static InfoConfig loadInfoConfig(){
        if (cachedInfoConfig != null) {
            return cachedInfoConfig;
        }
        Path configPath = getConfigPath(INFO_CONFIG_FILE);
        try {
            if (Files.exists(configPath)){
                String json = Files.readString(configPath);
                cachedInfoConfig = gson.fromJson(json, InfoConfig.class);
                System.out.println("✅ Bilgi konfigürasyonu yüklendi: " + configPath);
            }else {
                cachedInfoConfig = new InfoConfig();
                saveInfoConfig(cachedInfoConfig);
                System.out.println("📝 Varsayılan bilgi konfigürasyonu oluşturuldu: " + configPath);
            }
        }catch (IOException e){
            System.err.println("❌ Bilgi konfigürasyonu okunamadı: " + e.getMessage());
            cachedInfoConfig = new InfoConfig();
        }
        return cachedInfoConfig;
    }

    public static boolean saveInfoConfig(InfoConfig config){
        Path configPath = getConfigPath(INFO_CONFIG_FILE);
        try {
            String json = gson.toJson(config);
            Files.writeString(configPath, json);
            System.out.println("✅ Bilgi konfigürasyonu kaydedildi: " + configPath);
            return true;
        }catch (IOException e){
            System.err.println("❌ Bilgi konfigürasyonu kaydedilemedi: " + e.getMessage());
            return false;
        }
    }


    /**
     * Veritabanı konfigürasyonunu okur
     */
    public static DbConfig loadDbConfig() {
        if (cachedDbConfig != null) {
            return cachedDbConfig;
        }

        Path configPath = getConfigPath(DB_CONFIG_FILE);

        try {
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                cachedDbConfig = gson.fromJson(json, DbConfig.class);
                System.out.println("✅ Veritabanı konfigürasyonu yüklendi: " + configPath);
            } else {
                // Varsayılan konfigürasyon oluştur
                cachedDbConfig = new DbConfig();
                saveDbConfig(cachedDbConfig);
                System.out.println("📝 Varsayılan veritabanı konfigürasyonu oluşturuldu: " + configPath);
            }
        } catch (IOException e) {
            System.err.println("❌ Veritabanı konfigürasyonu okunamadı: " + e.getMessage());
            cachedDbConfig = new DbConfig();
        }

        return cachedDbConfig;
    }

    /**
     * Veritabanı konfigürasyonunu kaydeder
     */
    public static boolean saveDbConfig(DbConfig config) {
        Path configPath = getConfigPath(DB_CONFIG_FILE);

        try {
            String json = gson.toJson(config);
            Files.writeString(configPath, json);
            System.out.println("✅ Veritabanı konfigürasyonu kaydedildi: " + configPath);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Veritabanı konfigürasyonu kaydedilemedi: " + e.getMessage());
            return false;
        }
    }



    /**
     * Fiyatlandırma konfigürasyonunu okur
     */
    public static PricingConfig loadPricingConfig() {
        if (cachedPricingConfig != null) {
            return cachedPricingConfig;
        }

        Path configPath = getConfigPath(PRICING_CONFIG_FILE);

        try {
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                cachedPricingConfig = gson.fromJson(json, PricingConfig.class);
                System.out.println("✅ Fiyatlandırma konfigürasyonu yüklendi: " + configPath);
            } else {
                // Varsayılan konfigürasyon oluştur
                cachedPricingConfig = new PricingConfig();
                savePricingConfig(cachedPricingConfig);
                System.out.println("📝 Varsayılan fiyatlandırma konfigürasyonu oluşturuldu: " + configPath);
            }
        } catch (IOException e) {
            System.err.println("❌ Fiyatlandırma konfigürasyonu okunamadı: " + e.getMessage());
            cachedPricingConfig = new PricingConfig();
        }

        return cachedPricingConfig;
    }

    /**
     * Fiyatlandırma konfigürasyonunu kaydeder
     */
    public static boolean savePricingConfig(PricingConfig config) {
        Path configPath = getConfigPath(PRICING_CONFIG_FILE);

        try {
            String json = gson.toJson(config);
            Files.writeString(configPath, json);
            cachedPricingConfig = config;
            System.out.println("✅ Fiyatlandırma konfigürasyonu kaydedildi: " + configPath);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Fiyatlandırma konfigürasyonu kaydedilemedi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Önbelleği temizler (veritabanı bağlantısı değiştiğinde kullanılır)
     */
    public static void clearCache() {
        cachedDbConfig = null;
        cachedPricingConfig = null;
    }

    /**
     * Veritabanı önbelleğini temizler
     */
    public static void clearDbConfigCache() {
        cachedDbConfig = null;
    }

    /**
     * Fiyatlandırma önbelleğini temizler
     */
    public static void clearPricingConfigCache() {
        cachedPricingConfig = null;
    }
}

