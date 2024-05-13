package crud;

import entities.Livre;
import entities.Reservation;
import entities.User;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationCrud {

    // Create operation
    public void createReservation(Reservation reservation) {
        Connection connection = ConnectionDB.getInstance().getConnection();

        try {
            String query = "INSERT INTO Reservation (livre_id, date_debut, date_fin, nom_client, cin_client) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, reservation.getLivre().getId());
            statement.setDate(2, new java.sql.Date(reservation.getDateDebut().getTime()));
            statement.setDate(3, new java.sql.Date(reservation.getDateFin().getTime()));
            statement.setString(4, reservation.getNomClient());
            statement.setString(5, reservation.getCinClient());
            statement.executeUpdate();
            System.out.println("Reservation créée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read operation
    public List<Reservation> getAllReservations() {
        Connection connection = ConnectionDB.getInstance().getConnection();

        List<Reservation> reservations = new ArrayList<>();
        try {
            String query = "SELECT * FROM Reservation";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int livreId = resultSet.getInt("livre_id");
                Date dateDebut = resultSet.getDate("date_debut");
                Date dateFin = resultSet.getDate("date_fin");
                String nomClient = resultSet.getString("nom_client");
                String cinClient = resultSet.getString("cin_client");

                // Fetch corresponding Livre object based on livreId
                Livre livre = getLivreById(livreId);

                Reservation reservation = new Reservation(id, livre, dateDebut, dateFin, nomClient, cinClient);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Update operation
    public void updateReservation(Reservation reservation) {
        Connection connection = ConnectionDB.getInstance().getConnection();

        try {
            String query = "UPDATE Reservation SET livre_id=?, date_debut=?, date_fin=?, nom_client=?, cin_client=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, reservation.getLivre().getId());
            statement.setDate(2, new java.sql.Date(reservation.getDateDebut().getTime()));
            statement.setDate(3, new java.sql.Date(reservation.getDateFin().getTime()));
            statement.setString(4, reservation.getNomClient());
            statement.setString(5, reservation.getCinClient());
            statement.setInt(6, reservation.getId());
            statement.executeUpdate();
            System.out.println("Reservation mise à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void deleteReservation(int reservationId) {
        Connection connection = ConnectionDB.getInstance().getConnection();

        try {
            String query = "DELETE FROM Reservation WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, reservationId);
            statement.executeUpdate();
            System.out.println("Reservation supprimée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to fetch Livre object by its ID
    public Livre getLivreById(int livreId) {
        Livre livre = null;
        Connection connection = ConnectionDB.getInstance().getConnection();
        try {
            String query = "SELECT * FROM Livre WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, livreId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                livre = new Livre(
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("auteur"),
                        resultSet.getDate("datePub"),
                        resultSet.getString("isbn"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("genre"),
                        resultSet.getString("disponible")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livre;
    }
}
