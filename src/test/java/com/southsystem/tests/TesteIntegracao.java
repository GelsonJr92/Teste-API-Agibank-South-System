package com.southsystem.tests;

import com.southsystem.models.RespostaImagemAleatoria;
import com.southsystem.models.RespostaImagensRaca;
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
 * Testes de integração que validam fluxos completos da Dog API
 * Combina múltiplos endpoints para cenários reais de uso
 */
@Epic("Dog API")
@Feature("Testes de Integração")
@DisplayName("Testes de Integração Completos")
public class TesteIntegracao extends TesteBase {

    @Test
    @Order(1)
    @Story("Fluxo completo: Lista → Seleciona → Busca imagens")
    @DisplayName("Deve executar fluxo completo de busca de imagens por raça")
    @Description("Testa o fluxo: buscar todas as raças, selecionar uma e buscar suas imagens")
    @Severity(SeverityLevel.CRITICAL)
    void deveExecutarFluxoCompletoDeBuscaDeImagensPorRaca() {
        System.out.println("🔄 [INTEGRAÇÃO 1/6] Executando fluxo completo de busca...");
        // 1. Buscar todas as raças
        RespostaListaRacas listaRacas = servicoDogApi.buscarTodasRacasComoObjeto();
        assertEquals("success", listaRacas.getStatus(), "Busca de raças deve ter sucesso");
        assertFalse(listaRacas.getRacas().isEmpty(), "Deve ter raças disponíveis");

        // 2. Selecionar primeira raça disponível
        String racaSelecionada = listaRacas.getRacas().keySet().iterator().next();
        assertNotNull(racaSelecionada, "Deve conseguir selecionar uma raça");

        // 3. Buscar imagens da raça selecionada
        RespostaImagensRaca imagensRaca = servicoDogApi.buscarImagensPorRacaComoObjeto(racaSelecionada);
        
        assertAll("Validações do fluxo completo",
            () -> assertEquals("success", imagensRaca.getStatus(), "Busca de imagens deve ter sucesso"),
            () -> assertFalse(imagensRaca.getImagens().isEmpty(), "Deve retornar imagens da raça"),
            () -> assertTrue(imagensRaca.getImagens().get(0).contains(racaSelecionada), 
                "URLs devem conter o nome da raça selecionada")
        );
    }

    @Test
    @Order(2)
    @Story("Comparar imagem aleatória geral vs específica")
    @DisplayName("Deve comparar imagens aleatórias gerais e específicas")
    @Description("Testa diferenças entre imagens aleatórias gerais e de raças específicas")
    @Severity(SeverityLevel.NORMAL)
    void deveCompararImagemAleatoriaGeralVsEspecifica() {
        // 1. Buscar imagem aleatória geral
        RespostaImagemAleatoria imagemGeral = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        assertEquals("success", imagemGeral.getStatus(), "Imagem aleatória geral deve ter sucesso");

        // 2. Buscar imagem aleatória de raça específica
        String raca = "labrador";
        Response imagemEspecifica = servicoDogApi.buscarImagemAleatoriaPorRaca(raca);
        assertEquals(200, imagemEspecifica.getStatusCode(), "Imagem de raça específica deve ter sucesso");

        // 3. Validar que ambas são URLs válidas mas podem ser diferentes
        String urlGeral = imagemGeral.getImagemUrl();
        String urlEspecifica = imagemEspecifica.jsonPath().getString("message");

        assertAll("Validações de comparação de imagens",
            () -> assertTrue(urlGeral.contains("dog.ceo"), "URL geral deve ser do dog.ceo"),
            () -> assertTrue(urlEspecifica.contains("dog.ceo"), "URL específica deve ser do dog.ceo"),
            () -> assertTrue(urlEspecifica.contains(raca), "URL específica deve conter nome da raça"),
            () -> assertTrue(urlGeral.matches(".*\\.(jpg|jpeg|png)$"), "URL geral deve ser imagem"),
            () -> assertTrue(urlEspecifica.matches(".*\\.(jpg|jpeg|png)$"), "URL específica deve ser imagem")
        );
    }

