package com.southsystem.tests;

import com.southsystem.models.RespostaListaRacas;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o endpoint de listagem de raças
 * Endpoint: GET /breeds/list/all
 */
@Epic("Dog API")
@Feature("Listagem de Raças")
@DisplayName("Testes de Listagem de Raças")
public class TesteListagemRacas extends TesteBase {

    @Test
    @Order(1)
    @Story("Buscar todas as raças")
    @DisplayName("Deve retornar lista completa de raças com sucesso")
    @Description("Verifica se a API retorna todas as raças disponíveis com status code 200")
    @Severity(SeverityLevel.CRITICAL)
    void deveRetornarListaCompletaDeRacasComSucesso() {
        System.out.println("📋 [TESTE 1/8] Buscando lista completa de raças...");
        Response resposta = servicoDogApi.buscarTodasRacas();
        
        assertAll("Validações da resposta de listagem de raças",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getMap("message"), "Message não deve ser nulo"),
            () -> assertFalse(resposta.jsonPath().getMap("message").isEmpty(), "Lista de raças não deve estar vazia")
        );
        System.out.println("✅ Teste 1 concluído com sucesso!");
    }

    @Test
    @Order(2)
    @Story("Validar estrutura da resposta")
    @DisplayName("Deve validar estrutura correta da resposta JSON")
    @Description("Verifica se a estrutura da resposta JSON está conforme esperado")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarEstruturaDaRespostaJson() {
        System.out.println("🔍 [TESTE 2/8] Validando estrutura da resposta JSON...");
        RespostaListaRacas resposta = servicoDogApi.buscarTodasRacasComoObjeto();
        
        assertAll("Validações da estrutura da resposta",
            () -> assertNotNull(resposta, "Objeto resposta não deve ser nulo"),
            () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
            () -> assertNotNull(resposta.getRacas(), "Map de raças não deve ser nulo"),
            () -> assertFalse(resposta.getRacas().isEmpty(), "Map de raças não deve estar vazio")
        );
        System.out.println("✅ Teste 2 concluído com sucesso!");
    }

    @Test
    @Order(3)
    @Story("Validar conteúdo das raças")
    @DisplayName("Deve conter raças conhecidas na lista")
    @Description("Verifica se raças populares estão presentes na resposta")
    @Severity(SeverityLevel.NORMAL)
    void deveConterRacasConhecidasNaLista() {
        RespostaListaRacas resposta = servicoDogApi.buscarTodasRacasComoObjeto();
        Map<String, List<String>> racas = resposta.getRacas();
        
        assertAll("Validações de raças conhecidas",
            () -> assertTrue(racas.containsKey("labrador"), "Deve conter a raça labrador"),
            () -> assertTrue(racas.containsKey("bulldog"), "Deve conter a raça bulldog"),
            () -> assertTrue(racas.containsKey("beagle"), "Deve conter a raça beagle"),
            () -> assertTrue(racas.containsKey("retriever"), "Deve conter a raça retriever")
        );
    }

    @Test
    @Order(4)
    @Story("Validar sub-raças")
    @DisplayName("Deve validar sub-raças quando disponíveis")
    @Description("Verifica se raças com sub-raças retornam lista de sub-raças")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarSubRacasQuandoDisponiveis() {
        RespostaListaRacas resposta = servicoDogApi.buscarTodasRacasComoObjeto();
        Map<String, List<String>> racas = resposta.getRacas();
        
        // Verificar se terrier tem sub-raças (normalmente tem)
        if (racas.containsKey("terrier")) {
            List<String> subRacasTerrrier = racas.get("terrier");
            assertFalse(subRacasTerrrier.isEmpty(), "Terrier deve ter sub-raças");
        }
        
        // Verificar se spaniel tem sub-raças (normalmente tem)
        if (racas.containsKey("spaniel")) {
            List<String> subRacasSpaniel = racas.get("spaniel");
            assertFalse(subRacasSpaniel.isEmpty(), "Spaniel deve ter sub-raças");
        }
    }

    @Test
    @Order(5)
    @Story("Validar performance")
    @DisplayName("Deve responder em tempo aceitável")
    @Description("Verifica se a API responde em menos de 3 segundos")
    @Severity(SeverityLevel.MINOR)
    void deveResponderEmTempoAceitavel() {
        long tempoInicio = System.currentTimeMillis();
        Response resposta = servicoDogApi.buscarTodasRacas();
        long tempoFim = System.currentTimeMillis();
        long tempoResposta = tempoFim - tempoInicio;
        
        assertAll("Validações de performance",
            () -> assertEquals(200, resposta.getStatusCode(), "Deve retornar sucesso"),
            () -> assertTrue(tempoResposta < 3000, 
                String.format("Tempo de resposta deve ser menor que 3000ms, mas foi %dms", tempoResposta))
        );
    }

    @Test
    @Order(6)
    @Story("Validar headers da resposta")
    @DisplayName("Deve retornar headers corretos")
    @Description("Verifica se os headers da resposta estão conforme esperado")
    @Severity(SeverityLevel.MINOR)
    void deveRetornarHeadersCorretos() {
        Response resposta = servicoDogApi.buscarTodasRacas();
        
        assertAll("Validações dos headers",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertNotNull(resposta.getHeader("Content-Type"), "Header Content-Type deve estar presente"),
            () -> assertTrue(resposta.getHeader("Content-Type").contains("application/json"), 
                "Content-Type deve ser application/json")
        );
    }

    @Test
    @Order(7)
    @Story("Validar consistência dos dados")
    @DisplayName("Deve manter consistência entre múltiplas chamadas")
    @Description("Verifica se múltiplas chamadas retornam os mesmos dados")
    @Severity(SeverityLevel.NORMAL)
    void deveManterConsistenciaEntreMultiplasChamadas() {
        RespostaListaRacas primeiraChamada = servicoDogApi.buscarTodasRacasComoObjeto();
        RespostaListaRacas segundaChamada = servicoDogApi.buscarTodasRacasComoObjeto();
        
        assertAll("Validações de consistência",
            () -> assertEquals(primeiraChamada.getStatus(), segundaChamada.getStatus(), 
                "Status deve ser consistente"),
            () -> assertEquals(primeiraChamada.getRacas().size(), segundaChamada.getRacas().size(), 
                "Número de raças deve ser consistente"),
            () -> assertEquals(primeiraChamada.getRacas().keySet(), segundaChamada.getRacas().keySet(), 
                "Lista de raças deve ser idêntica")
        );
    }

    @Test
    @Order(8)
    @Story("Validar formato dos nomes das raças")
    @DisplayName("Deve retornar nomes de raças em formato válido")
    @Description("Verifica se os nomes das raças estão em formato lowercase sem espaços")
    @Severity(SeverityLevel.MINOR)
    void deveRetornarNomesDeRacasEmFormatoValido() {
        RespostaListaRacas resposta = servicoDogApi.buscarTodasRacasComoObjeto();
        Map<String, List<String>> racas = resposta.getRacas();
        
        for (String nomeRaca : racas.keySet()) {
            assertAll("Validações do formato do nome da raça: " + nomeRaca,
                () -> assertTrue(nomeRaca.equals(nomeRaca.toLowerCase()), 
                    "Nome da raça deve estar em lowercase: " + nomeRaca),
                () -> assertFalse(nomeRaca.contains(" "), 
                    "Nome da raça não deve conter espaços: " + nomeRaca),
                () -> assertFalse(nomeRaca.isEmpty(), 
                    "Nome da raça não deve estar vazio")
            );
        }
    }
}