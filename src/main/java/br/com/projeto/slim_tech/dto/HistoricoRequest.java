package br.com.projeto.slim_tech.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricoRequest {
    private String mensagem;
    private List<Map<String, String>> contents;

    // Getters and Setters
    public List<Map<String, String>> getContents() {
        return contents;
    }

    public void setContents(List<Map<String, String>> contents) {
        this.contents = contents;
    }

    public Object getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
