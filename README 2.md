# AsteroidsFX

Modulopdelt CBSE/JPMS-version af Asteroids bygget som et Maven multi-module projekt.

## Hvor ligger lab exercises?

De laboratorierelaterede dele er samlet i samme repository, men de er organiseret, så de kan fremvises tydeligt:

- `lab-showcase/` indeholder separate, korte README-filer for de vigtigste lab-emner:
  - `SpringLab`
  - `MicroServiceLab`
  - `TestLab`
- `til cbse eksamen/` indeholder kursusmateriale, slides og noter, som projektet er bygget ud fra.
- Selve hovedprojektet ligger i modul-mapperne `Core`, `Player`, `Bullet`, `Enemy`, `Asteroids`, `Collision` og `ScoringService`.

Kort sagt: labs er både afspejlet i den faktiske kode og samlet i `lab-showcase/`, så de kan vises separat uden at ændre hovedprojektet.

## Projektstruktur

- `Core`: fælles data, SPI/API, JavaFX-applikation, Spring bootstrap og runtime.
- `Player`: player-plugin og player-bevægelse.
- `Bullet`: bullet-plugin og `BulletSPI`.
- `Enemy`: enemy-plugin, pakket til `plugins/` og loaded via `ModuleLayer`.
- `Asteroids`: asteroid-plugin og asteroid-bevægelse.
- `Collision`: post-processor til kollisionshåndtering.
- `ScoringService`: separat Spring Boot microservice til score og highscore.
- `docs/`: rapportudkast, portfolio og dokumentation.
- `lab-showcase/`: separat fremvisning af lab-relaterede emner.
- `til cbse eksamen/`: kursusmateriale og eksamensnoter.

## Krav

- Java 17 eller nyere
- Maven 3.8 eller nyere
- Et desktopmiljø med JavaFX-vindue

## Build

Kør fra projektroden:

```bash
sh scripts/build-all.sh
```

Det gør følgende:

- kompilerer alle moduler
- kører tests
- bygger JPMS-artefakterne
- samler de faste runtime-moduler i `game-mods/`
- installerer `Enemy` som separat plugin i `plugins/`

## Sådan starter du spillet

Den anbefalede måde er:

```bash
sh run-game.sh
```

`run-game.sh` starter altid fra `game-mods/`, mens valgfrie plugins læses fra `plugins/`.

Alternativt kan du forsøge:

```bash
mvn -N exec:exec
```

`-N` er vigtig, fordi almindelig `mvn exec:exec` i en multi-module struktur ellers forsøger at køre i flere moduler.

## Sådan starter du scoring microservicen

Den anbefalede måde er:

```bash
sh run-scoring-service.sh
```

Alternativt:

```bash
mvn -pl ScoringService spring-boot:run
```

Microservicen eksponerer:

- `POST /score`
- `GET /score`
- `GET /score/highscore`

Scoring-klienten i `Core` bruger Spring `RestClient`. Det er valgt frem for `RestTemplate`, fordi det er den nyere og anbefalede synkrone REST-klient i Spring.

## Manuel guide

### 1. Byg projektet

```bash
mvn clean test package
```

### 2. Start scoring microservicen

```bash
sh run-scoring-service.sh
```

### 3. Start spillet

```bash
sh run-game.sh
```

### 4. Spil grundlæggende flow

- Brug `Start`-knappen i vinduet.
- Bevæg spilleren og skyd asteroider.
- Se score, level og health opdatere i HUD’en.
- Hvis du dør, kan du bruge `Reset`.

### 5. Test at microservicen virker

```bash
curl http://localhost:8080/score
curl http://localhost:8080/score/highscore
```

Du kan også poste en score manuelt:

```bash
curl -X POST http://localhost:8080/score \
  -H 'Content-Type: application/json' \
  -d '{"score":500}'
```

### 6. Test at plugin kan fjernes uden recompilation

Fjern plugin midlertidigt:

```bash
sh scripts/disable-enemy-plugin.sh
```

Start spillet igen:

```bash
sh run-game.sh
```

Nu skal `Enemy` være væk, men resten af spillet skal stadig fungere.

### 7. Installer plugin igen

```bash
sh scripts/enable-enemy-plugin.sh
```

Start spillet igen:

```bash
sh run-game.sh
```

Nu er `Enemy` tilbage.

## Hvad demonstrerer projektet?

- Komponentbaseret arkitektur med løst koblede moduler.
- JPMS med `requires`, `exports`, `uses` og `provides ... with`.
- `ServiceLoader` og runtime plugin-loading via `ModuleLayer`.
- Spring container og dependency injection i `Core`.
- Separat Spring Boot microservice til scoring.
- Testbarhed af gameplay-logik og microservice-logik.

## Dokumentation

- `docs/report-draft.md`: rapportudkast på dansk.
- `docs/portfolio.md`: kort eksamensnote.
- `lab-showcase/README.md`: samlet oversigt over lab-emnerne.

## Demo plan

1. Kør `sh scripts/build-all.sh`.
2. Start scoring microservicen.
3. Start spillet og vis, at score og highscore kan opdateres.
4. Fjern `Enemy`-plugin fra `plugins/`.
5. Start spillet igen og vis, at fjenden er væk uden recompilation.
6. Læg `Enemy` tilbage og start igen.

En mere detaljeret optageguide ligger i [VIDEO-DEMO.md](/Users/abishiyambaskaran/Desktop/Projekter/CBSE-projekt/AsteroidsFX/VIDEO-DEMO.md).

## Troubleshooting

- Hvis spillet ikke starter, så tjek at du bruger Java 17+.
- Hvis JavaFX-vinduet ikke åbner, så tjek at du kører fra projektroden.
- Hvis scoring ikke opdateres, så tjek at `ScoringService` kører på `localhost:8080`.
- Hvis `Enemy` stadig dukker op efter du har fjernet pluginet, så tjek at du fjernede den rigtige jar fra `plugins/`.
