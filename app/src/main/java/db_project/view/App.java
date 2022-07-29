package db_project.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

<<<<<<< HEAD
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("FXML text");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
=======
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/Login.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 600, 400);
    primaryStage.setTitle("FXML text");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
>>>>>>> 2a2de119376de7b282f23250f9db42b0be8d484c