    @Test
    @Order(3)
    @Story("Validar consistência de dados entre endpoints")
    @DisplayName("Deve manter consistência entre lista de raças e busca individual")
    @Description("Verifica se raças listadas realmente existem quando buscadas individualmente")
    @Severity(SeverityLevel.NORMAL)
    void deveManterConsistenciaEntreDados() {
        // 1. Buscar lista de raças
        RespostaListaRacas listaRacas = servicoDogApi.buscarTodasRacasComoObjeto();
        Map<String, List<String>> racas = listaRacas.getRacas();

        // 2. Testar algumas raças populares da lista
        String[] racasParaTestar = {"labrador", "bulldog", "beagle"};
        
        for (String raca : racasParaTestar) {
            if (racas.containsKey(raca)) {
                Response imagensRaca = servicoDogApi.buscarImagensPorRaca(raca);
                
                assertAll("Validações de consistência para raça: " + raca,
                    () -> assertEquals(200, imagensRaca.getStatusCode(), 
                        "Raça " + raca + " listada deve retornar imagens"),
                    () -> assertEquals("success", imagensRaca.jsonPath().getString("status"), 
                        "Status deve ser success para raça existente")
                );
            }
        }
    }

    @Test
    @Order(4)
    @Story("Testar performance em sequência")
    @DisplayName("Deve manter performance aceitável em múltiplas chamadas")
    @Description("Verifica se performance se mantém estável em chamadas sequenciais")
    @Severity(SeverityLevel.MINOR)
    void deveManterPerformanceAceitavelEmMultiplasChamadas() {
        long tempoInicio = System.currentTimeMillis();
        
        // Fazer múltiplas chamadas sequenciais
        for (int i = 0; i < 5; i++) {
            Response resposta = servicoDogApi.buscarImagemAleatoria();
            assertEquals(200, resposta.getStatusCode(), "Todas as chamadas devem ter sucesso");
        }
        
        long tempoTotal = System.currentTimeMillis() - tempoInicio;
        
        assertAll("Validações de performance sequencial",
            () -> assertTrue(tempoTotal < 15000, 
                String.format("5 chamadas devem completar em menos de 15s, mas levaram %dms", tempoTotal)),
            () -> assertTrue(tempoTotal / 5 < 5000, 
                "Tempo médio por chamada deve ser menor que 5s")
        );
    }

    @Test
    @Order(5)
    @Story("Testar robustez da API")
    @DisplayName("Deve manter estabilidade em diferentes cenários")
    @Description("Testa a API com diferentes tipos de requisições para validar robustez")
    @Severity(SeverityLevel.NORMAL)
    void deveManterEstabilidadeEmDiferentesCenarios() {
        // 1. Requisição válida comum
        Response racasValidas = servicoDogApi.buscarTodasRacas();
        assertEquals(200, racasValidas.getStatusCode(), "Requisição padrão deve funcionar");

        // 2. Requisição de imagem aleatória
        Response imagemAleatoria = servicoDogApi.buscarImagemAleatoria();
        assertEquals(200, imagemAleatoria.getStatusCode(), "Imagem aleatória deve funcionar");

        // 3. Requisição de raça específica válida
        Response racaEspecifica = servicoDogApi.buscarImagensPorRaca("labrador");
        assertEquals(200, racaEspecifica.getStatusCode(), "Raça específica deve funcionar");

        // 4. Requisição de raça inválida (deve retornar erro controlado)
        Response racaInvalida = servicoDogApi.buscarImagensPorRaca("racainexistente");
        assertTrue(racaInvalida.getStatusCode() >= 400, "Raça inválida deve retornar erro");

        // Todas as respostas de sucesso devem ter estrutura similar
        assertAll("Validações de robustez",
            () -> assertEquals("success", racasValidas.jsonPath().getString("status")),
            () -> assertEquals("success", imagemAleatoria.jsonPath().getString("status")),
            () -> assertEquals("success", racaEspecifica.jsonPath().getString("status")),
            () -> assertEquals("error", racaInvalida.jsonPath().getString("status"))
        );
    }

    @Test
    @Order(6)
    @Story("Validar tipos de conteúdo")
    @DisplayName("Deve retornar sempre conteúdo no formato correto")
    @Description("Verifica se todos os endpoints retornam JSON válido com estrutura esperada")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarSempreConteudoNoFormatoCorreto() {
        // Testar diferentes endpoints
        Response[] respostas = {
            servicoDogApi.buscarTodasRacas(),
            servicoDogApi.buscarImagemAleatoria(),
            servicoDogApi.buscarImagensPorRaca("beagle")
        };

        for (Response resposta : respostas) {
            assertAll("Validações de formato para endpoint",
                () -> assertEquals(200, resposta.getStatusCode(), "Status deve ser 200"),
                () -> assertTrue(resposta.getHeader("Content-Type").contains("application/json"), 
                    "Content-Type deve ser JSON"),
                () -> assertNotNull(resposta.jsonPath().getString("status"), "Deve ter campo status"),
                () -> assertNotNull(resposta.jsonPath().get("message"), "Deve ter campo message"),
                () -> assertTrue(resposta.jsonPath().getString("status").equals("success"), 
                    "Status deve ser success")
            );
        }
    }
}