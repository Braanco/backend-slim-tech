package br.com.projeto.slim_tech.controller;

import br.com.projeto.slim_tech.dto.ResponseDTO;
import br.com.projeto.slim_tech.dto.RespostaDto;
import br.com.projeto.slim_tech.service.ResponseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/slim_tech")
public class Controller {

    private ResponseService service;

    public Controller(ResponseService service) {
        this.service = service;
    }


    @PostMapping("/form")
    public ResponseEntity testeGoogleIA(@RequestBody @Valid RespostaDto body) {
        String prompt = service.createPrompt(body);
        List<ResponseDTO> jsonFormats = service.obterRespostaIA(prompt);
        return ResponseEntity.ok(jsonFormats);
    }


}
