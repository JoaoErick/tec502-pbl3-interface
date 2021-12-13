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

    /*-------------------------- Constante -----------------------------------*/
    private static final float BASE_VALUE = 3200;
    /*------------------------------------------------------------------------*/

    private LinkedList<List<Edge>> route;
    private int totalParts;
    private String cities;
    private float totalPrice;
    private double totalTime;
    private float totalCompanyTariff;

    /**
     * Método construtor.
     */
    public Travel() {
        this.route = new LinkedList<>();
    }

    /**
     * Inicializa atributos: totalParts, cities, totalCompanyTariff, totalPrice
     * e totalTime.
     */
    public void setAttributes() {
        this.totalParts = this.route.size() - 1;
        formatCities();
        calculate();
    }

    /**
     * Formata o atributo cities para exibição.
     */
    private void formatCities() {
        this.cities = "";
        for (int j = 0; j < this.route.size(); j++) {
            this.cities += this.route.get(j).get(0).getFirstCity().getCityName() + " -> ";
            if (j == this.route.size() - 1) {
                this.cities += this.route.get(j).get(0).getSecondCity().getCityName();
            }
        }
    }

    /**
     * Calcula o valor total da tarifa da viagem, tempo total de voo e o preço
     * total.
     */
    private void calculate() {
        this.totalCompanyTariff = 0;
        this.totalTime = 0;

        for (int j = 0; j < this.route.size(); j++) {
            this.totalTime += this.route.get(j).get(0).getTimeTravel();
            this.totalCompanyTariff += this.route.get(j).get(0).getCompanyTariff();
        }

        this.totalPrice = (float) (BASE_VALUE / this.totalTime) + this.totalCompanyTariff;
    }

    /**
     * Calcula o valor total da tarifa da viagem, tempo total de voo e o preço
     * total através de opções de trechos específicas.
     *
     * @param indexes List<Integer> - Lista de trechos específicos.
     */
    public void calculate(List<String> indexes) {
        this.totalCompanyTariff = 0;
        this.totalTime = 0;
        for (int j = 0; j < this.route.size(); j++) {
            for (int i = 0; i < this.route.get(j).size(); i++) {
                if (this.route.get(j).get(i).getCompanyName().equals(indexes.get(j))) {
                    this.totalTime += this.route.get(j).get(i).getTimeTravel();
                    this.totalCompanyTariff += this.route.get(j).get(i).getCompanyTariff();
                }
            }
        }

        this.totalPrice = (float) (BASE_VALUE / this.totalTime) + this.totalCompanyTariff;
    }

    public LinkedList<List<Edge>> getRoute() {
        return route;
    }

    public int getTotalParts() {
        return totalParts;
    }

    public String getCities() {
        return cities;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public double getTotalTime() {
        return totalTime;
    }

}
