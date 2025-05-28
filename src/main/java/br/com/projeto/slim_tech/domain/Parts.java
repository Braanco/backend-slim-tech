package br.com.projeto.slim_tech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Parts (List<Text> parts) {
}
