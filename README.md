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
│   ├── entiry (Domain models)
│   ├── repository (Repository interface)
│   └── error (Custom exceptions)
└── 📂 data
    ├── repository (Repository implementation)
    ├── source (Retrofit service)
    │     └── dto (API models)
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
- ❗ Lint warnings zerados

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