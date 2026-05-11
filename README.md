# FruitNinjaMicrocontrolador

Projeto de jogo desenvolvido em Java com [LibGDX](https://libgdx.com/), inspirado na ideia de acertar alvos que aparecem na tela para somar pontos. O jogo possui uma tela de menu, uma fase principal, pontuacao em tempo real e elementos com penalidades.

## Sobre o jogo

Na tela inicial, o jogador clica em **INICIAR** para comecar a partida. Durante a fase, diferentes objetos sobem pela tela:

- Fantasmas normais: somam pontos ao serem atingidos.
- Fantasmas vermelhos: reduzem a pontuacao.
- Bombas: aplicam uma penalidade maior.

A velocidade do jogo aumenta gradualmente conforme a pontuacao sobe, deixando a partida mais desafiadora.

## Tecnologias utilizadas

- Java 8+
- Gradle
- LibGDX 1.14.0
- LWJGL3 para execucao desktop

## Estrutura do projeto

```text
.
|-- README.md
`-- Projeto_jogos-main/
    |-- assets/                  # Imagens e recursos do jogo
    |   |-- fazes/               # Fundos das fases
    |   |-- logo/                # Logo exibida no menu
    |   `-- personas/            # Sprites dos personagens e obstaculos
    |-- core/                    # Logica principal do jogo
    |   `-- src/main/java/br/com/eduardopirolo/fruitninja/
    |       |-- Main.java        # Controle de estados: menu e jogo
    |       |-- Menu.java        # Tela inicial
    |       `-- faze1.java       # Primeira fase e regras de pontuacao
    |-- lwjgl3/                  # Launcher desktop com LWJGL3
    |-- build.gradle             # Configuracao geral do Gradle
    |-- gradle.properties        # Versoes e propriedades do projeto
    `-- settings.gradle          # Configuracao dos modulos
```

## Como executar

Entre na pasta do projeto:

```bash
cd Projeto_jogos-main
```

Antes de executar, verifique se o Java esta instalado:

```bash
java -version
```

No Windows, rode:

```bash
gradlew.bat lwjgl3:run
```

No Linux ou macOS, rode:

```bash
./gradlew lwjgl3:run
```

## Como gerar o arquivo JAR

Para gerar um JAR executavel da versao desktop:

```bash
gradlew.bat lwjgl3:jar
```

No Linux ou macOS:

```bash
./gradlew lwjgl3:jar
```

O arquivo gerado fica em:

```text
lwjgl3/build/libs/
```

## Comandos uteis

```bash
gradlew.bat build
```

Compila todos os modulos do projeto.

```bash
gradlew.bat clean
```

Remove os arquivos de build gerados.

```bash
gradlew.bat test
```

Executa os testes, caso existam testes configurados.

## Controles

- Clique em **INICIAR** para comecar.
- Mova o mouse sobre os alvos durante a fase para interagir com eles.
- Acerte fantasmas normais para aumentar a pontuacao.
- Evite fantasmas vermelhos e bombas para nao perder pontos.

## Assets

Os recursos visuais do jogo ficam na pasta `Projeto_jogos-main/assets/`. O modulo `lwjgl3` configura essa pasta como diretorio de recursos durante a execucao.

## Autor

Projeto desenvolvido por Eduardo Pirolo.
