package com.southsystem.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.qameta.allure.*;
import io.restassured.response.Response;
import com.southsystem.models.RespostaImagensRaca;

/**
 * Testes para o endpoint de imagens por raça
 * Endpoint: GET /breed/{breed}/images
 */
@Epic("Dog API")
@Feature("Imagens por Raça")
@DisplayName("Testes de Imagens por Raça")
public class TesteImagensPorRaca extends TesteBase {

    @Test
    @Order(1)
    @Story("Buscar imagens por raça")
    @DisplayName("Deve retornar imagens de uma raça específica com sucesso")
    @Description("Verifica se a API retorna imagens de uma raça válida")
    @Severity(SeverityLevel.CRITICAL)
    void deveRetornarImagensDeUmaRacaEspecificaComSucesso() {
        String raca = "beagle";
        System.out.println("🖼️  [TESTE 1/6] Buscando imagens da raça: " + raca + "...");
        Response resposta = servicoDogApi.buscarImagensPorRaca(raca);
        
        assertAll("Validações da resposta de imagens por raça",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens não deve ser nula"),
            () -> assertFalse(resposta.jsonPath().getList("message").isEmpty(), "Lista de imagens não deve estar vazia")
        );
        System.out.println("✅ Teste de imagens por raça concluído!");
    }

    @Test
    @Order(2)
    @Story("Validar estrutura da resposta")
    @DisplayName("Deve validar estrutura correta da resposta JSON")
    @Description("Verifica se a estrutura da resposta JSON está conforme esperado")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarEstruturaDaRespostaJson() {
        RespostaImagensRaca resposta = servicoDogApi.buscarImagensPorRacaComoObjeto("labrador");
        
        assertAll("Validações da estrutura da resposta",
            () -> assertNotNull(resposta, "Objeto resposta não deve ser nulo"),
            () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
            () -> assertNotNull(resposta.getImagens(), "Lista de imagens não deve ser nula"),
            () -> assertFalse(resposta.getImagens().isEmpty(), "Lista de imagens não deve estar vazia")
        );
    }

    @Test
    @Order(3)
    @Story("Validar URLs das imagens")
    @DisplayName("Deve retornar URLs válidas de imagens")
    @Description("Verifica se todas as URLs retornadas são válidas e contêm o nome da raça")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarUrlsValidasDeImagens() {
        String raca = "labrador";
        RespostaImagensRaca resposta = servicoDogApi.buscarImagensPorRacaComoObjeto(raca);
        
        assertAll("Validações das URLs das imagens",
            () -> assertNotNull(resposta.getImagens(), "Lista de imagens não deve ser nula"),
            () -> assertFalse(resposta.getImagens().isEmpty(), "Lista deve conter imagens"),
            () -> assertTrue(resposta.getImagens().get(0).startsWith("https://"), "URL deve começar com https://"),
            () -> assertTrue(resposta.getImagens().get(0).contains(raca), "URL deve conter o nome da raça")
        );
    }

    @Test
    @Order(4)
    @Story("Testar diferentes raças")
    @DisplayName("Deve funcionar com diferentes raças válidas")
    @Description("Verifica se o endpoint funciona corretamente com diferentes raças")
    @Severity(SeverityLevel.NORMAL)
    void deveFuncionarComDiferentesRacasValidas() {
        String[] racas = {"bulldog", "poodle", "husky"};
        
        for (String raca : racas) {
            Response resposta = servicoDogApi.buscarImagensPorRaca(raca);
            
            assertAll("Validações para raça: " + raca,
                () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200 para " + raca),
                () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success para " + raca),
                () -> assertFalse(resposta.jsonPath().getList("message").isEmpty(), "Deve retornar imagens para " + raca)
            );
        }
    }

    @Test
    @Order(5)
    @Story("Testar raça inexistente")
    @DisplayName("Deve tratar adequadamente raça inexistente")
    @Description("Verifica como a API lida com raças que não existem")
    @Severity(SeverityLevel.NORMAL)
    void deveTratarAdequadamenteRacaInexistente() {
        String racaInexistente = "racainexistente123";
        Response resposta = servicoDogApi.buscarImagensPorRaca(racaInexistente);
        
        assertAll("Validações para raça inexistente",
            () -> assertEquals(404, resposta.getStatusCode(), "Status code deve ser 404 para raça inexistente"),
            () -> assertEquals("error", resposta.jsonPath().getString("status"), "Status deve ser error")
        );
    }

    @Test
    @Order(6)
    @Story("Validar performance")
    @DisplayName("Deve responder em tempo aceitável")
    @Description("Verifica se a API responde em menos de 3 segundos")
    @Severity(SeverityLevel.MINOR)
    void deveResponderEmTempoAceitavel() {
        long inicioTempo = System.currentTimeMillis();
        
        Response resposta = servicoDogApi.buscarImagensPorRaca("labrador");
        
        long tempoResposta = System.currentTimeMillis() - inicioTempo;
        
        assertAll("Validações de performance",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertTrue(tempoResposta < 3000, "Resposta deve ser em menos de 3 segundos")
        );
    }
}