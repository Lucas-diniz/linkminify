# Guia de Uso do ktlint

Este projeto utiliza o **ktlint** para garantir a qualidade do código e manter um padrão consistente de formatação Kotlin.

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

### Verificar Source Set Específico

Você pode rodar ktlint em source sets específicos:

```bash
# Apenas código principal
./gradlew ktlintMainSourceSetCheck

# Apenas testes unitários
./gradlew ktlintTestSourceSetCheck

# Apenas testes instrumentados
./gradlew ktlintAndroidTestSourceSetCheck
```

### Formatar Source Set Específico

```bash
# Formatar apenas código principal
./gradlew ktlintMainSourceSetFormat

# Formatar apenas testes
./gradlew ktlintTestSourceSetFormat
```

## Relatórios

Os relatórios são gerados em dois formatos:

1. **Plain Text** (legível): `app/build/reports/ktlint/[task-name]/[task-name].txt`
2. **Checkstyle XML** (CI/CD): `app/build/reports/ktlint/[task-name]/[task-name].xml`

## Integração com CI/CD

Para integrar com pipelines de CI/CD, adicione ao seu workflow:

```yaml
- name: Run ktlint
  run: ./gradlew ktlintCheck
```

O build falhará automaticamente se houver violações de código.

## Integração com Git Hooks

Para executar ktlint antes de cada commit, adicione ao `.git/hooks/pre-commit`:

```bash
#!/bin/sh
./gradlew ktlintFormat --daemon
git add -u
```

## Ignorar Regras Específicas

Para desabilitar regras específicas em um arquivo, adicione no topo do arquivo:

```kotlin
@file:Suppress("ktlint:standard:max-line-length")a
```

Ou para uma função/classe específica:

```kotlin
@Suppress("ktlint:standard:function-naming")
fun MyComposableFunction() { }
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
