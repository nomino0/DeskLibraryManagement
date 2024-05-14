package crud;

import entities.Livre;
import utils.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreCrud {
    public void ajouterLivre(Livre livre) {
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "INSERT INTO Livre (titre, auteur, datePub, isbn, prix, genre, disponible, imageUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, livre.getTitre());
            preparedStatement.setString(2, livre.getAuteur());
            preparedStatement.setDate(3, new java.sql.Date(livre.getDatePub().getTime()));
            preparedStatement.setString(4, livre.getIsbn());
            preparedStatement.setFloat(5, livre.getPrix());
            preparedStatement.setString(6, livre.getGenre());
            preparedStatement.setString(7, livre.getDisponible());
            preparedStatement.setString(8, livre.getImageUrl()); // Set imageUrl parameter
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void supprimerLivre(int id) {
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "DELETE FROM Livre WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifierLivre(Livre livre) {
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "UPDATE Livre SET titre = ?, auteur = ?, datePub = ?, isbn = ?, prix = ?, genre = ?, disponible = ?, imageUrl = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, livre.getTitre());
            preparedStatement.setString(2, livre.getAuteur());
            preparedStatement.setDate(3, new java.sql.Date(livre.getDatePub().getTime()));
            preparedStatement.setString(4, livre.getIsbn());
            preparedStatement.setFloat(5, livre.getPrix());
            preparedStatement.setString(6, livre.getGenre());
            preparedStatement.setString(7, livre.getDisponible());
            preparedStatement.setString(8, livre.getImageUrl()); // Set imageUrl parameter
            preparedStatement.setInt(9, livre.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "SELECT * FROM Livre";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Livre livre = new Livre(
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("auteur"),
                        resultSet.getDate("datePub"),
                        resultSet.getString("isbn"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("genre"),
                        resultSet.getString("disponible"),
                        resultSet.getString("imageUrl") // Include imageUrl
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

}
