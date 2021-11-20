package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para a leitura do arquivo.
 * 
 * @author Allan Capistrano
 * @author João Erick Barbosa
 */
public class Reader {
    /*-------------------------- Constantes ----------------------------------*/
    private static final String FILE_NAME = "cities.txt";
    /*------------------------------------------------------------------------*/
    
    private File file;

    /**
     * Método construtor.
     */
    public Reader(){
        try {
            this.file = new File(FILE_NAME);
            file.createNewFile();
        } catch (IOException ioe) {
            System.err.println("Erro ao tentar criar o objeto \"file\".");
            System.out.println(ioe);
        }
    }

    /**
     * Realiza a leitura das cidades disponíveis para viagem.
     * 
     * @return List<String>
     */
    public List<String> readCities() {
        if(file.getName().compareTo(FILE_NAME) != 0) {
            return null;
        }
        
        List<String> cities  = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                cities.add(line);
            }
            br.close();
            
            return cities;
        } catch (IOException ioe) {
            System.err.println("Erro ao tentar ler o arquivo.");
            System.out.println(ioe);
            
            return null;
        }
    }
    
    public File getFile() {
        return file;
    }
}
