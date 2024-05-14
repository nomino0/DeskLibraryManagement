package controllers;

import crud.LivreCrud;
import crud.ReservationCrud;
import entities.Livre;
import entities.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.AlertUtils;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class ReservationController {
    ReservationCrud reservationCrud;
    LivreCrud livreCrud;

    @FXML
    private TableColumn<Reservation, String> cinClientColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> dateDebutColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, String> livreColumn;

    @FXML
    private TableColumn<Reservation, String> nomClientColumn;

    @FXML
    private TableView<Reservation> reservationTable;

    private List<Reservation> nonArchivedReservations;
    private List<Reservation> archivedReservations;

    @FXML
    private Button archiverButton;


    public ReservationController() {
        this.reservationCrud = new ReservationCrud();
        this.livreCrud = new LivreCrud();
    }

    @FXML
    void initialize() {
        nonArchivedReservations = new ArrayList<>();
        archivedReservations = new ArrayList<>();

        cinClientColumn.setCellValueFactory(new PropertyValueFactory<>("cinClient"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        livreColumn.setCellValueFactory(param -> {
            Livre livre = param.getValue().getLivre();
            String title = livre.getTitre();
            String isbn = livre.getIsbn();
            return new SimpleStringProperty(title + " - ISBN: " + isbn);
        });

        livreColumn.setSortable(false); // Disable sorting on this column

        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));

        reservationTable.setRowFactory(tv -> new TableRow<Reservation>() {
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || item.isArchived()) {
                    setStyle("-fx-background-color: lightgray; -fx-text-fill: gray;");
                } else {
                    setStyle("");
                }
            }
        });

        chargerNonArchivedReservations();
    }

    public void chargerNonArchivedReservations() {
        nonArchivedReservations = reservationCrud.getNonArchivedReservations();
        reservationTable.getItems().setAll(nonArchivedReservations);
    }

    public void chargerArchivedReservations() {
        archivedReservations = reservationCrud.getArchivedReservations();
        reservationTable.getItems().setAll(archivedReservations);
    }

    @FXML
    void archiverReservation(ActionEvent event) {
        Reservation reservationSelectionnee = reservationTable.getSelectionModel().getSelectedItem();
        if (reservationSelectionnee != null) {
            if (!reservationSelectionnee.isArchived()) {
                Livre livre = reservationSelectionnee.getLivre();
                if (livre != null) {
                    livre.setDisponible("Disponible");
                    livreCrud.modifierLivre(livre);
                }

                int reservationId = reservationSelectionnee.getId();
                reservationSelectionnee.setArchived(true);
                reservationCrud.updateReservation(reservationSelectionnee);

                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Réservation archivée avec succès!");

                chargerNonArchivedReservations();
                reservationTable.refresh();
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Erreur", "La réservation est déjà archivée!");
            }
        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune réservation sélectionnée.");
        }
    }

    @FXML
    void supprimerReservation(ActionEvent event) {
        Reservation reservationSelectionnee = reservationTable.getSelectionModel().getSelectedItem();
        if (reservationSelectionnee != null) {
            Livre livre = reservationSelectionnee.getLivre();
            if (livre != null) {
                livre.setDisponible("Disponible");
                livreCrud.modifierLivre(livre);
            }

            int reservationId = reservationSelectionnee.getId();
            reservationCrud.deleteReservation(reservationId);

            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Réservation supprimée avec succès!");

            chargerNonArchivedReservations();
            reservationTable.refresh();
        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune réservation sélectionnée.");
        }
    }

    @FXML
    void afficherArchives(ActionEvent event) {
        chargerArchivedReservations();
        archiverButton.setDisable(true);
    }

    @FXML
    void afficherActives(ActionEvent event) {
        chargerNonArchivedReservations();
        archiverButton.setDisable(false);
    }
}
