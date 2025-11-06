# Link Minify

**URL shortener app para Android**

Aplicação Android que permite encurtar URLs e visualizar histórico de links recentemente encurtados.

## 📱 Features

- Encurtamento de URLs via API REST
- Histórico de links encurtados em memória
- Validação de URLs com feedback em tempo real

## 🏗️ Arquitetura

Este projeto utiliza **MVVM + Clean Architecture** seguindo as recomendações
do [Guide to App Architecture](https://developer.android.com/topic/architecture) do Android.

### Por que essa escolha?

**Separação de responsabilidades**

- Camadas bem definidas (UI, Domain, Data)
- UI reactiva desacoplada da lógica de negócio
- Facilita manutenção e escalabilidade

**Testabilidade**

- Lógica de negócio testável sem dependências
- ViewModels isolados com StateFlow
- Casos de uso testáveis com mocks de repositório
- Cobertura de testes unitários e instrumentados

**Gerenciamento de Estado**

- Single source of truth via StateFlow
- Sobrevivência a mudanças de configuração (rotation)
- Estados bem definidos (Idle, Loading, Success, Error)

**Manutenibilidade**

- Código organizado e previsível
- Baixo acoplamento entre camadas
- Facilita onboarding e code review
- Preparado para evolução de requisitos

### Estrutura

```
📦 com.linkminify
├── 📂 presentation
│   ├── ui (Composables/Activities)
│   ├── viewmodel (ViewModels + UI States)
│   └── mapper (UI Models)
├── 📂 domain
│   ├── usecase (Use Cases)
│   ├── entity (Domain models)
│   ├── repository (Repository interface)
│   └── error (Custom exceptions)
└── 📂 data
    ├── repository (Repository implementation)
    ├── source (Retrofit service)
    └── mapper (UI Models)
```

## 🛠️ Tech Stack

- **UI**: Jetpack Compose
- **Arquitetura**: MVVM + Clean Architecture
- **Async**: Kotlin Coroutines
- **DI**: Koin
- **Network**: Retrofit + OkHttp
- **Tests**: JUnit5, MockK, Compose Test

## ✅ Critérios Atendidos

- ✅ Arquitetura com separação de conceitos
- ✅ Testes unitários (ViewModels, UseCases, Repository)
- ✅ Testes de UI (Compose)
- ✅ Gerenciamento de estado robusto
- ✅ Código organizado sem code smells
- ✅ Lint warnings zerados

## Trade-offs de Arquitetura

### Armazenamento do URL Original

**Opção 1: Guardar apenas alias + shortUrl**
✅ Economia de memória (URLs podem ser muito longos)
✅ Usa endpoint GET para buscar original sob demanda
❌ Latência ao clicar (chamada de rede adicional)
❌ Permite URLs duplicadas

**Opção 2: Guardar alias + shortUrl + originalUrl**
✅ Abertura instantânea (sem latência)
✅ Prevenção de duplicatas
✅ Funciona offline
❌ Maior consumo de memória
❌ Endpoint GET não é utilizado

### Decisão Implementada

**Opção 2** foi escolhida priorizando UX (zero latência) e prevenção de duplicatas,
considerando que o escopo é limitado e o consumo de memória é aceitável.

## Limitações Conhecidas

**Sem persistência**: Links só existem em memória durante a sessão do app

- Trade-off consciente para MVP: priorizou UX (zero latência) vs durabilidade
- Em produção: implementar Room

# Guia de Uso do ktlint

Este projeto utiliza o **ktlint** para garantir a qualidade do código e manter um padrão consistente de formatação
Kotlin.

## Configuração

O ktlint foi configurado com:

- **Plugin**: `jlleitschuh/ktlint-gradle` versão 12.1.2
- **Versão do ktlint**: 1.5.0
- **Modo Android**: Habilitado
- **Falha em violações**: Sim (ignoreFailures = false)
- **Relatórios**: Plain text e Checkstyle XML

### Configurações Personalizadas (.editorconfig)

O arquivo `.editorconfig` define:

- Max line length: 120 caracteres
- Trailing commas: Habilitadas
- Função Composable: Ignora regra de naming
- Wildcard imports: Desabilitados
- Filename rules: Desabilitadas

## Comandos Disponíveis

### Verificar Code Smells

Executa a análise estática sem modificar arquivos:

```bash
./gradlew ktlintCheck
```

Este comando verifica todos os source sets (main, test, androidTest) e gera relatórios em:

- `/app/build/reports/ktlint/`

### 2. Auto-Correção de Code Smells

Corrige automaticamente os problemas que podem ser resolvidos:

```bash
./gradlew ktlintFormat
```

Este comando:

- Remove imports não utilizados
- Adiciona trailing commas
- Corrige indentação
- Formata quebras de linha
- E outros problemas auto-corrigíveis

## Relatórios

Os relatórios são gerados em dois formatos:

1. **Plain Text** (legível): `app/build/reports/ktlint/[task-name]/[task-name].txt`
2. **Checkstyle XML** (CI/CD): `app/build/reports/ktlint/[task-name]/[task-name].xml`

## Ignorar Regras Específicas

Para desabilitar regras específicas em um arquivo, adicione no topo do arquivo:

```kotlin
@file:Suppress("ktlint:standard:max-line-length") a
```

Ou para uma função/classe específica:

```kotlin
@Suppress("ktlint:standard:function-naming")
fun MyComposableFunction() {
}
```

## Regras Principais Verificadas

O ktlint verifica:

1. **Formatação**: Indentação, espaçamento, quebras de linha
2. **Naming Conventions**: Nomes de classes, funções, propriedades
3. **Imports**: Imports não utilizados, ordem de imports
4. **Code Style**: Trailing commas, chain method continuation
5. **Complexity**: Max line length, expressões multi-linha
6. **Best Practices**: Backing properties, function expression body

## Troubleshooting

### Build falha após executar ktlintFormat

Alguns problemas não podem ser auto-corrigidos e precisam de intervenção manual:

- **backing-property-naming**: Propriedades backing devem ter propriedades públicas correspondentes
- **max-line-length**: Linhas com mais de 120 caracteres precisam ser quebradas manualmente
- **indent**: Problemas complexos de indentação em casos específicos

### Verificar apenas sem falhar o build

Para análise sem falhar o build, modifique temporariamente em `build.gradle`:

```groovy
ktlint {
    ignoreFailures = true
}
```
