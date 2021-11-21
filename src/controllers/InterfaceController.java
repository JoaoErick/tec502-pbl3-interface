
package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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
import models.Reader;
import models.Travel;

/**
 * Classe controladora da página inicial da interface da companhia aérea.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class InterfaceController extends StageController implements Initializable {
    
    @FXML
    private TableView<Travel> table;

    @FXML
    private TableColumn<Travel, Integer> clmParts;

    @FXML
    private TableColumn<Travel, String> clmRoutes;

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
    private static final int PORT = 12240;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Preenche as cidades nos componentes de seleção.
        fillComboBox();
        
        cBoxDestination.setDisable(true);
        btnSearch.setDisable(true);
        
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
        
        cBoxOrigin.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                cBoxDestination.setDisable(false);
                removeOption(cBoxDestination, oldValue, newValue);
            }
        
        });
        
        cBoxDestination.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                btnSearch.setDisable(false);
                removeOption(cBoxOrigin, oldValue, newValue);
            }
        
        });
          
    }  
    
    /**
     * Inicializa a tabela de voos com dados presentes na lista de voos.
     */
    private void initTable() {
        clmParts.setCellValueFactory(new PropertyValueFactory("totalParts"));
        clmRoutes.setCellValueFactory(new PropertyValueFactory("cities"));
        
        table.setItems(listToObservableList());
    }
    
    /**
     * Preenche as cidades nos componentes de seleção a partir da leitura do 
     * arquivo de cidades.
     */
    private void fillComboBox(){
        Reader reader = new Reader();
        
        for(String city: reader.readCities()){
            cBoxOrigin.getItems().add(city);
            cBoxDestination.getItems().add(city);
        }
    }
    
    /**
     * Remove a opção de outro componente de seleção que foi selecionada no 
     * componente de seleção especificado.
     * 
     * @param option ComboBox<String> - Componente de seleção.
     * @param oldValue Object - Antiga opção selecionada.
     * @param newValue Object - Nova opção selecionada.
     */
    private void removeOption(ComboBox<String> option, Object oldValue, Object newValue){
        option.getItems().remove((String) newValue);
        if(oldValue != null)
            option.getItems().add((String) oldValue);

        option.getItems().sort(new Comparator<String>() {
            @Override
            public int compare(String city1, String city2) {
                return city1.compareTo(city2);
            }
        });
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
    
    /**
     * Requisitas as possíveis rotas de voo aos servidores e atualiza a tabela.
     */
    private void search(){
        requestTravels();
        for(Travel travel : travels){
            travel.setAttributes();
        }
        travels.sort(new Comparator<Travel>() {
            @Override
            public int compare(Travel travel1, Travel travel2) {
                return new Integer(travel1.getTotalParts()).compareTo(new Integer(travel2.getTotalParts()));
            }
        });
        initTable();
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
            
            ObjectOutputStream output2 = new ObjectOutputStream(client.getOutputStream());
            output2.flush();
            output2.writeObject(cBoxOrigin.getValue() + "," + cBoxDestination.getValue());

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
