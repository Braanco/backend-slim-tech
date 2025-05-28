package br.com.projeto.slim_tech.service;

import br.com.projeto.slim_tech.dto.RespostaDto;
import br.com.projeto.slim_tech.domain.Content;
import br.com.projeto.slim_tech.dto.ResponseDTO;
import br.com.projeto.slim_tech.exception.SlimTechException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResponseService {

    @Value("${spring.ai.openai.api-key}")
    // essa key estar sendo salva no application.properties para projeto educacional.
    private String keySecret;

    private final String urlBase = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    private static HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Logger logger = LoggerFactory.getLogger(ResponseService.class);


    public String coletaDeDados(RespostaDto body) {
        logger.info("coleta dos dados:");
        Map<String, Object> coleta = new HashMap<>();
        coleta.put("Objetivo", body.metaDoUsuario());
        coleta.put("Condiçoes_medicas_e_limitaçoes", body.problemaDeSaude());
        coleta.put("peso_atual", body.peso());
        coleta.put("idade", body.idade());
        coleta.put("Altura", body.altura());
        coleta.put("Genero", body.genero());


        return coleta.toString();
    }

    public String createPrompt(RespostaDto body) {
        logger.debug("Gerando prompt para o usuário com os dados: {}", body);

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("{")
                .append("\"contents\": [{")
                .append("\"parts\": [{\"text\": \"")
                .append("Quero um plano completo de 5 exercícios e alimentação para determinado objetivo do usuario , baseado em evidências científicas. ")
                .append("O plano deve considerar minhas condições físicas, alimentação e rotina. Aqui estão os detalhes:\n\n")
                .append(coletaDeDados(body)).append("\n")
                .append("Use apenas string para devolver a resposta.\n")
                .append("Use esse modelo de JSON schema e não precisa adicionar observação:\n")
                .append("Resposta = [{exercicio : str , descricao : str , frequencia : str , intensidade : str, plano_alimentar : str , link_de_artigos: str}]\n")
                .append("return: resposta")
                .append("\"}]")
                .append("}]")
                .append("}");

        return promptBuilder.toString();
    }


    //fazer  a requisição para a url da IA
    public List<ResponseDTO> obterRespostaIA(String prompt) {
        logger.info("Iniciando a requisição para obter resposta da IA.");
        try {
            String json = resposeHttp(prompt);
            logger.debug("Resposta recebida da IA. ");
            Content content = mapperJson(json, Content.class);
            String response = content
                    .getCandidates()
                    .get(0)
                    .content()
                    .parts()
                    .get(0)
                    .text();

            logger.debug("Resposta recebida da IA: {}", response);
            logger.info("Transformando resposta em lista de ResponseDTO.");
            return transformarResponseEmLista(response);

        } catch (IOException e) {
            logger.error("Error during HTTP request: {}", e.getMessage(), e);
            throw new SlimTechException("Error of IO : ", e.getCause());
        } catch (InterruptedException e) {
            logger.error("Error of interrupted: {}", e.getMessage(), e);
            throw new SlimTechException("Interrupted error: ", e.getCause());
        }

    }

    public String resposeHttp(String prompt) throws IOException, InterruptedException {
        HttpRequest httpRequest = this.criarRequest(this.urlBase, this.keySecret, prompt);
        HttpResponse<String> httpResponse = null;
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();

    }

    //metodo para o httpRequest
    public HttpRequest criarRequest(String urlBase, String keySecret, String prompt) {
        return HttpRequest
                .newBuilder()
                .uri(URI.create(urlBase + keySecret))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(prompt, StandardCharsets.UTF_8))
                .build();
    }


    //transformar a resposta da requisição em lista
    public List<ResponseDTO> transformarResponseEmLista(String response) throws JsonProcessingException {

        if (response == null || response.isEmpty()) {
            logger.error("The response is null or empty: {}", response);
            throw new SlimTechException("The response is null or empty");
        }

        String substring = response.substring(8, response.length() - 4).trim();
        List<ResponseDTO> list = (List<ResponseDTO>) mapperJson(substring, ResponseDTO.class);
        return list;

    }


    public <T> T mapperJson(String response, Class<T> classe) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (response.trim().startsWith("[")) {
                return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, classe));
            }
            var resposta = mapper.readValue(response, classe);
            return resposta;
        } catch (JsonMappingException e) {
            logger.error("Error of mapping: {}", e.getMessage(), e);
            throw new SlimTechException("Error of Mapping :", e.getCause());
        } catch (JsonProcessingException e) {
            logger.error("Error of processing: {}", e.getMessage(), e);
            throw new SlimTechException("Error of processing : ", e.getCause());
        }
    }



}
