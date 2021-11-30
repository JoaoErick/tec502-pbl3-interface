package main;

import controllers.InterfaceController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Tela de escolher a cidade de origem e destino.
 * 
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Interface extends Application {

    private static Stage stage;
    
    /**
     * Método construtor.
     * 
     * @param companyName String - Nome da companhia.
     */
    public Interface(String companyName) {
        InterfaceController.setCompanyName(companyName);
    }
    
    public Interface() {}

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Interface.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Companhia Aérea");
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
        Interface.stage = stage;
    }

}
