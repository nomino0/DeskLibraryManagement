package controllers;

import crud.LivreCrud;
import entities.Livre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LivreController {

    @FXML
    private TextField searchText;
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
    private ImageView coverImageView; // Add ImageView field for displaying the uploaded image

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

        livreListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                loadBookDetails(newValue);
            }
        });
    }

    private void loadBookDetails(Livre livre) {
        titreTextField.setText(livre.getTitre());
        auteurTextField.setText(livre.getAuteur());

        // Convert java.sql.Date to LocalDate
        java.sql.Date datePub = (java.sql.Date) livre.getDatePub();
        if (datePub != null) {
            datePicker.setValue(datePub.toLocalDate());
        } else {
            datePicker.setValue(null);
        }

        isbnTextField.setText(livre.getIsbn());
        prixTextField.setText(String.valueOf(livre.getPrix()));
        genreTextField.setText(livre.getGenre());
        disponibiliteComboBox.setValue(livre.getDisponible());
    }

    private void loadLivres() {
        livres.addAll(livreCrud.getAllLivres());
        livreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        livreListView.setItems(livres);
    }

    private void searchLivre(String searchText) throws SQLException {
        List<Livre> searchResult = livres.stream()
                .filter(livre -> {
                    String title = livre.getTitre();
                    String auteur = livre.getAuteur();
                    String isbn = livre.getIsbn();
                    // Add other fields here (date, price, genre, etc.) if necessary

                    return title != null && title.toLowerCase().contains(searchText.toLowerCase())
                            || auteur != null && auteur.toLowerCase().contains(searchText.toLowerCase())
                            || isbn != null && isbn.toLowerCase().contains(searchText.toLowerCase());
                })
                .collect(Collectors.toList());

        livreListView.setItems(FXCollections.observableArrayList(searchResult));
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
        LocalDate datePubValue = datePicker.getValue();
        String isbn = isbnTextField.getText();
        String prixText = prixTextField.getText();
        String genre = genreTextField.getText();
        String disponibilite = disponibiliteComboBox.getValue();

        // Check if any field is empty
        if (titre.isEmpty() || auteur.isEmpty() || datePubValue == null || isbn.isEmpty() || prixText.isEmpty() || genre.isEmpty() || disponibilite == null) {
            afficherAlerteErreur("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        // Parse date and price
        Date datePub = java.sql.Date.valueOf(datePubValue);
        float prix;
        try {
            prix = Float.parseFloat(prixText);
        } catch (NumberFormatException e) {
            afficherAlerteErreur("Erreur de format", "Le prix doit être un nombre valide.");
            return;
        }

        // Create the new Livre object
        Livre nouveauLivre = new Livre(titre, auteur, isbn, datePub, prix, genre, disponibilite);

        try {
            // Add the book to the database
            livreCrud.ajouterLivre(nouveauLivre);
            System.out.println("Livre ajouté avec succès !");

            // Add the new book to the list
            livres.add(nouveauLivre);

            // Show success message
            afficherAlerteInformation("Ajout réussi", "Le livre a été ajouté avec succès.");

            // Clear all fields
            titreTextField.clear();
            auteurTextField.clear();
            datePicker.setValue(null);
            isbnTextField.clear();
            prixTextField.clear();
            genreTextField.clear();
            disponibiliteComboBox.getSelectionModel().clearSelection();
        } catch (Exception e) {
            // Handle any exception
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
            // Check if all fields are filled
            if (areAllFieldsFilled()) {
                String titre = titreTextField.getText();
                String auteur = auteurTextField.getText();
                LocalDate datePubValue = datePicker.getValue();
                String isbn = isbnTextField.getText();
                String prixText = prixTextField.getText();
                String genre = genreTextField.getText();
                String disponibilite = disponibiliteComboBox.getValue();

                // Parse date and price
                Date datePub = java.sql.Date.valueOf(datePubValue);
                float prix = Float.parseFloat(prixText);

                // Update the information of the selected book
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
                // Display an error message if any field is empty
                afficherAlerteErreur("Champs requis", "Veuillez remplir tous les champs.");
            }
        } else {
            afficherAlerteErreur("Sélection requise", "Veuillez sélectionner un livre à modifier.");
        }
        livreListView.refresh();
    }

    private boolean areAllFieldsFilled() {
        return !titreTextField.getText().isEmpty() &&
                !auteurTextField.getText().isEmpty() &&
                datePicker.getValue() != null &&
                !isbnTextField.getText().isEmpty() &&
                !prixTextField.getText().isEmpty() &&
                !genreTextField.getText().isEmpty() &&
                disponibiliteComboBox.getValue() != null;
    }

    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void AjoutReservation(ActionEvent actionEvent) throws SQLException {
        Livre livreSelectionne = livreListView.getSelectionModel().getSelectedItem();
        if (livreSelectionne != null) {
            // Check if the book is available
            if ("Disponible".equals(livreSelectionne.getDisponible())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutReservation.fxml"));
                    Parent root = loader.load();
                    System.out.println("FXML file loaded successfully.");
                    AjoutReservation controller = loader.getController();
                    System.out.println("Controller initialized.");

                    controller.setLivre(livreSelectionne);
                    System.out.println("Data initialized in controller.");

                    Stage stage = new Stage();
                    stage.setTitle("Ajout Reservation");
                    stage.setScene(new Scene(root));

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Show an alert
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Livre non disponible");
                alert.setHeaderText("Le livre sélectionné n'est pas disponible.");
                alert.setContentText("Veuillez sélectionner un autre livre.");
                alert.showAndWait();
            }
        } else {
            System.out.println("Aucun Livre sélectionné.");
        }
        livreListView.refresh();
    }

    public void ConsulterReservation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
            Parent root = loader.load();
            System.out.println("FXML file loaded successfully.");
            ReservationController controller = loader.getController();
            System.out.println("Controller initialized.");

            System.out.println("Data initialized in controller.");

            Stage stage = new Stage();
            stage.setTitle("Ajout Reservation");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void uploadCoverImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de couverture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        Stage stage = (Stage) coverImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            coverImageView.setImage(image);
        }
    }
}
