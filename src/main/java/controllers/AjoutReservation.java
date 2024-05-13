package controllers;

import crud.ReservationCrud;
import entities.Livre;
import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjoutReservation {
    ReservationCrud reservationCrud = new ReservationCrud();
    @FXML
    private TextField cinClientField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField nomClientField;
    Livre livreSelectionne ;
    public void validerAction(ActionEvent actionEvent) {
        String nomClient = nomClientField.getText();
        String cinClient = cinClientField.getText();
        java.util.Date dateDebut = java.sql.Date.valueOf(dateDebutPicker.getValue());
        java.util.Date dateFin = java.sql.Date.valueOf(dateFinPicker.getValue());

         Reservation reservation = new Reservation(livreSelectionne, dateDebut, dateFin, nomClient, cinClient);

        // Appeler la méthode pour créer la réservation dans la base de données
        reservationCrud.createReservation(reservation);
    }

    public void annulerAction(ActionEvent actionEvent) {
        ((Stage) cinClientField.getScene().getWindow()).close();
    }



    public void setLivre(Livre livre) {
        this.livreSelectionne = livre;

    }

}
