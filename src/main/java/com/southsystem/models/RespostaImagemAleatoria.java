package com.southsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Modelo para resposta da API de imagem aleatória
 * Endpoint: GET /breeds/image/random
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaImagemAleatoria {
    
    @JsonProperty("message")
    private String imagemUrl;
    
    @JsonProperty("status")
    private String status;
}