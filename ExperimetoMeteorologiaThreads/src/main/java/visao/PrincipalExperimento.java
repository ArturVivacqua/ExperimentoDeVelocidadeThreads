package visao;

import controle.ColetorDadosClimaticos;
import controle.CoordenadasCidade;
import controle.EstatisticasClima;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PrincipalExperimento {
    private static final int NUM_REPETICOES = 10;

    public static void main(String[] args) {
        CoordenadasCidade[] coordenadasCapitais = {
                new CoordenadasCidade("Aracaju", -10.9167, -37.05),
                new CoordenadasCidade("Belém", -1.4558, -48.5039),
                new CoordenadasCidade("Belo Horizonte", -19.9167, -43.9333),
                new CoordenadasCidade("Boa Vista", 2.81972, -60.67333),
                new CoordenadasCidade("Brasília", -15.7939, -47.8828),
                new CoordenadasCidade("Campo Grande", -20.44278, -54.64639),
                new CoordenadasCidade("Cuiabá", -15.5989, -56.0949),
                new CoordenadasCidade("Curitiba", -25.4297, -49.2711),
                new CoordenadasCidade("Florianópolis", -27.5935, -48.55854),
                new CoordenadasCidade("Fortaleza", -3.7275, -38.5275),
                new CoordenadasCidade("Goiânia", -16.6667, -49.25),
                new CoordenadasCidade("João Pessoa", -7.12, -34.88),
                new CoordenadasCidade("Macapá", 0.033, -51.05),
                new CoordenadasCidade("Maceió", -9.66583, -35.73528),
                new CoordenadasCidade("Manaus", -3.1189, -60.0217),
                new CoordenadasCidade("Natal", -5.7833, -35.2),
                new CoordenadasCidade("Palmas", -10.16745, -48.32766),
                new CoordenadasCidade("Porto Alegre", -30.0331, -51.23),
                new CoordenadasCidade("Porto Velho", -8.76194, -63.90389),
                new CoordenadasCidade("Recife", -8.05, -34.9),
                new CoordenadasCidade("Rio Branco", -9.97472, -67.81),
                new CoordenadasCidade("Rio de Janeiro", -22.9111, -43.2056),
                new CoordenadasCidade("Salvador", -12.9747, -38.4767),
                new CoordenadasCidade("São Luís", -2.5283, -44.3044),
                new CoordenadasCidade("São Paulo", -23.55, -46.6333),
                new CoordenadasCidade("Teresina", -5.08917, -42.80194),
                new CoordenadasCidade("Vitória", -20.2889, -40.3083)
        };

        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        ExecutorService executor3 = Executors.newFixedThreadPool(3);
        ExecutorService executor9 = Executors.newFixedThreadPool(9);
        ExecutorService executor27 = Executors.newFixedThreadPool(27);

        try {
            List<Long> duracoes = new ArrayList<>();

            duracoes.add(executarExperimento(executor1, "1 Thread", coordenadasCapitais));
            duracoes.add(executarExperimento(executor3, "3 Threads", coordenadasCapitais));
            duracoes.add(executarExperimento(executor9, "9 Threads", coordenadasCapitais));
            duracoes.add(executarExperimento(executor27, "27 Threads", coordenadasCapitais));

            System.out.println("\nResumo de Tempo de Execução dos Experimentos:");
            System.out.println("Experimento 1 (1 Thread): " + duracoes.get(0) + " milissegundos");
            System.out.println("Experimento 2 (3 Threads): " + duracoes.get(1) + " milissegundos");
            System.out.println("Experimento 3 (9 Threads): " + duracoes.get(2) + " milissegundos");
            System.out.println("Experimento 4 (27 Threads):" + duracoes.get(3) + " milissegundos");

        } finally {
            executor1.shutdown();
            executor3.shutdown();
            executor9.shutdown();
            executor27.shutdown();
        }
    }

    private static long executarExperimento(ExecutorService executor, String infoThread, CoordenadasCidade[] coordenadasCapitais) {
        List<Double> temperaturas = new ArrayList<>();
        long inicioExperimento = System.currentTimeMillis();

        for (int i = 0; i < NUM_REPETICOES; i++) {
            List<Future<List<Double>>> futuros = new ArrayList<>();

            int numThreads = executor instanceof ThreadPoolExecutor? ((ThreadPoolExecutor) executor).getCorePoolSize() : 1;
            int tamanhoLista = coordenadasCapitais.length;
            int tamanhoParticao = tamanhoLista / numThreads;

            for (int j = 0; j < numThreads; j++) {
                int indiceInicio = j * tamanhoParticao;
                int indiceFim = (j == numThreads - 1)? tamanhoLista : indiceInicio + tamanhoParticao;
                ColetorDadosClimaticos tarefa = new ColetorDadosClimaticos(indiceInicio, indiceFim, coordenadasCapitais);
                Future<List<Double>> futuro = executor.submit(tarefa);
                futuros.add(futuro);
            }

            for (Future<List<Double>> futuro : futuros) {
                try {
                    List<Double> temperaturasThread = futuro.get();
                    temperaturas.addAll(temperaturasThread);

                    System.out.println("Experimento: " + infoThread + " | Rodada " + (i + 1));
                    EstatisticasClima.calcularEstatisticas(temperaturasThread);

                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Erro ao executar thread: " + e.getMessage());
                }
            }
        }

        long fimExperimento = System.currentTimeMillis();
        long duracaoExperimento = fimExperimento - inicioExperimento;
        return duracaoExperimento;
    }
}