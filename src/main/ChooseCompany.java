package main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Tela de escolher a companhia.
 * 
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class ChooseCompany extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/ChooseCompany.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Escolha a companhia aérea");
        stage.setResizable(false);
        stage.show();
        setStage(stage);

        Image image = new Image("/images/plane-solid.png");

        stage.getIcons().add(image);
    }

    /**
     * Resgata argumentos passados por linha de comando.
     *
     * @param args
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
        ChooseCompany.stage = stage;
    }
    
}
