package controle;

import java.util.List;

public class EstatisticasClima {
    public static void calcularEstatisticas(List<Double> temperaturas) {
        if (temperaturas.isEmpty()) {
            System.out.println("Nenhuma temperatura disponível para cálculo.");
            return;
        }

        double tempMin = Double.MAX_VALUE;
        double tempMax = Double.MIN_VALUE;
        double soma = 0.0;

        for (double temp : temperaturas) {
            if (temp < tempMin) {
                tempMin = temp;
            }
            if (temp > tempMax) {
                tempMax = temp;
            }
            soma += temp;
        }

        double tempMedia = soma / temperaturas.size();

        System.out.println("Temperatura Média: " + tempMedia);
        System.out.println("Temperatura Mínima: " + tempMin);
        System.out.println("Temperatura Máxima: " + tempMax);
        System.out.println();
    }
}