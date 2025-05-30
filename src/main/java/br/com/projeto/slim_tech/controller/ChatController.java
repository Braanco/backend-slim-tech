package br.com.projeto.slim_tech.controller;

import br.com.projeto.slim_tech.dto.HistoricoRequest;
import br.com.projeto.slim_tech.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping("/chat")
    public ResponseEntity chat(@RequestBody HistoricoRequest historicoRequest) {
        String chat = this.chatService.chat(historicoRequest.getMensagem());
        return ResponseEntity.ok(chat);
    }
}
