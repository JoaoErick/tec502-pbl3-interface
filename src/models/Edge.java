package models;

import java.io.Serializable;
import java.util.Objects;
import utils.RandomUtil;

/**
 * Classe da aresta.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Edge implements Serializable{

    /*-------------------------- Constante -----------------------------------*/
    private static final float BASE_VALUE = 3200;
    /*------------------------------------------------------------------------*/

    private Vertex firstCity;
    private Vertex secondCity;
    private double timeTravel;
    private float price;
    private String companyName;
    private float companyTariff;
    private int amountSeat;

    /**
     * Método construtor.
     *
     * @param firstCity Vertex - Primeira cidade.
     * @param secondCity Vertex - Segunda cidade.
     * @param timeTravel double - Tempo de duração da viagem em horas.
     * @param companyName String - Nome da companhia.
     * @param amountSeat int - Quantidade de acentos.
     */
    public Edge(
            Vertex firstCity,
            Vertex secondCity,
            double timeTravel,
            String companyName,
            int amountSeat
    ) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.timeTravel = timeTravel;
        this.companyName = companyName;

        this.companyTariff
                = RandomUtil.generateFloat((float) 1.05, (float) 1.21);

        this.price = (float) (BASE_VALUE / this.timeTravel) * companyTariff;
    }

    /**
     * Método construtor.
     *
     * @param firstCity Vertex - Primeira cidade.
     * @param secondCity Vertex - Segunda cidade.
     */
    public Edge(String firstCity, String secondCity) {
        this.firstCity = new Vertex(firstCity);
        this.secondCity = new Vertex(secondCity);
    }

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

    public int getAmountSeat() {
        return amountSeat;
    }

    public void setAmountSeat(int amountSeat) {
        this.amountSeat = amountSeat;
    }
}
