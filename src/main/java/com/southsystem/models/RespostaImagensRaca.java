package com.southsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Modelo para resposta da API de imagens por raça
 * Endpoint: GET /breed/{breed}/images
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaImagensRaca {
    
    @JsonProperty("message")
    private List<String> imagens;
    
    @JsonProperty("status")
    private String status;
}