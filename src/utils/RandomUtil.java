package utils;

import java.util.Random;

/**
 * Gerador de números aleatórios.
 *
 * @author Allan Capistrano
 */
public class RandomUtil {

    /**
     * Gera um número de ponto flutuante aleatório com base nos limites
     * passados.
     *
     * @param start float - Menor número que pode ser gerado.
     * @param end float - Limite do maior número que pode ser gerado.
     * @return float
     */
    public static float generateFloat(float start, float end) {
        Random randomFloat = new Random();

        return (float) randomFloat.doubles(start, end).findFirst().getAsDouble();
    }

    /**
     * Gera um número inteiro aleatório com base nos limites passados.
     *
     * @param start int - Menor número que pode ser gerado.
     * @param end int - Limite do maior número que pode ser gerado.
     * @return int
     */
    public static int generateInt(int start, int end) {
        Random randomInt = new Random();

        return randomInt.ints(start, end).findFirst().getAsInt();
    }
    
    /**
     * Gera um booleano de forma aleatória.
     * @return boolean
     */
    public static boolean generateBoolean() {
        return new Random().nextBoolean();
    }
}
