package pdfconverter.Service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {
    @Value("${GROQ_API_KEY}")
    private String apiKey;
    private final WebClient webClient=WebClient.builder().build();
    public String convertDocument(String pdfText){
        String prompt= """
                Analyze the following PDF content.

                Return ONLY JSON.

                {
                  "documentType":"",
                  "title":"",
                  "authors":"",
                  "summary":"",
                  "keyTakeaway":""
                }

                PDF Content:
                """
                + pdfText.substring(0,
                Math.min(pdfText.length(),3000)
        );
        Map<String,Object> requestBody=Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages",
                List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.2
        );
        String response=webClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root=mapper.readTree(response);
            String content =
                    root.get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText();
            content = content
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();
            return content;
        }catch (Exception e){
            throw new RuntimeException(
                    "Failed to parse Groq response",
                    e
            );
        }
    }
}
