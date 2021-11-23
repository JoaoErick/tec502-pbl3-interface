package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Interface;
import main.MoreInfo;
import models.Reader;
import models.ServerAddress;
import models.Travel;

/**
 * Controlador da tela inicial da interface da companhia aérea.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class InterfaceController extends StageController implements Initializable {

    @FXML
    private Label lblCompanyName;
     
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

    @FXML
    private ImageView imgLoading;

    @FXML
    private Text txtRouteCalculate;

    private static Socket client;

    private static List<Travel> travels = new ArrayList();

    private Travel selected;

    private static String companyName;

    private final static List<ServerAddress> companyServers = Arrays.asList(
            new ServerAddress("localhost", 12240, "Azul"),
            new ServerAddress("localhost", 12241, "GOL"),
            new ServerAddress("localhost", 12242, "TAM")
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Preenche as cidades nos componentes de seleção.
        fillComboBox();

        lblCompanyName.setText(lblCompanyName.getText() + " " + companyName);
        
        cBoxDestination.setDisable(true);
        btnSearch.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (Travel) newValue;
                if (selected != null) {
                    MoreInfo alt = new MoreInfo(selected);
                    close(Interface.getStage());
                    try {
                        alt.start(new Stage());
                    } catch (Exception ex) {
                        System.err.println("Não foi possível exibir mais "
                                + "informações sobre o trajeto selecionado.");
                        System.out.println(ex);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setHeaderText("Selecione um voo!");
                    alert.show();
                }

            }

        });

        btnSearch.setOnMouseClicked((MouseEvent e) -> {
            loadRoutes();
        });

        btnSearchIcon.setOnMouseClicked((MouseEvent e) -> {
            loadRoutes();
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
    private void fillComboBox() {
        Reader reader = new Reader();

        for (String city : reader.readCities()) {
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
    private void removeOption(
            ComboBox<String> option,
            Object oldValue,
            Object newValue
    ) {
        option.getItems().remove((String) newValue);
        if (oldValue != null) {
            option.getItems().add((String) oldValue);
        }

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
            if (!companyName.equals("")) {
                for (ServerAddress serverAddress : companyServers) {
                    if (serverAddress.getCompanyName().equals(companyName)) {
                        client = new Socket(
                                serverAddress.getIpAddress(),
                                serverAddress.getPort()
                        );
                        System.out.println("Conexão estabelecida!");
                        break;
                    }
                }

            }

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
    private String search() {
        requestTravels();

        for (Travel travel : travels) {
            travel.setAttributes();
        }

        travels.sort(new Comparator<Travel>() {
            @Override
            public int compare(Travel travel1, Travel travel2) {
                return new Integer(travel1.getTotalParts()).compareTo(new Integer(travel2.getTotalParts()));
            }
        });

        initTable();

        return "Tabela montada com sucesso.";
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
            ObjectOutputStream output
                    = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            output.writeObject(new String("GET /routes"));

            ObjectOutputStream output2
                    = new ObjectOutputStream(client.getOutputStream());
            output2.flush();
            output2.writeObject(
                    cBoxOrigin.getValue() + "," + cBoxDestination.getValue()
            );

            //Recebendo a resposta do servidor.
            ObjectInputStream input
                    = new ObjectInputStream(client.getInputStream());

            travels = (List<Travel>) input.readObject();

            client.close();
        } catch (IOException ioe) {
            System.err.println("Erro ao requisitar a lista de possíveis "
                    + "trajetos para o servidor.");
            System.out.println(ioe);
            client = null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Classe String não foi encontrada.");
            System.out.println(cnfe);
        }
    }

    /**
     * Exibe as informações das rotas na janela.
     */
    private void loadRoutes() {
        setLoadingVisibity(true);
        table.setItems(null);

        Task loadRoutes = new Task() {

            @Override
            protected String call() throws Exception {
                return search();
            }

            @Override
            protected void succeeded() {
                setLoadingVisibity(false);
                if (travels.size() <= 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Atenção!");
                    alert.setHeaderText("Não foi possível encontrar rotas entre essas"
                            + " duas cidades.");
                    alert.show();
                }
            }
        };

        Thread t = new Thread(loadRoutes);
        t.setDaemon(true);
        t.start();
        if (t.isAlive()) {
            t.interrupt();
        }
    }

    /**
     * Muda a visibilidade dos elementos de carregamento.
     *
     * @param visibility boolean - Visibilidade
     */
    private void setLoadingVisibity(boolean visibility) {
        imgLoading.setVisible(visibility);
        txtRouteCalculate.setVisible(visibility);
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static void setCompanyName(String companyName) {
        InterfaceController.companyName = companyName;
    }

}
