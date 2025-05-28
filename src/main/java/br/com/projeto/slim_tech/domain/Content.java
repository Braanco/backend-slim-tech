package br.com.projeto.slim_tech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {

    private  List<Candidates> candidates;

    public List<Candidates> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidates> candidates) {
        this.candidates = candidates;

    }

    @Override
    public String toString() {
        return "Content{" +
                "candidates=" + candidates +
                '}';
    }
}
