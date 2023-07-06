package org.enissay.minefort;

import org.enissay.minefort.servers.ServerManager;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static String BASE_URL = "https://api.minefort.com/v1";
    public static String EMAIL = null;
    public static String PASSWORD = null;
    public static String COOKIE = null;

    public static void main(String[] args) {
        //FETCH SERVERS
        ServerManager.getServers().forEach(minefortServer -> {
            System.out.println(minefortServer);
        });

        //FIND PLAYER
        System.out.println(ServerManager.findPlayer("EnissayDev"));

        //TO GET COOKIE
        if (EMAIL != null && PASSWORD != null) {
            try {
                JSONObject jsObject = new JSONObject();
                jsObject.put("emailAddress", EMAIL);
                jsObject.put("password", PASSWORD);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(BASE_URL + "/auth/login"))
                        .headers("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsObject.toString()))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("AUTHENTIFICATION - RECEIVED: " + response.statusCode());

                if (response.statusCode() == 200)
                    COOKIE = response.headers().firstValue("set-cookie").get().split(";")[0];
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
