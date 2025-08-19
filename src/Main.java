import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String menu = """
                1) Dolar(USD) => Peso Argentino(ARS).
                2) Peso Argentino (ARS) => Dolar(USD).
                3) Dolar(USD) => Real Brasil(BRL).
                4) Real Brasil (BRS) => Dolar(USD).
                5) Dolar(USD) => Peso Colombiano(COP).
                6) Peso Colombiano (COP) => Dolar(USD).
                7) Salir.
                
                >> Elija una opcion de convercion VALIDA:
                """;
        int opc;
        String base =  "USD";
        String target = "MXN";
        do {
            System.out.println(menu);
            opc = scanner.nextInt();
            switch (opc) {
                case 1:
                    base = "USD";
                    target = "ARS";
                    break;
                case 2:
                    base = "ARS";
                    target = "USD";
                    break;
                case 3:
                    base = "USD";
                    target = "BRL";
                    break;
                case 4:
                    base = "BRL";
                    target = "USD";
                    break;
                case 5:
                    base = "USD";
                    target = "COP";
                    break;
                case 6:
                    base = "COP";
                    target = "USD";
                    break;

                default:
                    System.out.println("Opcion no Valida");
                    break;
            }
            System.out.println("Digite el valor a convertir: ");
            var valor = scanner.nextDouble();
            String apiKey = System.getenv("API_KEY");
            if (apiKey == null) {
                throw new IllegalStateException("⚠️ API_KEY no configurada en las variables de entorno. Run: setx API_KEY YOUR_API_KEY");
            }

            String URL = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + base + "/" + target + "/" + valor;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        } while (opc != 7);
//        System.out.println("Elija una opcion de convercion VALIDA: ");

    }
}
