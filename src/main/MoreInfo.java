package main;

import controllers.MoreInfoController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Travel;

/**
 * Tela de mais informações a respeito de um trajeto.
 * 
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class MoreInfo extends Application {

    private static Stage stage;

    public MoreInfo(Travel travel) {
        MoreInfoController.setTravel(travel);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MoreInfo.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Companhia Aérea - Informações do voo");
        stage.setResizable(false);
        stage.show();
        setStage(stage);

        Image image = new Image("/images/plane-solid.png");

        stage.getIcons().add(image);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Retorna o objeto da janela instanciada.
     *
     * @return Stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Altera o objeto da janela instanciada.
     * 
     * @param stage Stage - Nova janela.
     */
    public static void setStage(Stage stage) {
        MoreInfo.stage = stage;
    }

}
