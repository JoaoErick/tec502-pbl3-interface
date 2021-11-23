package models;

/**
 * Responsável pelas informações de endereço do servidor.
 *
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class ServerAddress {

    private final String ipAddress;
    private final int port;
    private final String companyName;

    /**
     * Método construtor.
     *
     * @param ipAddress String - Endereço ip.
     * @param port int - Porta.
     * @param companyName String - Nome da companhia.
     */
    public ServerAddress(String ipAddress, int port, String companyName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.companyName = companyName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getCompanyName() {
        return companyName;
    }
}
