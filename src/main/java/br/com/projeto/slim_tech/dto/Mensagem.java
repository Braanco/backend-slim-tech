package br.com.projeto.slim_tech.dto;

import java.util.List;
import java.util.Map;

public class Mensagem {
    private String role; // "user" ou "model"
    private List<Map<String, String>> parts; // Lista de { "text": "..." }

    public Mensagem() {}

    public Mensagem(String role, List<Map<String, String>> parts) {
        this.role = role;
        this.parts = parts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Map<String, String>> getParts() {
        return parts;
    }

    public void setParts(List<Map<String, String>> parts) {
        this.parts = parts;
    }
}
