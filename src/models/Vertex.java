package models;

import java.util.Objects;

/**
 * Classe do vértice.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Vertex {

    private String cityName;

    /**
     * Método construtor.
     * 
     */
    public Vertex() {}

    /**
     * Compara se dois vértices são iguais pelo nome da cidade.
     *
     * @param o Object - Vértice que se deseja comparar.
     * @return boolean.
     */
    @Override
    public boolean equals(Object o) {
        return this.cityName.equals(((Vertex) o).getCityName());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.cityName);
        return hash;
    }
    
    public String getCityName() {
        return cityName;
    }
}
