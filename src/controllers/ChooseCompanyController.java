package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.ChooseCompany;
import main.Interface;

/**
 * Controlador da tela de escolha da companhia aérea.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class ChooseCompanyController extends StageController implements Initializable {

    @FXML
    private Button btnGOL;

    @FXML
    private Button btnTAM;

    @FXML
    private Button btnAzul;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAzul.setOnMouseClicked((MouseEvent e) -> {
            Interface inter = new Interface("Azul");
            close(ChooseCompany.getStage());
            try {
                inter.start(new Stage());
            } catch (Exception ex) {
                System.err.println("Não foi possível exibir mais acessar a "
                        + "interface da companhia Azul.");
                System.out.println(ex);
            }
        });
        btnGOL.setOnMouseClicked((MouseEvent e) -> {
            Interface inter = new Interface("GOL");
            close(ChooseCompany.getStage());
            try {
                inter.start(new Stage());
            } catch (Exception ex) {
                System.err.println("Não foi possível exibir mais acessar a "
                        + "interface da companhia Gol.");
                System.out.println(ex);
            }
        });
        btnTAM.setOnMouseClicked((MouseEvent e) -> {
            Interface inter = new Interface("TAM");
            close(ChooseCompany.getStage());
            try {
                inter.start(new Stage());
            } catch (Exception ex) {
                System.err.println("Não foi possível exibir mais acessar a "
                        + "interface da companhia Tam.");
                System.out.println(ex);
            }
        });
        
    }    
    
}
