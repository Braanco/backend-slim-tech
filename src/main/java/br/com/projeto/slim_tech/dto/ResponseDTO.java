package br.com.projeto.slim_tech.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO {
    String exercicio;
    String descricao;
    String frequencia;
    String intensidade;
    String plano_alimentar;
    String link_de_artigos;

    public String getPlano_alimentar() {
        return plano_alimentar;
    }

    public void setPlano_alimentar(String plano_alimentar) {
        this.plano_alimentar = plano_alimentar;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(String intensidade) {
        this.intensidade = intensidade;
    }

    public String getLink_de_artigos() {
        return link_de_artigos;
    }

    public void setLink_de_artigos(String link_de_artigos) {
        this.link_de_artigos = link_de_artigos;
    }

    public String getExercicio() {
        return exercicio;
    }

    public void setExercicio(String exercicio) {
        this.exercicio = exercicio;
    }

    @Override
    public String toString() {
        return "Resposta{" +
                "exercicio='" + exercicio + '\'' +
                ", descricao='" + descricao + '\'' +
                ", frequencio='" + frequencia + '\'' +
                ", intensidade='" + intensidade + '\'' +
                ", link_de_artigos='" + link_de_artigos + '\'' +
                '}';
    }
}
