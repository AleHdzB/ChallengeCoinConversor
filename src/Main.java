import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.Gson;

// Clase para mapear la respuesta de la API
class ConversionResponse {
    private String result;
    private String documentation;
    private String terms_of_use;
    private long time_last_update_unix;
    private String time_last_update_utc;
    private String base_code;
    private String target_code;
    private double conversion_rate;
    private double conversion_result; // algunos endpoints devuelven este valor

    public String getResult() { return result; }
    public String getBase_code() { return base_code; }
    public String getTarget_code() { return target_code; }
    public double getConversion_rate() { return conversion_rate; }
    public double getConversion_result() { return conversion_result; }
}

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String menu = """
                1) Dolar(USD) => Peso Argentino(ARS)
                2) Peso Argentino(ARS) => Dolar(USD)
                3) Dolar(USD) => Real Brasil(BRL)
                4) Real Brasil(BRL) => Dolar(USD)
                5) Dolar(USD) => Peso Colombiano(COP)
                6) Peso Colombiano(COP) => Dolar(USD)
                7) Salir

                >> Elija una opcion de conversion VALIDA:
                """;
        int opc;
        String base = "USD";
        String target = "MXN";
        Gson gson = new Gson();

        do {
            System.out.println(menu);
            opc = scanner.nextInt();
            switch (opc) {
                case 1: base = "USD"; target = "ARS"; break;
                case 2: base = "ARS"; target = "USD"; break;
                case 3: base = "USD"; target = "BRL"; break;
                case 4: base = "BRL"; target = "USD"; break;
                case 5: base = "USD"; target = "COP"; break;
                case 6: base = "COP"; target = "USD"; break;
                case 7: System.out.println("Saliendo..."); continue;
                default:
                    System.out.println("Opcion no valida");
                    continue;
            }

            System.out.println("Digite el valor a convertir: ");
            double valor = scanner.nextDouble();

            String apiKey = System.getenv("API_KEY");
            if (apiKey == null) {
                throw new IllegalStateException("⚠️ API_KEY no configurada. Ejecuta: setx API_KEY YOUR_API_KEY");
            }

            String URL = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + base + "/" + target + "/" + valor;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear JSON usando Gson
            ConversionResponse conversion = gson.fromJson(response.body(), ConversionResponse.class);

            // Mostrar resultado formateado
            System.out.printf("Resultado: %.2f %s -> %.2f %s%n",
                    valor, conversion.getBase_code(),
                    conversion.getConversion_result(),
                    conversion.getTarget_code());

        } while (opc != 7);
    }
}
