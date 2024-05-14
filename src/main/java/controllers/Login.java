package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.ConnectionDB;

import java.io.IOException;
import java.sql.*;

public class Login {

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    // Ajoutez ces champs pour la scène et la stage
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loginAction(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (validateCredentials(email, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherLivres.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                LivreController controller = loader.getController();
                if (controller != null) {

                    // Afficher la scène
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                    // Obtenir la scène actuelle à partir de l'événement

                    // Définir la nouvelle scène
                    stage.setScene(scene);
                    stage.show();

                }
                if (stage != null) {
                    stage.setScene(scene);
                    stage.setTitle("AfficherLivres");
                    stage.show();
                } else {
                    System.err.println("La stage n'est pas initialisée !");
                }
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de la vue AfficherLivres.fxml : " + e.getMessage());
                errorMessage.setText("Erreur lors du chargement de la vue AfficherLivres.");
                errorMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            errorMessage.setText("Nom d'utilisateur ou mot de passe incorrect !");
            errorMessage.setStyle("-fx-text-fill: red;");
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        emailField.clear();
        passwordField.clear();
        errorMessage.setText("");
    }

    private boolean validateCredentials(String email, String password) {
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la validation des informations d'identification : " + e.getMessage());
            return false;
        }
    }
}
