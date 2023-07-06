package org.enissay.minefort.servers;

import org.enissay.minefort.Main;
import org.enissay.minefort.utils.UUIDFetcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ServerManager {

    public static boolean isServerNameAvailable(final String name) {
        JSONObject jsObject = new JSONObject();
        jsObject.put("serverName", name);

        AtomicReference<Boolean> responseServer = new AtomicReference<>(false);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Main.BASE_URL + "/server/availability"))
                    .headers("Content-Type", "application/json", "Cookie", Main.COOKIE)
                    .POST(HttpRequest.BodyPublishers.ofString(jsObject.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 200:
                    JSONObject result = new JSONObject(response.body());
                    responseServer.set(result.getBoolean("result"));
                    break;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return responseServer.get();
    }

    public static List<MinefortServer> getServers() {
        JSONObject jsObject = new JSONObject();

        final Map<String, Integer> map = new HashMap<>();
        final Map<String, String> map2 = new HashMap<>();

        map.put("skip", 0);
        map.put("limit", 500);
        map2.put("field", "players.online");
        map2.put("order", "desc");
        jsObject.put("pagination", map);
        jsObject.put("sort", map2);

        final LinkedList<MinefortServer> servers = new LinkedList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Main.BASE_URL + "/servers/list"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsObject.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200:
                    JSONObject result = new JSONObject(response.body());
                    Arrays.asList(result.getJSONArray("result")).forEach(server -> {
                        for (int i = 0; i < result.getJSONArray("result").length(); i++) {
                            final JSONObject stats = server.getJSONObject(i);
                            final JSONObject players = stats.getJSONObject("players");
                            final JSONArray playersList = players.getJSONArray("list");
                            final List<String> uuidsList = new ArrayList<>();
                            for (int e = 0; e < playersList.length(); e++) {
                                uuidsList.add(playersList.getJSONObject(e).getString("uuid"));
                            }
                            String[] uuids = new String[uuidsList.size()];
                            uuidsList.toArray(uuids);

                            //serverName - serverId - userId - version - state - MOTD - players
                            final MinefortServer minefortServer = new MinefortServer(stats.getString("serverName"), stats.getString("serverId"), stats.getString("userId"), stats.getString("version"), stats.getInt("state"), stats.getString("messageOfTheDay"),
                                    new MinefortPlayers(players.getInt("online"), uuids, players.getInt("max")));
                            servers.add(minefortServer);
                        }
                    });
                    break;
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return servers;
    }

    public static MinefortServer getServer(final String name) {
        return getServers().stream().filter(server -> server.getServerName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static List<String> getPlayers(final String name) {
        return Arrays.asList(getServer(name).getPlayers().getUuids());
    }

    public static String findPlayer(final String name) {
        AtomicReference<String> responseServer = new AtomicReference<>("Not found!");

        String playerToFind = name;
        System.out.println("Finding UUID...");
        final String UUID = UUIDFetcher.getUUID(playerToFind) != null ? UUIDFetcher.getUUID(playerToFind).toString() : null;
        System.out.println("Fetching Servers...");
        getServers().forEach(minefortServer -> {
            if (UUID != null && Arrays.stream(minefortServer.getPlayers().getUuids()).collect(Collectors.toList()).contains(UUID))
                responseServer.set(minefortServer.getServerName());
        });

        System.out.println("Player to find: " + playerToFind);
        System.out.println("Response -> " + responseServer.get());

        return responseServer.get();
    }
}
