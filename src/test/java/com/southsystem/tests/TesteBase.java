package com.southsystem.tests;

import com.southsystem.config.ConfiguracaoApi;
import com.southsystem.services.ServicoDogApi;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Classe base para todos os testes
 * Configura o ambiente de teste e inicializa os serviços necessários
 */
@ExtendWith(AllureJunit5.class)
public abstract class TesteBase {

    protected ServicoDogApi servicoDogApi;
    private long inicioTeste;

    @BeforeAll
    static void configurarAmbiente() {
        System.out.println("\n🔧 CONFIGURANDO AMBIENTE DE TESTE...");
        System.out.println("📡 Base URL: " + ConfiguracaoApi.BASE_URL);
        
        // Configurar RestAssured com logs detalhados
        RestAssured.baseURI = ConfiguracaoApi.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Habilitar logs de todas as requisições e respostas
        RestAssured.config = RestAssured.config()
            .logConfig(LogConfig.logConfig()
                .enableLoggingOfRequestAndResponseIfValidationFails()
                .enablePrettyPrinting(true));
                
        System.out.println("✅ Ambiente configurado com sucesso!");
        System.out.println("📋 Logs HTTP habilitados para debug!");
        System.out.println();
    }

    @BeforeEach
    void inicializarServicos(TestInfo testInfo) {
        servicoDogApi = new ServicoDogApi();
        inicioTeste = System.currentTimeMillis();
        
        String nomeMetodo = testInfo.getTestMethod().get().getName();
        String nomeAmigavel = converterNomeMetodo(nomeMetodo);
        
        System.out.println("🚀 EXECUTANDO: " + nomeAmigavel);
        System.out.println("⏱️  Início: " + java.time.LocalTime.now());
        System.out.println("🔍 Logs HTTP detalhados habilitados");
        System.out.println("───────────────────────────────────────");
    }
    
    @AfterEach 
    void finalizarTeste(TestInfo testInfo) {
        long duracaoMs = System.currentTimeMillis() - inicioTeste;
        String nomeMetodo = testInfo.getTestMethod().get().getName();
        String nomeAmigavel = converterNomeMetodo(nomeMetodo);
        
        System.out.println("───────────────────────────────────────");
        System.out.println("✅ CONCLUÍDO: " + nomeAmigavel);
        System.out.println("⏱️  Duração: " + duracaoMs + "ms");
        System.out.println("⏰ Fim: " + java.time.LocalTime.now());
        System.out.println("");
    }
    
    private String converterNomeMetodo(String nomeMetodo) {
        return nomeMetodo
            .replaceAll("deve", "Deve ")
            .replaceAll("([A-Z])", " $1")
            .replaceAll("\\s+", " ")
            .trim()
            .toLowerCase();
    }
}