import lombok.Data;

@Data
public class Transcript {
    private String audio_url;
    private String id;
    private String text;
    private String status;

    Transcript(String url) {
        audio_url = url;
    }
}
