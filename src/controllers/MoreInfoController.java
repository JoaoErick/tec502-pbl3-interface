package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.Interface;
import main.MoreInfo;
import models.Ticket;
import models.Edge;
import models.Travel;

/**
 * Controlador da tela de mais informações.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class MoreInfoController extends StageController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnBack;

    @FXML
    private ImageView btnArrowBack;

    @FXML
    private Label lblParts;

    @FXML
    private Label lblTimeTravel;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Button btnBuy;

    @FXML
    private Text txtRouteTitle;

    @FXML
    private ImageView imgLoading;

    private static Travel travel;

    private static Socket client;

    private List<String> comboBoxes = new ArrayList<>();

    private boolean purchaseStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillFields();

        createRoute();

        setComboBoxEvent();

        btnBack.setOnMouseClicked((MouseEvent e) -> {
            back(MoreInfo.getStage(), new Interface());
        });

        btnBack.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                back(MoreInfo.getStage(), new Interface());
            }
        });

        btnArrowBack.setOnMouseClicked((MouseEvent e) -> {
            back(MoreInfo.getStage(), new Interface());
        });

        btnArrowBack.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                back(MoreInfo.getStage(), new Interface());
            }
        });

        btnBuy.setOnKeyPressed((KeyEvent e) -> {
            this.waitPurchase();
        });

        btnBuy.setOnMouseClicked((MouseEvent e) -> {
            this.waitPurchase();
        });
    }

    /**
     * Preenche os campos da quantidade de escalas, preço total e tempo total de
     * voo.
     */
    private void fillFields() {
        lblParts.setText((travel.getTotalParts() != 0 ? String.valueOf(travel.getTotalParts()) : "Sem escala"));
        lblTimeTravel.setText(String.format("%.2f", travel.getTotalTime()) + " Horas");
        lblTotalPrice.setText("R$ " + String.format("%.2f", travel.getTotalPrice()));
    }

    /**
     * Preenche o campo de trajeto com os trechos do voo e as caixa de seleção
     * com as companhias que operam em cada trecho.
     */
    private void createRoute() {
        LinkedList<List<Edge>> route = new LinkedList<>();

        route.addAll(travel.getRoute());
        double posY = txtRouteTitle.getLayoutY();

        for (int j = 0; j < route.size(); j++) {
            Label lblPart = new Label(
                    route.get(j).get(0).getFirstCity().getCityName()
                    + " -> "
                    + route.get(j).get(0).getSecondCity().getCityName());
            lblPart.setLayoutX(txtRouteTitle.getLayoutX());
            lblPart.setFont(Font.font("System", FontWeight.BOLD, 16));
            posY += (j != 0) ? 30 : 5;
            lblPart.setLayoutY(posY);

            String airlines[] = new String[route.get(j).size()];

            for (int i = 0; i < route.get(j).size(); i++) {
                airlines[i] = route.get(j).get(i).getCompanyName();
            }

            ComboBox combo_box
                    = new ComboBox(FXCollections
                            .observableArrayList(airlines));

            combo_box.setLayoutX(lblPart.getLayoutX() + 200);
            combo_box.setLayoutY(lblPart.getLayoutY());
            combo_box.setValue(airlines[0]);

            if (j < 10) {
                combo_box.setId("cb0" + j);
            } else {
                combo_box.setId("cb" + j);
            }

            comboBoxes.add((String) combo_box.getValue());

            anchorPane.getChildren().addAll(lblPart, combo_box);
        }
    }

    /**
     * Realiza a requisição de compra para o servidor da companhia da interface.
     */
    private String buy() {
        try {
            client = new Socket(
                    InterfaceController.address.getIpAddress(),
                    InterfaceController.address.getPort()
            );

            System.out.println("Conexão estabelecida!");

            Ticket ticket = new Ticket();

            /* Adicionando os trechos da viagem na passagem. */
            for (int i = 0; i < travel.getRoute().size(); i++) {
                ticket.getListRoutes().add(travel.getRoute().get(i).get(0));
            }

            /* Adicionando os nomes das companhias na passagem. */
            ticket.getListCompanyNames().addAll(comboBoxes);

            ObjectOutputStream output
                    = new ObjectOutputStream(client.getOutputStream());

            output.flush();
            output.writeObject("POST /buy");
            output.flush();

            ObjectOutputStream outputBody
                    = new ObjectOutputStream(client.getOutputStream());

            outputBody.flush();
            outputBody.writeObject(ticket);
            outputBody.flush();

            ObjectInputStream input = new ObjectInputStream(client.getInputStream());

            purchaseStatus = (boolean) input.readObject();

            output.close();
            outputBody.close();
            client.close();
        } catch (IOException ioe) {
            System.err.println("Erro ao tentar comprar a passagem!");
            System.out.println(ioe);
            try {
                client.close();
            } catch (IOException ioex) {
                System.err.println("Não foi possível encerrar a conexão com o "
                        + "servidor de maneira segura.");
                System.out.println(ioex);
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("O tipo boolean não foi encontrado.");
            System.out.println(cnfe);
        }

        return "";
    }

    /**
     * Recalcula o preço total e o tempo total de viagem a depender das
     * companhias escolhidas para cada trecho.
     */
    private void setComboBoxEvent() {
        for (int i = 0; i < anchorPane.getChildren().size(); i++) {
            if (anchorPane.getChildren().get(i).getId() != null
                    && anchorPane.getChildren().get(i).getId().contains("cb")) {
                ((ComboBox) anchorPane.getChildren().get(i)).valueProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        String id = observable.toString().substring(36, 38);
                        comboBoxes.remove(Integer.parseInt(id));
                        comboBoxes.add(Integer.parseInt(id), (String) newValue);
                        travel.calculate(comboBoxes);
                        fillFields();
                    }
                });
            }
        }
    }

    /**
     * Espera ser realizada a compra da passagem, e toma uma ação a depender da 
     * resposta do servidor.
     */
    private void waitPurchase() {
        Task loadRoutes = new Task() {

            @Override
            protected String call() throws Exception {
                handleButtons(true);

                Platform.runLater(() -> {
                    btnBuy.setText("");
                });

                return buy();
            }

            @Override
            protected void succeeded() {
                Alert alert;

                handleButtons(false);

                Platform.runLater(() -> {
                    btnBuy.setText("Comprar");
                });

                if (purchaseStatus) { /* Caso a compra seja efetuada. */
                    alert = new Alert(Alert.AlertType.CONFIRMATION);

                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Compra da passagem realizada com "
                            + "sucesso!");

                } else { /* Caso a compra não seja efetuada. */
                    alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Ops");
                    alert.setHeaderText("Não foi possível realizar a compra da "
                            + "passagem!");
                }

                alert.show();
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
     * Lida com os botões da interface.
     *
     * @param status boolean - Estado do botão.
     */
    private synchronized void handleButtons(boolean status) {
        btnArrowBack.setDisable(status);
        btnBack.setDisable(status);
        btnBuy.setDisable(status);

        imgLoading.setVisible(status);
    }

    public static Travel getTravel() {
        return travel;
    }

    public static void setTravel(Travel travel) {
        MoreInfoController.travel = travel;
    }

}
