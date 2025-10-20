package com.southsystem.config;

/**
 * Classe de configuração base para testes da Dog API
 * Contém as configurações padrão para requisições HTTP
 */
public class ConfiguracaoApi {
    
    public static final String BASE_URI = "https://dog.ceo/api";
    public static final String BASE_URL = "https://dog.ceo/api";
    public static final String CONTENT_TYPE = "application/json";
    
    /**
     * Construtor privado para evitar instanciação
     */
    private ConfiguracaoApi() {
        // Classe utilitária - não deve ser instanciada
    }
}