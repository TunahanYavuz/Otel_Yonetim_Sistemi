package ymt_odev.Patterns;

import ymt_odev.Services.ReservationService;

/**
 * Command Pattern - Rezervasyon iptal komutu
 */
public class CancelReservationCommand implements Command {
    private final int reservationId;
    private String previousState;

    public CancelReservationCommand(int reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public boolean execute() {
        // Önceki durumu sakla (undo için)
        // TODO: Veritabanından önceki durumu al
        previousState = "CONFIRMED";

        return ReservationService.cancelReservation(reservationId);
    }

    @Override
    public boolean undo() {
        // İptali geri al, önceki duruma döndür
        return ReservationService.updateReservationState(reservationId, previousState);
    }

    @Override
    public String getDescription() {
        return "Rezervasyon iptal edildi: " + reservationId;
    }
}

