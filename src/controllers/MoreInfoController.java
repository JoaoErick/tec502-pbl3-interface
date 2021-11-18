package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import models.Travel;

/**
 * 
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class MoreInfoController implements Initializable {
    private static Travel travel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public static Travel getTravel() {
        return travel;
    }

    public static void setTravel(Travel travel) {
        MoreInfoController.travel = travel;
    }
    
}
