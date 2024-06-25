package controle;

public class CoordenadasCidade {
    private String cidade;
    private double latitude;
    private double longitude;

    public CoordenadasCidade(String cidade, double latitude, double longitude) {
        this.cidade = cidade;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCidade() {
        return cidade;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
