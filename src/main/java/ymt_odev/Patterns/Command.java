package ymt_odev.Patterns;

/**
 * Command Pattern - Komut arayüzü
 * Rezervasyon işlemleri için geri alınabilir komutlar
 */
public interface Command {
    boolean execute();
    boolean undo();
    String getDescription();
}

