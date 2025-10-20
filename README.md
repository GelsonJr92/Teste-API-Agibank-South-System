# 🐕 South System - Dog API Automation Tests

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue)
![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-green)
![JUnit](https://img.shields.io/badge/JUnit-5.10.1-red)
![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow)

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como parte do teste técnico para a **South System**, implementando uma suite completa de automação de testes para a **Dog CEO API** (https://dog.ceo/api). 

A automação utiliza as mesmas estruturas e ferramentas do projeto Carrefour, seguindo as melhores práticas de desenvolvimento de testes automatizados e padrões de mercado.

## 🎯 Objetivos

- ✅ Automatizar testes para os principais endpoints da Dog API
- ✅ Implementar cobertura de testes abrangente e robusta  
- ✅ Seguir padrões de qualidade e boas práticas
- ✅ Gerar relatórios detalhados e visuais
- ✅ Configurar pipeline de CI/CD
- ✅ Documentar o projeto de forma clara e completa

## 🔧 Stack Tecnológica

### Core Technologies
- **Java 17**: Linguagem de programação principal
- **Maven 3.9+**: Gerenciamento de dependências e build
- **RestAssured 5.4.0**: Framework para testes de API REST
- **JUnit 5.10.1**: Framework de testes unitários
- **Allure Reports 2.25.0**: Geração de relatórios HTML visuais

### Ferramentas Auxiliares
- **Lombok 1.18.30**: Redução de boilerplate code
- **Jackson 2.16.1**: Serialização/deserialização JSON
- **GitHub Actions**: Pipeline de CI/CD
- **Maven Surefire**: Execução dos testes
- **Maven AntRun**: Automação de tarefas

## 🏗️ Arquitetura do Projeto

```
src/
├── main/java/com/southsystem/
│   ├── config/
│   │   └── ConfiguracaoApi.java          # Configurações da API
│   └── models/
│       ├── RespostaListaRacas.java       # Modelo para lista de raças
│       ├── RespostaImagensRaca.java      # Modelo para imagens por raça
│       └── RespostaImagemAleatoria.java  # Modelo para imagem aleatória
│
└── test/java/com/southsystem/
    ├── services/
    │   └── ServicoDogApi.java            # Service Object Pattern
    └── tests/
        ├── TesteBase.java                # Classe base para testes
        ├── TesteListagemRacas.java       # Testes de listagem de raças
        ├── TesteImagensPorRaca.java      # Testes de imagens por raça
        ├── TesteImagensAleatorias.java   # Testes de imagens aleatórias
        └── TesteIntegracao.java          # Testes de integração
```

### 🎨 Padrões Implementados

1. **Service Object Pattern**: Encapsulamento da lógica de negócio nos services
2. **Page Object Model**: Adaptado para APIs como Service Object
3. **Builder Pattern**: Construção fluente de requisições com RestAssured
4. **Data Transfer Objects (DTOs)**: Modelos para mapeamento de respostas JSON

## 🧪 Cobertura de Testes

### Endpoints Testados

| Endpoint | Método | Descrição | Testes |
|----------|--------|-----------|---------|
| `/breeds/list/all` | GET | Lista todas as raças | 8 testes |
| `/breed/{breed}/images` | GET | Imagens por raça específica | 6 testes |
| `/breeds/image/random` | GET | Imagem aleatória geral | 10 testes |
| **Integração** | - | Testes end-to-end | 6 testes |

### Total: **30 testes automatizados**

### 📊 Cenários de Teste

#### 🐕 Listagem de Raças (`TesteListagemRacas`)
- Busca completa de raças com sucesso
- Validação da estrutura JSON da resposta
- Verificação de tipos de dados e campos obrigatórios
- Validação de sub-raças quando disponíveis
- Teste de performance (tempo de resposta)
- Validação de headers HTTP
- Verificação de codificação de caracteres
- Formato dos nomes das raças

#### 🖼️ Imagens por Raça (`TesteImagensPorRaca`)
- Busca de imagens por raça específica
- Validação da estrutura da resposta JSON
- Verificação de URLs válidas das imagens
- Testes com diferentes raças válidas
- Tratamento de raças inexistentes
- Testes de performance

#### 🎲 Imagens Aleatórias (`TesteImagensAleatorias`)
- Busca de imagem aleatória geral
- Validação da estrutura da resposta
- Verificação de URLs válidas
- Teste de aleatoriedade (múltiplas chamadas)
- Busca de múltiplas imagens aleatórias
- Imagens aleatórias por raça específica
- Testes de performance e headers
- Validação de limites e parâmetros

#### 🔄 Integração (`TesteIntegracao`)
- Fluxo completo: buscar raças → selecionar → buscar imagens
- Validação de diferentes tipos de imagens aleatórias
- Consistência entre endpoints
- Teste de robustez com diferentes cenários
- Estabilidade da API
- Validação de tipos de conteúdo

## 🚀 Como Executar

### Pré-requisitos

```bash
# Java 17 ou superior
java -version

# Maven 3.9 ou superior  
mvn -version

# Git (opcional)
git --version
```

### Executando os Testes

```bash
# Clonar o repositório (se aplicável)
git clone <repository-url>
cd dog-api-automation-tests

# Executar todos os testes
mvn clean test

# Executar classe específica
mvn test -Dtest=TesteListagemRacas

# Executar teste específico
mvn test -Dtest=TesteListagemRacas#deveRetornarListaCompletaDeRacasComSucesso

# Executar com debug
mvn test -X

# Executar em paralelo
mvn test -Dparallel=methods
```

### 📊 Visualizando Relatórios

```bash
# Gerar e abrir relatório Allure
mvn allure:serve

# Apenas gerar relatório
mvn allure:report

# Localização do relatório
target/allure-results/allure-report/index.html
```

## 📈 Relatórios e Evidências

### Allure Reports
O projeto gera relatórios HTML interativos com:

- **Dashboard**: Visão geral dos resultados
- **Suites**: Organização por features e stories
- **Gráficos**: Distribuição de resultados e trends
- **Timeline**: Linha temporal da execução
- **Categories**: Categorização de falhas
- **Environment**: Informações do ambiente de teste

### Configurações do Allure
- Épicos, Features e Stories organizados por funcionalidade
- Steps detalhados para cada ação
- Anexos automáticos em caso de falha
- Categorização de severidade (Critical, Normal, Minor)
- Descrições detalhadas para cada teste

## 🔄 CI/CD Pipeline

### GitHub Actions
```yaml
# Workflow configurado para:
- Execução automática em push/PR
- Múltiplas versões do Java
- Cache de dependências Maven
- Geração automática de relatórios
- Notificações de falha
```

### Triggers
- **Push** para branch main/master
- **Pull Requests** 
- **Schedule** diário às 6:00 UTC
- **Manual** via workflow dispatch

## ⚙️ Configurações

### Environment Properties
```properties
# src/test/resources/environment.properties
test.author=South System QA Team
test.environment=Production Dog API
test.version=1.0.0
api.base.url=https://dog.ceo/api
```

### Configurações Maven
- **Java Target**: 17
- **Encoding**: UTF-8
- **Parallel Execution**: Suportado
- **Failsafe Integration**: Configurado
- **Allure Integration**: Automática

## 🛠️ Manutenção e Extensão

### Adicionando Novos Testes

1. **Novo Endpoint**:
```java
// Service method
@Step("Descrição da ação")
public Response novoMetodo() {
    return given()
        .when()
        .get("/novo/endpoint")
        .then()
        .extract()
        .response();
}

// Teste
@Test
@Epic("Dog API") 
@Feature("Nova Feature")
@Story("Nova Story")
void deveTestarNovoEndpoint() {
    // implementação
}
```

2. **Novo Modelo**:
```java
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovoModelo {
    @JsonProperty("field")
    private String campo;
}
```

### Boas Práticas Implementadas

- ✅ Nomenclatura em português para métodos de teste
- ✅ Organização com @Order para execução sequencial
- ✅ Uso de assertAll() para múltiplas validações
- ✅ Steps do Allure para rastreabilidade
- ✅ Tratamento de exceções adequado
- ✅ Configuração centralizada
- ✅ Logs estruturados
- ✅ Timeouts configurados

## 🐛 Troubleshooting

### Problemas Comuns

1. **Erro de conexão com a API**:
```bash
# Verificar conectividade
curl https://dog.ceo/api/breeds/list/all
```

2. **Falhas de timeout**:
```java
// Aumentar timeout no RestAssured
given().timeout(Duration.ofSeconds(30))
```

3. **Problemas com Java/Maven**:
```bash
# Limpar cache Maven
mvn dependency:purge-local-repository

# Recompilar
mvn clean compile
```

## 📞 Suporte

Para dúvidas ou problemas:

1. **Verificar logs**: `target/surefire-reports/`
2. **Consultar relatório Allure**: Detalhes completos das execuções
3. **Validar ambiente**: Java 17+ e Maven 3.9+
4. **Revisar configurações**: Verificar `ConfiguracaoApi.java`

## 📝 Notas de Desenvolvimento

Este projeto foi desenvolvido seguindo:

- **Clean Code**: Código limpo e legível
- **SOLID Principles**: Princípios de design
- **DRY**: Don't Repeat Yourself
- **KISS**: Keep It Simple, Stupid
- **Test Pyramid**: Estrutura de testes bem definida

### Melhorias Futuras

- [ ] Implementação de Data Driven Tests
- [ ] Integração com banco de dados para dados de teste
- [ ] Testes de carga e performance
- [ ] Monitoramento contínuo
- [ ] Integração com ferramentas de APM

---

## 🏆 Resultados

✅ **30 testes** implementados e funcionais  
✅ **100%** de cobertura dos endpoints solicitados  
✅ **Relatórios Allure** com evidências visuais  
✅ **CI/CD Pipeline** configurado  
✅ **Documentação completa** e profissional  
✅ **Boas práticas** aplicadas  

*Desenvolvido com ❤️ para South System*