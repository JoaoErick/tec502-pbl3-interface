package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.Interface;
import main.MoreInfo;
import models.Travel;

/**
 * 
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class MoreInfoController extends StageController implements Initializable {
    @FXML
    private Button btnBack;

    @FXML
    private ImageView btnArrowBack;

    @FXML
    private Label lblRoute;

    @FXML
    private Label lblParts;

    @FXML
    private Label lblTimeTravel;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Button btnBuy;
    
    private static Travel travel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillFields();
        
        btnBack.setOnMouseClicked((MouseEvent e)->{
            back(MoreInfo.getStage(), new Interface());
        });
        
        btnBack.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() == KeyCode.ENTER){
                back(MoreInfo.getStage(), new Interface());
            }
        });
        
        btnArrowBack.setOnMouseClicked((MouseEvent e)->{
            back(MoreInfo.getStage(), new Interface());
        });
        
        btnArrowBack.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() == KeyCode.ENTER){
                back(MoreInfo.getStage(), new Interface());
            }
        });
    } 
    
    private void fillFields(){
        lblParts.setText((travel.getTotalParts() != 0 ? String.valueOf(travel.getTotalParts()) : "Sem escala"));
        lblTimeTravel.setText(String.format("%.2f", travel.getTotalTime()) + " Horas");
        lblTotalPrice.setText("R$ " + String.format("%.2f", travel.getTotalPrice()));
    }
    
    public static Travel getTravel() {
        return travel;
    }

    public static void setTravel(Travel travel) {
        MoreInfoController.travel = travel;
    }
    
}
