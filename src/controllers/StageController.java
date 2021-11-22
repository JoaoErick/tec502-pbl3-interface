package controllers;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class StageController {

    /**
     * Função para fechar a tela atual e abri uma nova.
     *
     * @param stage Stage
     * @param application Application
     */
    public void open(Stage stage, Application application) {
        this.close(stage);

        try {
            application.start(new Stage());
        } catch (Exception ex) {
            System.err.println("Erro ao fazer a troca de janelas.");
            System.out.println(ex);
        }
    }

    /**
     * Função para fechar a página atual.
     *
     * @param stage Stage
     */
    public void close(Stage stage) {
        stage.close();
    }

    /**
     * Função para voltar para a tela anterior.
     *
     * @param stage Stage
     * @param application Application
     */
    public void back(Stage stage, Application application) {
        this.close(stage);

        try {
            application.start(new Stage());
        } catch (Exception ex) {
            System.err.println("Erro ao voltar à janela anterior");
            System.out.println(ex);
        }
    }
}
