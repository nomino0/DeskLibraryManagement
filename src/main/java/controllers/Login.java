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

        // Vérification des informations d'identification dans la base de données
        if (validateCredentials(email, password)) {
            if (isAdmin(email)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherLivres.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    
                        stage.setScene(scene);
                        stage.setTitle("AfficherLivres");
                        stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Erreur lors du chargement de la vue AfficherLivres.fxml : " + e.getMessage());
                }
            } else {
                // L'utilisateur n'est pas un administrateur, afficher un message de réussite
                errorMessage.setText("Connexion réussie !");
                errorMessage.setStyle("-fx-text-fill: green;");
            }
        } else {
            // Informations d'identification incorrectes, afficher un message d'erreur
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
        try {
            Connection  connection = ConnectionDB.getInstance().getConnection();
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement  statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet   resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isAdmin(String email) {
        try {
            Connection connection = ConnectionDB.getInstance().getConnection();
            String query = "SELECT role FROM user WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                // Vérifie si le rôle de l'utilisateur est "admin"
                return role.equals("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openAdminView() {

    }
}
