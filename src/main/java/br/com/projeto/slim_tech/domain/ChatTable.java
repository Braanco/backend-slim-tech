package br.com.projeto.slim_tech.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "chats")
@Entity
public class ChatTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "usuario")
    @Lob
    private String user;
    @Column(name = "modelo")
    @Lob
    private String model;

    public ChatTable() {
    }

    public ChatTable(String user, String model) {
        this.user = user;
        this.model = model;
    }

    public ChatTable(String model) {
        this.model = model;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
