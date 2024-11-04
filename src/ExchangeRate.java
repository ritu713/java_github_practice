import io.github.cdimascio.dotenv.Dotenv;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

public class ExchangeRate {
    Set<String> currencyCodes = new HashSet<>();
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

    public void call(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        init();
        try{
            System.out.println("Welcome to exchange rate! \nPlease enter the currency code of the first country");
            String a = reader.readLine().trim().toUpperCase();
            if(!currencyCodes.contains(a)){
                throw new Error("Invalid currency code");
            }

            System.out.println("Great! Please enter the currency code of the second country");
            String b = reader.readLine().trim().toUpperCase();
            if(!currencyCodes.contains(b)){
                throw new Error("Invalid currency code");
            }

            double aToB = findExchangeRate(a, b);
            double bToA = 1.0d/aToB;

            System.out.format("%s to %s : %,.2f", a, b, aToB);
            System.out.format("%s to %s : %,.2f", b, a, bToA);

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void init(){
        // Add currency codes to the HashSet
        currencyCodes.add("USD"); // United States Dollar
        currencyCodes.add("EUR"); // Euro
        currencyCodes.add("JPY"); // Japanese Yen
        currencyCodes.add("GBP"); // British Pound Sterling
        currencyCodes.add("AUD"); // Australian Dollar
        currencyCodes.add("CAD"); // Canadian Dollar
        currencyCodes.add("CHF"); // Swiss Franc
        currencyCodes.add("CNY"); // Chinese Yuan
        currencyCodes.add("SEK"); // Swedish Krona
        currencyCodes.add("NZD"); // New Zealand Dollar
        currencyCodes.add("MXN"); // Mexican Peso
        currencyCodes.add("SGD"); // Singapore Dollar
        currencyCodes.add("HKD"); // Hong Kong Dollar
        currencyCodes.add("NOK"); // Norwegian Krone
        currencyCodes.add("KRW"); // South Korean Won
        currencyCodes.add("TRY"); // Turkish Lira
        currencyCodes.add("INR"); // Indian Rupee
        currencyCodes.add("RUB"); // Russian Ruble
        currencyCodes.add("BRL"); // Brazilian Real
        currencyCodes.add("ZAR"); // South African Rand
        currencyCodes.add("DKK"); // Danish Krone
        currencyCodes.add("PLN"); // Polish Zloty
        currencyCodes.add("THB"); // Thai Baht
        currencyCodes.add("IDR"); // Indonesian Rupiah
        currencyCodes.add("HUF"); // Hungarian Forint
        currencyCodes.add("CZK"); // Czech Koruna
        currencyCodes.add("ILS"); // Israeli New Shekel
        currencyCodes.add("CLP"); // Chilean Peso
        currencyCodes.add("PHP"); // Philippine Peso
        currencyCodes.add("AED"); // United Arab Emirates Dirham
        currencyCodes.add("COP"); // Colombian Peso
        currencyCodes.add("SAR"); // Saudi Riyal
        currencyCodes.add("MYR"); // Malaysian Ringgit
        currencyCodes.add("RON"); // Romanian Leu
    }

}
