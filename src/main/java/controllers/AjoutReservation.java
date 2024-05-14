package controllers;

import crud.LivreCrud;
import crud.ReservationCrud;
import entities.Livre;
import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

    private LivreCrud livreCrud;

    public AjoutReservation() {
        this.livreCrud = new LivreCrud();
    }

    Livre livreSelectionne ;
    public void validerAction(ActionEvent actionEvent) {
        String nomClient = nomClientField.getText();
        String cinClient = cinClientField.getText();
        java.util.Date dateDebut = java.sql.Date.valueOf(dateDebutPicker.getValue());
        java.util.Date dateFin = java.sql.Date.valueOf(dateFinPicker.getValue());


         Reservation reservation = new Reservation(livreSelectionne, dateDebut, dateFin, nomClient, cinClient);

         // Create the reservation in the database
        reservationCrud.createReservation(reservation);

        // Update the availability status of the book to "Non disponible"
        livreSelectionne.setDisponible("Non disponible");
        livreCrud.modifierLivre(livreSelectionne);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Réservation réussie");
        successAlert.setHeaderText(null);
        successAlert.setContentText("La réservation a été effectuée avec succès." +
                "Disponibilité mise à jour.");
        successAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Close the window
                Stage stage = (Stage) cinClientField.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void annulerAction(ActionEvent actionEvent) {
        ((Stage) cinClientField.getScene().getWindow()).close();
    }



    public void setLivre(Livre livre) {
        this.livreSelectionne = livre;

    }

}
