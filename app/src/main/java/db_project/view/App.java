package db_project.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/TestTicketReservation.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 1000, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
