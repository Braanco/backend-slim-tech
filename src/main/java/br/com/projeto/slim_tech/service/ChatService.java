package br.com.projeto.slim_tech.service;

import br.com.projeto.slim_tech.domain.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    @Value("${spring.ai.openai.api-key}")
    private String keySecret;  // A chave API salva em application.properties

    private WebClient webClient;
    private ResponseService responseService;

    private final String urlBase = "https://generativelanguage.googleapis.com"; // URL base para a API Gemini

    public ChatService(WebClient.Builder webClientBuilder, ResponseService responseService) {
        this.webClient = webClientBuilder.baseUrl(urlBase).build();
        this.responseService = responseService;
    }


    public String chat(List<Map<String, String>> historico, Object conversaUser) {
        // Construção do prompt JSON
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("{")
                .append("\"contents\": [{")
                .append("\"parts\": [")
                .append("{\"text\": \"")
                .append("Esta é uma conversa contínua. O usuário e a IA têm trocado as seguintes mensagens:\n")
                .append("me der mensagens curtas.");


        for (Map<String,String> msg : historico){
            promptBuilder.append("model: ").append(msg.get("model")).append("\n");
            promptBuilder.append("user: ").append(msg.get("user"));
        }
        // A última mensagem do usuário
        System.out.println(historico.getLast().get("user"));
        promptBuilder.append("\n")
                .append("Agora o usuário diz: ")

                .append(conversaUser.toString()) // Última mensagem enviada
                .append("\"")
                .append("}]")
                .append("}]")
                .append("}");

        String promptJson = promptBuilder.toString();  // Finaliza o JSON

        try {
            // Envia a requisição para a API Gemini
            String responseJson = webClient.post()
                    .uri("/v1beta/models/gemini-2.0-flash:generateContent?key=" + keySecret)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(promptJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();  // Bloqueia até receber a resposta da API

            // Trata a resposta da IA
            Content content = responseService.mapperJson(responseJson, Content.class);

            // Retorna o texto da primeira resposta da IA
            return content.getCandidates().get(0).content().parts().get(0).text();

        } catch (Exception e) {
            // Em caso de erro ao chamar a API ou processar a resposta
            e.printStackTrace();
            return "Ocorreu um erro ao tentar obter uma resposta da IA.";
        }
    }
}
