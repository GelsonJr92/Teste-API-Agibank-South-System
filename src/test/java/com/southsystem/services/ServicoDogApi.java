package com.southsystem.services;

import com.southsystem.config.ConfiguracaoApi;
import com.southsystem.models.RespostaImagemAleatoria;
import com.southsystem.models.RespostaImagensRaca;
import com.southsystem.models.RespostaListaRacas;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Serviço para gerenciar operações da Dog API
 * Implementa o padrão Service Object para encapsular lógica de negócio
 */
public class ServicoDogApi {

    /**
     * Método helper para exibir resposta de forma legível
     */
    private void exibirRespostaDetalhada(Response response, String endpoint) {
        System.out.println("📡 [RESPOSTA] " + endpoint);
        System.out.println("📊 Status: " + response.getStatusCode());
        System.out.println("⏰ Tempo: " + response.getTime() + "ms");
        System.out.println("📋 Headers: " + response.getHeaders().toString());
        
        String body = response.getBody().asString();
        if (body.length() > 500) {
            System.out.println("📄 Body (primeiros 500 chars): " + body.substring(0, 500) + "...");
        } else {
            System.out.println("📄 Body: " + body);
        }
        System.out.println("─────────────────────────────────");
    }

    /**
     * Busca todas as raças disponíveis na API
     * @return Response com lista de todas as raças e sub-raças
     */
    @Step("Buscar todas as raças disponíveis")
    public Response buscarTodasRacas() {
        System.out.println("📡 [HTTP] GET /breeds/list/all");
        Response response = given()
                .contentType("application/json")
                .log().all()  // Log da requisição
                .when()
                .get("/breeds/list/all")
                .then()
                .log().all()  // Log da resposta
                .extract()
                .response();
        
        exibirRespostaDetalhada(response, "GET /breeds/list/all");
        return response;
    }

    /**
     * Busca todas as raças e converte para objeto
     * @return RespostaListaRacas objeto mapeado
     */
    @Step("Buscar todas as raças e converter para objeto")
    public RespostaListaRacas buscarTodasRacasComoObjeto() {
        return buscarTodasRacas()
                .as(RespostaListaRacas.class);
    }

    /**
     * Busca imagens de uma raça específica
     * @param raca nome da raça para buscar imagens
     * @return Response com lista de URLs de imagens
     */
    @Step("Buscar imagens da raça: {raca}")
    public Response buscarImagensPorRaca(String raca) {
        System.out.println("📡 [HTTP] GET /breed/" + raca + "/images");
        Response response = given()
                .contentType("application/json")
                .pathParam("raca", raca)
                .log().all()  // Log da requisição
                .when()
                .get("/breed/{raca}/images")
                .then()
                .log().all()  // Log da resposta
                .extract()
                .response();
        System.out.println("✅ [HTTP] Status: " + response.getStatusCode());
        return response;
    }

    /**
     * Busca imagens de uma raça e converte para objeto
     * @param raca nome da raça
     * @return RespostaImagensRaca objeto mapeado
     */
    @Step("Buscar imagens da raça {raca} e converter para objeto")
    public RespostaImagensRaca buscarImagensPorRacaComoObjeto(String raca) {
        return buscarImagensPorRaca(raca)
                .as(RespostaImagensRaca.class);
    }

    /**
     * Busca imagens de uma sub-raça específica
     * @param raca nome da raça principal
     * @param subRaca nome da sub-raça
     * @return Response com lista de URLs de imagens
     */
    @Step("Buscar imagens da sub-raça: {raca}/{subRaca}")
    public Response buscarImagensPorSubRaca(String raca, String subRaca) {
        return given()
                .contentType("application/json")
                .pathParam("raca", raca)
                .pathParam("subRaca", subRaca)
                .when()
                .get("/breed/{raca}/{subRaca}/images")
                .then()
                .extract()
                .response();
    }

    /**
     * Busca uma imagem aleatória de qualquer raça
     * @return Response com URL de uma imagem aleatória
     */
    @Step("Buscar imagem aleatória")
    public Response buscarImagemAleatoria() {
        System.out.println("📡 [HTTP] GET /breeds/image/random");
        Response response = given()
                .contentType("application/json")
                .log().all()  // Log da requisição
                .when()
                .get("/breeds/image/random")
                .then()
                .log().all()  // Log da resposta
                .extract()
                .response();
        System.out.println("✅ [HTTP] Status: " + response.getStatusCode());
        return response;
    }

    /**
     * Busca imagem aleatória e converte para objeto
     * @return RespostaImagemAleatoria objeto mapeado
     */
    @Step("Buscar imagem aleatória e converter para objeto")
    public RespostaImagemAleatoria buscarImagemAleatoriaComoObjeto() {
        return buscarImagemAleatoria()
                .as(RespostaImagemAleatoria.class);
    }

    /**
     * Busca múltiplas imagens aleatórias
     * @param quantidade número de imagens para retornar
     * @return Response com lista de URLs de imagens aleatórias
     */
    @Step("Buscar {quantidade} imagens aleatórias")
    public Response buscarMultiplasImagensAleatorias(int quantidade) {
        System.out.println("📡 [HTTP] GET /breeds/image/random/" + quantidade);
        Response response = given()
                .contentType("application/json")
                .pathParam("quantidade", quantidade)
                .log().all()
                .when()
                .get("/breeds/image/random/{quantidade}")
                .then()
                .log().all()
                .extract()
                .response();
        System.out.println("✅ [HTTP] Status: " + response.getStatusCode());
        return response;
    }

    /**
     * Busca uma imagem aleatória de uma raça específica
     * @param raca nome da raça
     * @return Response com URL de imagem aleatória da raça
     */
    @Step("Buscar imagem aleatória da raça: {raca}")
    public Response buscarImagemAleatoriaPorRaca(String raca) {
        System.out.println("📡 [HTTP] GET /breed/" + raca + "/images/random");
        Response response = given()
                .contentType("application/json")
                .pathParam("raca", raca)
                .log().all()
                .when()
                .get("/breed/{raca}/images/random")
                .then()
                .log().all()
                .extract()
                .response();
        System.out.println("✅ [HTTP] Status: " + response.getStatusCode());
        return response;
    }

    /**
     * Busca múltiplas imagens aleatórias de uma raça específica
     * @param raca nome da raça
     * @param quantidade número de imagens
     * @return Response com lista de URLs de imagens da raça
     */
    @Step("Buscar {quantidade} imagens aleatórias da raça: {raca}")
    public Response buscarMultiplasImagensAleatoriasPorRaca(String raca, int quantidade) {
        System.out.println("📡 [HTTP] GET /breed/" + raca + "/images/random/" + quantidade);
        Response response = given()
                .contentType("application/json")
                .pathParam("raca", raca)
                .pathParam("quantidade", quantidade)
                .log().all()
                .when()
                .get("/breed/{raca}/images/random/{quantidade}")
                .then()
                .log().all()
                .extract()
                .response();
        System.out.println("✅ [HTTP] Status: " + response.getStatusCode());
        return response;
    }
}