package client;

import manager.ManagerLoadException;
import manager.ManagerSaveException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private static final String URN_REGISTER = "/register";
    private String url;
    private String apiToken;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port;
        apiToken = register(url);
    }
    private String register(String url){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + URN_REGISTER))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                throw  new ManagerSaveException();
            }
            return response.body();
        }catch(Exception e){
            e.printStackTrace();
            throw new ManagerSaveException();

        }

    }

    public String load(String key){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "/load"))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                throw  new ManagerSaveException();
            }
            return response.body();
        }catch(Exception e){
            e.printStackTrace();
            throw new ManagerSaveException();

        }
    }


    public String put(String key, String json){
        return"";
    }
}
