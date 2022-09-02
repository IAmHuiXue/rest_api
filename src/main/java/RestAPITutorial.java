import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestAPITutorial {
    public static void main(String[] args) throws Exception {
        String url = "https://api.assemblyai.com/v2/transcript";
        String audioUrl = "https://bit.ly/3yxKEIY";
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        Transcript transcript = new Transcript(audioUrl);
        String jsonRequest = gson.toJson(transcript);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("authorization", Credential.getAPI_KEY())
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/" + transcript.getId()))
                .header("authorization", Credential.getAPI_KEY())
                .GET()
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            System.out.println(transcript.getStatus());
            if (transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")) {
                break;
            }
            Thread.sleep(1000);
        }

        System.out.println("The transcript from the audio is: ");
        System.out.println("\"" + transcript.getText() + "\"");

    }
}
