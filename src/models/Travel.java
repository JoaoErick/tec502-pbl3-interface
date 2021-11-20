package models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Trajeto entre a cidade de origem e a cidade de destino.
 * 
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Travel implements Serializable {
    private LinkedList<List<Edge>> route;
    private int totalParts;
    private String cities;

    public Travel() {
        this.route = new LinkedList<>();
    }

    public LinkedList<List<Edge>> getRoute() {
        return route;
    }
    
    public void format(){
        this.totalParts = this.route.size();
        formatCities();
    }
    
    private void formatCities(){
        this.cities = "";
        for (int j = 0; j < this.route.size(); j++) {
            this.cities += this.route.get(j).get(0).getFirstCity().getCityName() + " -> ";
            if(j == this.route.size() - 1){
                this.cities += this.route.get(j).get(0).getSecondCity().getCityName();
            }
        }
    }

    public int getTotalParts() {
        return totalParts;
    }

    public String getCities() {
        return cities;
    }
    
    
    
}
