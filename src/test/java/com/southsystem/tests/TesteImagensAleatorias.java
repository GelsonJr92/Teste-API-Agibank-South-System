package com.southsystem.tests;

import com.southsystem.models.RespostaImagemAleatoria;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o endpoint de imagens aleatórias
 * Endpoint: GET /breeds/image/random
 */
@Epic("Dog API")
@Feature("Imagens Aleatórias")
@DisplayName("Testes de Imagens Aleatórias")
public class TesteImagensAleatorias extends TesteBase {

    @Test
    @Order(1)
    @Story("Buscar imagem aleatória")
    @DisplayName("Deve retornar uma imagem aleatória com sucesso")
    @Description("Verifica se a API retorna uma imagem aleatória válida")
    @Severity(SeverityLevel.CRITICAL)
    void deveRetornarUmaImagemAleatoriaComSucesso() {
        System.out.println("🎲 [TESTE 1/10] Buscando imagem aleatória...");
        Response resposta = servicoDogApi.buscarImagemAleatoria();
        
        assertAll("Validações da resposta de imagem aleatória",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getString("message"), "URL da imagem não deve ser nula"),
            () -> assertFalse(resposta.jsonPath().getString("message").isEmpty(), "URL da imagem não deve estar vazia")
        );
        System.out.println("✅ Teste de imagem aleatória concluído!");
    }

    @Test
    @Order(2)
    @Story("Validar estrutura da resposta")
    @DisplayName("Deve validar estrutura correta da resposta JSON")
    @Description("Verifica se a estrutura da resposta JSON está conforme esperado")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarEstruturaDaRespostaJson() {
        RespostaImagemAleatoria resposta = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        
        assertAll("Validações da estrutura da resposta",
            () -> assertNotNull(resposta, "Objeto resposta não deve ser nulo"),
            () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
            () -> assertNotNull(resposta.getImagemUrl(), "URL da imagem não deve ser nula"),
            () -> assertFalse(resposta.getImagemUrl().isEmpty(), "URL da imagem não deve estar vazia")
        );
    }

    @Test
    @Order(3)
    @Story("Validar URL da imagem")
    @DisplayName("Deve retornar URL válida de imagem")
    @Description("Verifica se a URL retornada é válida e aponta para uma imagem")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarUrlValidaDeImagem() {
        RespostaImagemAleatoria resposta = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        String urlImagem = resposta.getImagemUrl();
        
        assertAll("Validações da URL da imagem",
            () -> assertNotNull(urlImagem, "URL da imagem não deve ser nula"),
            () -> assertFalse(urlImagem.isEmpty(), "URL da imagem não deve estar vazia"),
            () -> assertTrue(urlImagem.startsWith("https://"), "URL deve começar com https://"),
            () -> assertTrue(urlImagem.contains("dog.ceo"), "URL deve conter domínio dog.ceo"),
            () -> assertTrue(urlImagem.matches(".*\\.(jpg|jpeg|png)$"), 
                "URL deve terminar com extensão de imagem válida")
        );
    }

    @Test
    @Order(4)
    @Story("Validar aleatoriedade")
    @DisplayName("Deve retornar imagens diferentes em chamadas consecutivas")
    @Description("Verifica se múltiplas chamadas retornam imagens diferentes (aleatoriedade)")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarImagensDiferentesEmChamadasConsecutivas() {
        RespostaImagemAleatoria primeira = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        RespostaImagemAleatoria segunda = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        RespostaImagemAleatoria terceira = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        
        // Nota: É possível (mas improvável) que retorne a mesma imagem
        // Vamos verificar que pelo menos uma das três é diferente
        boolean todasIguais = primeira.getImagemUrl().equals(segunda.getImagemUrl()) && 
                             segunda.getImagemUrl().equals(terceira.getImagemUrl());
        
        assertAll("Validações de aleatoriedade",
            () -> assertEquals("success", primeira.getStatus(), "Primeira chamada deve ter sucesso"),
            () -> assertEquals("success", segunda.getStatus(), "Segunda chamada deve ter sucesso"),
            () -> assertEquals("success", terceira.getStatus(), "Terceira chamada deve ter sucesso"),
            () -> assertFalse(todasIguais, "Nem todas as imagens devem ser iguais (teste de aleatoriedade)")
        );
    }

    @Test
    @Order(5)
    @Story("Validar múltiplas imagens aleatórias")
    @DisplayName("Deve retornar múltiplas imagens aleatórias")
    @Description("Verifica se a API pode retornar múltiplas imagens aleatórias de uma vez")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarMultiplasImagensAleatorias() {
        int quantidade = 3;
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatorias(quantidade);
        
        assertAll("Validações de múltiplas imagens aleatórias",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens não deve ser nula"),
            () -> assertEquals(quantidade, resposta.jsonPath().getList("message").size(), 
                "Deve retornar exatamente " + quantidade + " imagens")
        );
    }

    @Test
    @Order(6)
    @Story("Validar performance")
    @DisplayName("Deve responder em tempo aceitável")
    @Description("Verifica se a API responde em menos de 3 segundos")
    @Severity(SeverityLevel.MINOR)
    void deveResponderEmTempoAceitavel() {
        long tempoInicio = System.currentTimeMillis();
        Response resposta = servicoDogApi.buscarImagemAleatoria();
        long tempoFim = System.currentTimeMillis();
        long tempoResposta = tempoFim - tempoInicio;
        
        assertAll("Validações de performance",
            () -> assertEquals(200, resposta.getStatusCode(), "Deve retornar sucesso"),
            () -> assertTrue(tempoResposta < 3000, 
                String.format("Tempo de resposta deve ser menor que 3000ms, mas foi %dms", tempoResposta))
        );
    }

    @Test
    @Order(7)
    @Story("Validar headers da resposta")
    @DisplayName("Deve retornar headers corretos")
    @Description("Verifica se os headers da resposta estão conforme esperado")
    @Severity(SeverityLevel.MINOR)
    void deveRetornarHeadersCorretos() {
        Response resposta = servicoDogApi.buscarImagemAleatoria();
        
        assertAll("Validações dos headers",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertNotNull(resposta.getHeader("Content-Type"), "Header Content-Type deve estar presente"),
            () -> assertTrue(resposta.getHeader("Content-Type").contains("application/json"), 
                "Content-Type deve ser application/json")
        );
    }

    @Test
    @Order(8)
    @Story("Buscar imagem aleatória por raça")
    @DisplayName("Deve retornar imagem aleatória de raça específica")
    @Description("Verifica se a API retorna imagem aleatória de uma raça específica")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarImagemAleatoriaPorRaca() {
        String raca = "labrador";
        Response resposta = servicoDogApi.buscarImagemAleatoriaPorRaca(raca);
        
        assertAll("Validações de imagem aleatória por raça",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getString("message"), "URL da imagem não deve ser nula"),
            () -> assertTrue(resposta.jsonPath().getString("message").contains(raca), 
                "URL deve conter o nome da raça: " + raca)
        );
    }

    @Test
    @Order(9)
    @Story("Validar múltiplas imagens por raça")
    @DisplayName("Deve retornar múltiplas imagens aleatórias de raça específica")
    @Description("Verifica se a API retorna múltiplas imagens aleatórias de uma raça")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarMultiplasImagensPorRaca() {
        String raca = "beagle";
        int quantidade = 5;
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatoriasPorRaca(raca, quantidade);
        
        assertAll("Validações de múltiplas imagens por raça",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens não deve ser nula"),
            () -> assertTrue(resposta.jsonPath().getList("message").size() <= quantidade, 
                "Não deve retornar mais imagens que o solicitado")
        );
    }

    @Test
    @Order(10)
    @Story("Testar limite de múltiplas imagens")
    @DisplayName("Deve respeitar limite máximo de imagens")
    @Description("Verifica comportamento ao solicitar quantidade excessiva de imagens")
    @Severity(SeverityLevel.MINOR)
    void deveRespeitarLimiteMaximoDeImagens() {
        int quantidadeExcessiva = 100;
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatorias(quantidadeExcessiva);
        
        // A API pode limitar a quantidade ou retornar erro
        assertTrue(resposta.getStatusCode() == 200 || resposta.getStatusCode() >= 400, 
            "Deve retornar sucesso com limite ou erro para quantidade excessiva");
        
        if (resposta.getStatusCode() == 200) {
            assertTrue(resposta.jsonPath().getList("message").size() <= 50, 
                "Deve aplicar limite razoável de imagens");
        }
    }
}