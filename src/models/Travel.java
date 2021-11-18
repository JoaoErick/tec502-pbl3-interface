package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Travel {
    private List<List<Edge>> route;

    public Travel() {
        this.route = new ArrayList<>();
    }

    public List<List<Edge>> getRoute() {
        return route;
    }
    
}
