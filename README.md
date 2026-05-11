# FruitNinjaMicrocontrolador

Projeto inicial de um jogo em Java usando [libGDX](https://libgdx.com/), com foco em execucao desktop via LWJGL3. A estrutura foi gerada com o [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) e ja vem separada em modulos para manter a logica do jogo isolada da plataforma de execucao.

## Sobre o projeto

Esta base serve como ponto de partida para o desenvolvimento de um jogo inspirado em Fruit Ninja, com possibilidade de evolucao para integracao com microcontrolador. No estado atual, o projeto contem a estrutura essencial de uma aplicacao libGDX:

- criacao da janela desktop;
- loop principal de renderizacao;
- carregamento de textura;
- pasta centralizada para assets;
- empacotamento via Gradle.

## Tecnologias

- Java/libGDX
- libGDX 1.14.0
- LWJGL3
- Gradle Wrapper 9.4.0

## Estrutura de pastas

```text
.
|-- assets/                 # Imagens, sons e outros recursos usados pelo jogo
|-- core/                   # Codigo principal compartilhado da aplicacao
|   `-- src/main/java/
|-- lwjgl3/                 # Launcher desktop usando LWJGL3
|   `-- src/main/java/
|-- build.gradle            # Configuracao Gradle raiz
|-- settings.gradle         # Modulos do projeto
|-- gradle.properties       # Versoes e propriedades do build
|-- gradlew                 # Gradle Wrapper para Linux/macOS
`-- gradlew.bat             # Gradle Wrapper para Windows
```

## Pre-requisitos

- JDK instalado e configurado no `PATH`.
- IDE com suporte a Gradle, como IntelliJ IDEA, Eclipse ou VS Code.

O codigo do projeto esta configurado com compatibilidade Java 8. Como o wrapper usa Gradle 9.4.0, use uma versao de JDK compativel com essa versao do Gradle para executar os comandos de build.

## Como executar

No Windows:

```powershell
.\gradlew.bat lwjgl3:run
```

No Linux ou macOS:

```bash
./gradlew lwjgl3:run
```

O jogo sera iniciado em uma janela desktop de 640x480 pixels.

## Como gerar o JAR

Para gerar um arquivo `.jar` executavel:

```powershell
.\gradlew.bat lwjgl3:jar
```

O arquivo gerado ficara em:

```text
lwjgl3/build/libs/FruitNinjaMicrocontrolador-1.0.0.jar
```

Tambem e possivel gerar builds especificos por sistema operacional:

```powershell
.\gradlew.bat lwjgl3:jarWin
.\gradlew.bat lwjgl3:jarLinux
.\gradlew.bat lwjgl3:jarMac
```

## Comandos uteis

```powershell
.\gradlew.bat clean
.\gradlew.bat build
.\gradlew.bat lwjgl3:run
.\gradlew.bat lwjgl3:jar
```

No Linux/macOS, substitua `.\gradlew.bat` por `./gradlew`.

## Desenvolvimento

A classe principal do jogo fica em:

```text
core/src/main/java/br/com/eduardopirolo/fruitninja/Main.java
```

O launcher desktop fica em:

```text
lwjgl3/src/main/java/br/com/eduardopirolo/fruitninja/lwjgl3/Lwjgl3Launcher.java
```

Para adicionar imagens, sons ou outros recursos, coloque os arquivos na pasta `assets/`. O modulo `lwjgl3` ja esta configurado para usar essa pasta como diretorio de recursos durante a execucao.

## Importando em uma IDE

O projeto pode ser importado como um projeto Gradle.

Passos recomendados:

1. Abra a pasta raiz do projeto na IDE.
2. Aguarde a sincronizacao do Gradle.
3. Execute a task `lwjgl3:run` ou rode a classe `Lwjgl3Launcher`.

## Observacoes

- A logica compartilhada do jogo deve ficar no modulo `core`.
- Codigo especifico de desktop deve ficar no modulo `lwjgl3`.
- Os assets devem ser referenciados pelo nome do arquivo dentro da pasta `assets/`.
- Caso o jogo nao encontre uma textura, confira se o nome usado no codigo corresponde exatamente ao arquivo existente em `assets/`.

## Licenca

Este projeto ainda nao possui uma licenca definida.
