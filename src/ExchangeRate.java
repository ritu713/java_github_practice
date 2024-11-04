import io.github.cdimascio.dotenv.Dotenv;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class ExchangeRate {
    Dotenv dotenv = Dotenv.configure().load();

    public double findExchangeRate(String currencyCodeOrigin, String currencyCodeDestination){
        String apiKey = dotenv.get("EXCHANGE_RATE_API_KEY");

        try{
            URL exchangeRateAPI = new URL(String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", apiKey, currencyCodeOrigin));
            HttpURLConnection connection = (HttpURLConnection) exchangeRateAPI.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader readResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = readResponse.readLine()) != null) {
                    content.append(inputLine);
                }

                readResponse.close();
                connection.disconnect();

                JSONObject json = new JSONObject(content.toString());

                double rate = json.getJSONObject("conversion_rates").getDouble(currencyCodeDestination);

                return rate;
            }
            else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }


}
