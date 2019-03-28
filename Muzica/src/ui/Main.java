package ui;

import domain.Muzica;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.Service;

import javax.swing.event.ChangeListener;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Service service=new Service("Albume.txt");
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));
        root = loader.load();
        Controller ctrl = loader.getController();
        ctrl.setFXMLController(service);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Albume");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);//.show();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
