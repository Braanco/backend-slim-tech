package br.com.projeto.slim_tech.service;

import br.com.projeto.slim_tech.GeminiRequest;
import br.com.projeto.slim_tech.domain.ChatTable;
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
    private ChatTableService chatTableService;
    public ChatService(WebClient.Builder webClientBuilder, ResponseService responseService, ChatTableService chatTableService) {
        this.webClient = webClientBuilder.baseUrl(urlBase).build();
        this.responseService = responseService;
        this.chatTableService = chatTableService;
    }


    public String chat(Object conversaUser) {


        StringBuilder textoPrompt = new StringBuilder();
        textoPrompt.append("Apresente a resposta como um texto corrido, em linguagem natural, sem usar formato de lista ou JSON.\n");
        textoPrompt.append("Caso o usuário peça mais exercicios,não use o formato de lista, JSON ou no formato de objeto.\n");
        textoPrompt.append("Esta é uma conversa contínua:\n");

        for (ChatTable chatTable : chatTableService.recoverChat()) {
            textoPrompt.append("model: ").append(chatTable.getModel()).append("\n");
            textoPrompt.append("user: ").append(chatTable.getUser()).append("\n");
        }

        textoPrompt.append("\nAgora o usuário diz: ").append(conversaUser.toString());

        GeminiRequest.Part part = new GeminiRequest.Part(textoPrompt.toString());
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest request = new GeminiRequest(List.of(content));

        try {
            // Envia a requisição para a API Gemini
            String responseJson = webClient.post()
                    .uri("/v1beta/models/gemini-2.0-flash:generateContent?key=" + keySecret)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();  // Bloqueia até receber a resposta da API

            // Trata a resposta da IA
            Content contents = responseService.mapperJson(responseJson, Content.class);
            String text = contents.getCandidates().get(0).content().parts().get(0).text();

            this.chatTableService.saveChat(text,conversaUser.toString());
            // Retorna o texto da primeira resposta da IA
            return text;

        } catch (Exception e) {
            // Em caso de erro ao chamar a API ou processar a resposta
            e.printStackTrace();
            return "Ocorreu um erro ao tentar obter uma resposta da IA.";
        }
    }
}
