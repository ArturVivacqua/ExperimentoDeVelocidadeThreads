package controle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ColetorDadosClimaticos implements Callable<List<Double>> {
    private static final String URL_BASE_API = "https://api.open-meteo.com/v1/forecast";
    private int indiceInicio;
    private int indiceFim;
    private CoordenadasCidade[] coordenadasCapitais;

    public ColetorDadosClimaticos(int indiceInicio, int indiceFim, CoordenadasCidade[] coordenadasCapitais) {
        this.indiceInicio = indiceInicio;
        this.indiceFim = indiceFim;
        this.coordenadasCapitais = coordenadasCapitais;
    }

    @Override
    public List<Double> call() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        List<Double> temperaturas = new ArrayList<>();

        for (int i = indiceInicio; i < indiceFim; i++) {
            CoordenadasCidade cidade = coordenadasCapitais[i];
            String urlApi = URL_BASE_API + "?latitude=" + cidade.getLatitude() + "&longitude=" + cidade.getLongitude();
            HttpGet requisicao = new HttpGet(urlApi);

            try {
                HttpResponse resposta = httpClient.execute(requisicao);
                String corpoResposta = EntityUtils.toString(resposta.getEntity());
          
                double temperatura = Math.random() * 40; 
                temperaturas.add(temperatura);
            } catch (IOException e) {
                System.err.println("Erro ao buscar dados para " + cidade.getCidade() + ": " + e.getMessage());
            }
        }

        return temperaturas;
    }
}