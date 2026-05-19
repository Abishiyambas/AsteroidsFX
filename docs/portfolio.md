# Portfolio Note

## Component-Based Architecture

Projektet er opdelt i selvstændige komponenter som Maven- og JPMS-moduler:

- `Core`
- `Player`
- `Bullet`
- `Asteroids`
- `Collision`
- `Enemy`

`Core` indeholder den fælles runtime, game loop, fælles data og service-kontrakter. De øvrige moduler leverer funktionalitet som udskiftelige komponenter.

## Provided And Required Interfaces

Projektet bruger tre centrale service-interfaces:

- `IGamePluginService`
- `IEntityProcessorService`
- `IPostEntityProcessorService`

Derudover bruges `BulletSPI` som et required/provided interface mellem shooter-komponenter og bullet-komponenten.

Eksempler:

- `Player` provides `IGamePluginService` og `IEntityProcessorService`
- `Bullet` provides `BulletSPI`
- `Collision` provides `IPostEntityProcessorService`
- `Player` og `Enemy` uses `BulletSPI`

Det betyder, at `Core` og de enkelte komponenter arbejder mod interfaces i stedet for konkrete implementationer.

## JPMS And module-info.java

Hvert modul har en `module-info.java`, som deklarerer:

- `requires`
- `exports`
- `uses`
- `provides ... with`

Det gør afhængighederne eksplicitte og understøtter den komponentbaserede opdeling. `Core` kender fx kun services gennem `uses`, mens implementationsmodulerne registrerer sig gennem `provides`.

## ServiceLoader

Runtime assembly sker med Java `ServiceLoader`.

`Core` loader:

- `IGamePluginService`
- `IEntityProcessorService`
- `IPostEntityProcessorService`

Dermed kan nye komponenter installeres som moduler uden at ændre `Core`-koden.

## ModuleLayer And plugins/

`Enemy` demonstrerer dynamisk plugin-loading via `ModuleLayer`.

- Standardmoduler ligger i `mods-mvn/`
- Eksterne plugins kan ligge i `plugins/`
- `Core` opretter en separat `ModuleLayer` for plugin-mappen

Det demonstrerer installation og afinstallation uden rekompilering af `Core`.

## Installation And Removal Without Recompilation

Demonstrationen er:

1. Byg projektet med `mvn clean test package`
2. Start spillet med `sh run-game.sh`
3. Bekræft at `Enemy` er aktiv, fordi `plugins/Enemy-1.0-SNAPSHOT.jar` findes
4. Fjern eller omdøb plugin-jar i `plugins/`
5. Start spillet igen uden ny kompilering
6. Bekræft at `Enemy` nu er væk
7. Læg plugin-jar tilbage
8. Start spillet igen og vis at `Enemy` er tilbage

Pointen er, at `Core` ikke genbygges. Kun tilstedeværelsen af plugin-modulet i `plugins/` ændres.

## Testing Strategy

Projektet bruger primært enhedstest af kerneadfærd:

- player movement
- collision handling
- score/level progression

Det er valgt, fordi spillet er interaktivt, og store dele af adfærden er lettest at verificere gennem små deterministiske tests kombineret med manuel gameplay-test.

## Spring As Relevant Theory

Spring er relevant som teori, fordi det også handler om:

- komponenter
- dependency injection
- wiring
- løst koblede systemer

Dette projekt bruger ikke Spring som framework. I stedet bruges JPMS, `ServiceLoader` og `ModuleLayer` til at opnå samme overordnede CBSE-principper i en modulær Java-applikation.

Et godt eksamenspoint er derfor:

“Spring var relevant i undervisningen som eksempel på komponent-wiring og dependency injection. I dette projekt demonstreres de samme principper med JPMS, ServiceLoader og ModuleLayer.”

## Gameplay Balance

Spillet er finjusteret med tre niveauer:

- Level 1: roligt tempo, få asteroids, lav enemy fire rate
- Level 2: tydeligt højere pres
- Level 3: hurtigere asteroids, flere aktive trusler og hurtigere enemy fire

Målet har været at gøre progressionen tydelig uden at gøre level 3 urimeligt svært.
