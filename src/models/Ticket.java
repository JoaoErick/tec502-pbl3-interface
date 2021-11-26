package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Passagem do avião.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Ticket implements Serializable {

    private List<Edge> listRoutes;
    private List<String> listCompanyNames;

    /**
     * Método construtor.
     *
     */
    public Ticket() {
        this.listRoutes = new ArrayList<>();
        this.listCompanyNames = new ArrayList<>();
    }

    public List<Edge> getListRoutes() {
        return listRoutes;
    }

    public List<String> getListCompanyNames() {
        return listCompanyNames;
    }
}
