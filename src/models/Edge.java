package models;

import java.util.Objects;

/**
 * Classe da aresta.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Edge {

    private Vertex firstCity;
    private Vertex secondCity;
    private double timeTravel;
    private float price;
    private String companyName;
    private float companyTariff;

    /**
     * Método construtor.
     *
     * 
     */
    public Edge() {}

    /**
     * Compara se duas arestas são iguais pelos vértices que compõem a mesma.
     *
     * @param o Object - Aresta que se deseja comparar.
     * @return boolean.
     */
    @Override
    public boolean equals(Object o) {
        return this.firstCity.equals(((Edge) o).getFirstCity())
                && this.secondCity.equals(((Edge) o).getSecondCity());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.firstCity);
        hash = 71 * hash + Objects.hashCode(this.secondCity);
        return hash;
    }

    public Vertex getFirstCity() {
        return firstCity;
    }

    public Vertex getSecondCity() {
        return secondCity;
    }

    public double getTimeTravel() {
        return timeTravel;
    }

    public float getPrice() {
        return price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getCompanyTariff() {
        return companyTariff;
    }
}
