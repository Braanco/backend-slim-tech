package br.com.projeto.slim_tech.service;

import br.com.projeto.slim_tech.domain.ChatTable;
import br.com.projeto.slim_tech.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatTableService {
    private ChatRepository repository;

    public ChatTableService(ChatRepository repository) {
        this.repository = repository;
    }

    public void saveChat (String model, String user){
        ChatTable chatTable = new ChatTable();
        if (repository.findAll().isEmpty()){
            chatTable = new ChatTable(user,model);
            repository.save(chatTable);
        }else {
            chatTable.setModel(model);
            chatTable.setUser(user);
            repository.save(chatTable);
        }

    }

    public List<ChatTable> recoverChat (){
        return repository.findAll();
    }

}
