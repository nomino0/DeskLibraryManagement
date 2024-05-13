package controllers;

import crud.ReservationCrud;
import entities.Livre;
import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {
    ReservationCrud reservationCrud;

    @FXML
    private TableColumn<Reservation, String> cinClientColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> dateDebutColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, Livre> livreColumn;

    @FXML
    private TableColumn<Reservation, String> nomClientColumn;
    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    void initialize() {
        reservationCrud = new ReservationCrud();
        chargerReservations();
    }
    public void chargerReservations() {
            List<Reservation> reservations = reservationCrud.getAllReservations();
            reservationTable.getItems().setAll(reservations);

            cinClientColumn.setCellValueFactory(new PropertyValueFactory<>("cinClient"));
            dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            livreColumn.setCellValueFactory(new PropertyValueFactory<>("livre"));
            nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));

    }

    // Méthode pour supprimer une réservation
    @FXML
    void supprimerReservation(ActionEvent event) {
        Reservation reservationSelectionnee = reservationTable.getSelectionModel().getSelectedItem();
        if (reservationSelectionnee != null) {
            int reservationId = reservationSelectionnee.getId();
            reservationCrud.deleteReservation(reservationId);
            // Actualiser la liste des réservations après la suppression
            chargerReservations();
        } else {
            System.out.println("Aucune réservation sélectionnée.");
        }
    }

}
