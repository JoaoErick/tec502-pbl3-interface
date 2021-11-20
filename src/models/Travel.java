package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Travel implements Serializable {
    private LinkedList<List<Edge>> route;

    public Travel() {
        this.route = new LinkedList<>();
    }

    public LinkedList<List<Edge>> getRoute() {
        return route;
    }
    
}
