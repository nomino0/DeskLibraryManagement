package controllers;

import crud.LivreCrud;
import entities.Livre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LivreController {
    @FXML
    private TextField searchText ;
    @FXML
    private TextField titreTextField;
    @FXML
    private TextField auteurTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField isbnTextField;
    @FXML
    private TextField prixTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private ComboBox<String> disponibiliteComboBox;

    @FXML
    private ListView<Livre> livreListView;

    private ObservableList<Livre> livres = FXCollections.observableArrayList();
    private LivreCrud livreCrud = new LivreCrud();

    @FXML
    public void initialize() {
        loadLivres();

        // Add a listener for changes in the search text
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchLivre(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    private void loadLivres() {
        livres.addAll(livreCrud.getAllLivres());
        livreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        livreListView.setItems(livres);
    }


    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void supprimerLivre(javafx.event.ActionEvent actionEvent) {
        Livre livreSelectionne = livreListView.getSelectionModel().getSelectedItem();
        if (livreSelectionne != null) {
            try {
                livreCrud.supprimerLivre(livreSelectionne.getId());
                System.out.println("Livre supprimé avec succès !");

                livreListView.getItems().remove(livreSelectionne);

                // Afficher une confirmation à l'utilisateur
                afficherAlerteErreur("Suppression réussie", "Le Livre a été supprimé avec succès.");
            } catch (Exception e) {
                // Gérer les autres exceptions
                afficherAlerteErreur("Erreur", "Une erreur s'est produite lors de la suppression de Livre : " + e.getMessage());
            }
        } else {
            afficherAlerteErreur("Sélection requise", "Veuillez sélectionner un Livre à supprimer.");
        }
        livreListView.refresh();
    }

    public void ajouterLivre(ActionEvent actionEvent) {
        String titre = titreTextField.getText();
        String auteur = auteurTextField.getText();
        Date datePub = java.sql.Date.valueOf(datePicker.getValue());
        String isbn = isbnTextField.getText();
        float prix = Float.parseFloat(prixTextField.getText());
        String genre = genreTextField.getText();
        String disponibilite = disponibiliteComboBox.getValue();

        Livre nouveauLivre = new Livre(titre, auteur, isbn, datePub, prix,genre, disponibilite);

        try {
            livreCrud.ajouterLivre(nouveauLivre);
            System.out.println("Livre ajouté avec succès !");
            livres.add(nouveauLivre); // Add the new book to the list
            afficherAlerteInformation("Ajout réussi", "Le livre a été ajouté avec succès.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void afficherAlerteInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void modifierLivre(ActionEvent actionEvent) {
        Livre livreSelectionne = livreListView.getSelectionModel().getSelectedItem();
        if (livreSelectionne != null) {
            String titre = titreTextField.getText();
            String auteur = auteurTextField.getText();
            Date datePub = java.sql.Date.valueOf(datePicker.getValue());
            String isbn = isbnTextField.getText();
            float prix = Float.parseFloat(prixTextField.getText());
            String genre = genreTextField.getText();
            String disponibilite = disponibiliteComboBox.getValue();

            // Mettez à jour les informations du livre sélectionné
            livreSelectionne.setTitre(titre);
            livreSelectionne.setAuteur(auteur);
            livreSelectionne.setDatePub(datePub);
            livreSelectionne.setIsbn(isbn);
            livreSelectionne.setPrix(prix);
            livreSelectionne.setGenre(genre);
            livreSelectionne.setDisponible(disponibilite);

            try {
                livreCrud.modifierLivre(livreSelectionne);
                System.out.println("Livre modifié avec succès !");
                afficherAlerteInformation("Modification réussie", "Le livre a été modifié avec succès.");
            } catch (Exception e) {
                afficherAlerteErreur("Erreur", "Une erreur s'est produite lors de la modification du livre : " + e.getMessage());
            }
        } else {
            afficherAlerteErreur("Sélection requise", "Veuillez sélectionner un livre à modifier.");
        }
        livreListView.refresh();
    }
    private void searchLivre(String searchText) throws SQLException {
        List<Livre> searchResult = livres.stream()
                .filter(livre -> {
                    String title = livre.getTitre();
                    String auteur = livre.getAuteur();
                    String isbn = livre.getIsbn();
                    // Ajoutez d'autres champs ici (date, prix, genre, etc.) si nécessaire

                    return title != null && title.toLowerCase().contains(searchText.toLowerCase())
                            || auteur != null && auteur.toLowerCase().contains(searchText.toLowerCase())
                            || isbn != null && isbn.toLowerCase().contains(searchText.toLowerCase());
                })
                .collect(Collectors.toList());

        livreListView.setItems(FXCollections.observableArrayList(searchResult));
    }


}

