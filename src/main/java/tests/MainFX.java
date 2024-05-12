package tests;

import entities.Livre;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class MainFX extends Application {

    @FXML
    private ListView<Livre> livreListView;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader= new FXMLLoader(getClass().getResource("/Login.fxml"));

        Parent root=loader.load();
        Scene scene=new Scene(root);



        stage.setScene(scene);
        stage.setTitle("AfficherLivres");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
