
package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Interface;
import main.MoreInfo;
import models.Travel;

/**
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class InterfaceController extends StageController implements Initializable {
    
    @FXML
    private TableView<Travel> table;

    @FXML
    private TableColumn<Travel, String> clmSeat;

    @FXML
    private TableColumn<Travel, String> clmPrice;

    @FXML
    private TableColumn<Travel, String> clmTime;

    @FXML
    private ComboBox<String> cBoxOrigin;

    @FXML
    private ComboBox<String> cBoxDestination;

    @FXML
    private Button btnSearch;

    @FXML
    private ImageView btnSearchIcon;
    
    private static Socket client;
    
    private static List<Travel> travels = new ArrayList();
    
    private Travel selected;
    
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 12250;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializa a tabela de voos.
        initTable();
        
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (Travel) newValue;
                if(selected != null){
                    MoreInfo alt = new MoreInfo(selected);
                    close(Interface.getStage());
                    try {
                        alt.start(new Stage());
                    } catch (Exception ex) {
                        Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setHeaderText("Selecione um voo!");
                    alert.show();
                }
                
            }
        
        });
        
       btnSearch.setOnMouseClicked((MouseEvent e)->{    
            search();
        });
        
        btnSearchIcon.setOnMouseClicked((MouseEvent e)->{          
            search();
        });
        
    }  
    
    /**
     * Inicializa a tabela de voos com dados presentes na lista de voos.
     */
    private void initTable() {
        clmSeat.setCellValueFactory(new PropertyValueFactory("totalSeat"));
        clmPrice.setCellValueFactory(new PropertyValueFactory("totalPrice"));
        clmTime.setCellValueFactory(new PropertyValueFactory("totalTime"));
    }
    
    /**
     * Inicializa a conexão cliente com o servidor através do endereço IP e
     * porta especificados.
     */
    private static void initClient() {
        try {
            client = new Socket(IP_ADDRESS, PORT);
            System.out.println("Conexão estabelecida!");
        } catch (IOException ioe) {
            System.err.println("Erro, a conexão com o servidor não foi "
                    + "estabelecida!");
            System.out.println(ioe);

            try {
                client.close();
            } catch (IOException ioex) {
                System.err.println("Não foi possível fechar a porta da conexão.");
                System.out.println(ioex);
            }
        }
    }
    
    /**
     * Converte a lista de voos de forma a ser compatível com tabela da
     * interface.
     *
     * @return ObservableList<PatientDevice> - Pacientes ordenados.
     */
    private ObservableList<Travel> listToObservableList() {
        return FXCollections.observableArrayList(travels);
    }
    
    private void search(){
        requestTravels();
        table.setItems(listToObservableList());
    }
    
    /**
     * Faz requisição ao servidor para resgatar a lista de voos partindo da 
     * cidade de origem e cidade de destino especificados.
     */
    private void requestTravels() {
        try {
            //Faz a conexão com o servidor.
            initClient();

            travels.removeAll(travels);

            //Enviando a requisição para o servidor.
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            output.writeObject(new String("GET /routes"));

            //Recebendo a resposta do servidor.
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());

            travels = (List<Travel>) input.readObject();

            client.close();
        } catch (IOException ex) {
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            client = null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Classe String não foi encontrada.");
            System.out.println(cnfe);
        }
    }
    
}
