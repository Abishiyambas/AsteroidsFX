# AsteroidsFX

Modulopdelt CBSE/JPMS-udgave af Asteroids bygget som et Maven multi-module projekt.

## Struktur

- `Core`: fælles data, SPI/API og JavaFX-applikationen.
- `Player`: player-plugin og player-bevægelse.
- `Bullet`: bullet-plugin og `BulletSPI`.
- `Enemy`: enemy-plugin, pakket til `plugins/` og loaded via `ModuleLayer`.
- `Asteroids`: asteroid-plugin og asteroid-bevægelse.
- `Collision`: post-processor til kollisionshåndtering.
- `til cbse eksamen`: kursusmateriale, labs og eksamensnoter.

## Repository Note

Dette repository fungerer som hovedprojekt for AsteroidsFX og samler samtidig det relevante laboratoriearbejde, der ligger til grund for den endelige CBSE-løsning.

## CBSE/JPMS-emner dækket i projektet

- Komponentopdeling med løst koblede moduler.
- Provided interfaces via `provides ... with`.
- Required interfaces via `uses` og `ServiceLoader`.
- `IGamePluginService`, `IEntityProcessorService` og `IPostEntityProcessorService`.
- Separat plugin-layer til eksterne moduler i `plugins/`.
- Enhedstest af komponentadfærd og kollisionslogik.

## Krav

- Java 17+
- Maven 3.8+

## Build

Kør fra projektroden:

```bash
mvn clean test package
```

Det kompilerer modulerne, kører tests, kopierer de almindelige moduler til `mods-mvn/` og kopierer `Enemy` til `plugins/`.

## Kørsel

Kør fra projektroden:

```bash
mvn -N exec:exec
```

Alternativt kan du starte spillet direkte uden reactor-problemet:

```bash
sh run-game.sh
```

Bemærk: almindelig `mvn exec:exec` over hele reactoren prøver også at køre målet i undermodulerne. Brug derfor `-N` eller scriptet ovenfor. Spillet kræver et desktopmiljø med JavaFX-vindue.

## Demo Plan

### Build

```bash
mvn clean test package
```

### Start With Enemy Plugin

Bekræft at plugin findes:

```bash
ls plugins
```

Start spillet:

```bash
sh run-game.sh
```

### Remove Enemy Without Recompilation

Fjern plugin midlertidigt:

```bash
mv plugins/Enemy-1.0-SNAPSHOT.jar plugins/Enemy-1.0-SNAPSHOT.jar.off
```

Start spillet igen:

```bash
sh run-game.sh
```

Nu skal `Enemy` være væk, mens resten af spillet stadig virker. `Core` er ikke rekompileret.

### Install Enemy Again Without Recompilation

Læg plugin tilbage:

```bash
mv plugins/Enemy-1.0-SNAPSHOT.jar.off plugins/Enemy-1.0-SNAPSHOT.jar
```

Start spillet igen:

```bash
sh run-game.sh
```

Nu er `Enemy` tilbage.

### Portfolio

En kort eksamensforklaring findes i:

`docs/portfolio.md`
