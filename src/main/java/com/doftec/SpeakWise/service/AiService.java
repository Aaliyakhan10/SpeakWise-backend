package com.doftec.SpeakWise.service;

import com.doftec.SpeakWise.dto.AiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiService {

    private final RestClient restClient;

    public AiService(RestClient restClientAi) {
        this.restClient = restClientAi;
    }
      public AiResponse callAi(String SpeechText){
          String prompt = buildDetailedAIPrompt(SpeechText);

          Map<String, Object> message = Map.of(
                  "role", "user",
                  "content", prompt
          );

          Map<String, Object> requestBody = Map.of(
                  "model", "qwen/qwen2.5-vl-32b-instruct:free",
                  "messages", List.of(message),
                  "temperature", 0.9
          );

          try {
              // get actual response body string by blocking on Mono
              String responseBody = restClient.post()
                      .uri("/chat/completions")
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(requestBody)
                      .retrieve()
                      .body(String.class);

              if (responseBody == null) {
                  return new AiResponse();
              }
              System.out.println("API response: " + responseBody);

              ObjectMapper mapper = new ObjectMapper();
              JsonNode root = mapper.readTree(responseBody);
              String content = root.path("choices").get(0).path("message").path("content").asText();

              // Extract JSON inside ```json ... ```
              Pattern pattern = Pattern.compile("```json\\s*(\\{.*?\\})\\s*```", Pattern.DOTALL);
              Matcher matcher = pattern.matcher(content);

              if (matcher.find()) {
                  String jsonSnippet = matcher.group(1);

                  JsonNode jsonNode = mapper.readTree(jsonSnippet);

            AiResponse response = mapper.treeToValue(jsonNode, AiResponse.class);
                  return response;
              } else {
                  // fallback if no embedded JSON
                  return new AiResponse();
              }

          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
      }

    private String buildDetailedAIPrompt(String speechText) {
       return """  
               Analyze the following **speech-to-text transcript**. Your task is to assess both the **content** and the **quality of speech**. Return a JSON object with the following:
               >
               > 1. **Sentiment**: Overall sentiment (Positive, Neutral, Negative).
               > 2. **Emotion**: Dominant emotion expressed.
               > 3. **Intent**: User’s likely intent (e.g. Complaint, Request, Statement, Question, Suggestion).
               > 4. **Topics**: Main subjects mentioned.
               > 5. **Summary**: A 1–2 sentence summary of the speech.
               > 6. **GrammarMistakes**: Any grammar mistakes found, along with suggestions.
               > 7. **FillerWords**: List of filler words or disfluencies used (e.g. "um", "uh", "like").
               > 8. **SpeechPaceEstimate**: Estimated speech pace based on sentence structure and filler word frequency. Options: "Slow", "Normal", "Fast".
               > 9. **Clarity**: Was the speech clear and easy to follow? (Yes/No with optional short reason)
               >
               > Format your response as JSON:
               >
               > ```json
               > {
               >   "sentiment": "string",
               >   "emotion": "string",
               >   "intent": "string",
               >   "topics": ["string"],
               >   "summary": "string",
               >   "grammarMistakes": [
               >     {
               >       "error": "string",
               >       "suggestion": "string"
               >     }
               >   ],
               >   "fillerWords": ["string"],
               >   "speechPaceEstimate": "string",
               >   "clarity": {
               >     "clear": boolean,
               >     "reason": "string"
               >   }
               > }
               > ```
               >Return only valid JSON. Ensure all double quotes inside strings are escaped (e.g., \\"was\\"). Do not include unescaped quotes inside string values.
               >No error should be occur when transferring the json.
               > Here is the user's speech transcript:
                   %s
               """.formatted(speechText);
    }

}
