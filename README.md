# Create: Hamsters Compat

<img src="ico.jpg" alt="Create: Hamsters Compat" width="128">

[Русская версия](README.ru.md)

Compatibility mod for [Create](https://modrinth.com/mod/create) and [Hamsters](https://modrinth.com/mod/hamsters) on **Minecraft 1.20.1**.

Place a Create shaft against the hub of a hamster wheel. When a hamster runs, the shaft turns the same way as the wheel.

## Features

- Shafts connect only on the spoke hub of a `hamsters:hamster_wheel`, not on the stand side.
- Speed and stress default to the Create hand crank: **32 RPM** and **8 SU/RPM** (256 SU).
- Right-click an empty wheel while holding a hamster to put it in.
- Right-click an occupied wheel with an empty hand to take the hamster back.
- By default hamsters cannot leave the wheel on their own (server config).
- First time a hamster powers a connected shaft, players nearby get **Exploitation...**
  (English or Russian follows the game language).

## Loader notes (1.20.1)

Create 6.0.8 lists both Forge and NeoForge for 1.20.1. Hamsters 1.0.3 ships a Forge jar. NeoForge 20.1.x still uses `net.minecraftforge` and can load that jar. The public NeoForge 1.20.1 installer was later withdrawn, so this project builds with ForgeGradle / Forge 47.x. The same compat jar is meant to run on Forge 47 and NeoForge 20.1. The entrypoint is `javafml`; Kotlin for Forge stays a required library.

A 1.21.1 port will move to NeoForge packages, `neoforge.mods.toml`, Kotlin for Forge 5.x, and the Hamsters `1.21.1-Neo` line. Gameplay rules live in `src/logic` so that port stays small.

## Requirements

| Mod | Version |
| --- | --- |
| Minecraft | 1.20.1 |
| Forge 47.x or NeoForge 20.1.x | 47+ |
| [Kotlin for Forge](https://modrinth.com/mod/ordsPcFz) | 4.12.0 |
| [Create](https://modrinth.com/mod/create) | 6.0.8 |
| [Hamsters](https://modrinth.com/mod/hamsters) | 1.20.1-1.0.3 (Forge) |
| [GeckoLib](https://modrinth.com/mod/geckolib) | 4.4.9 (Hamsters dependency) |

## Config

Mods list → Create: Hamsters Compat → Config. Values come from the server: singleplayer and LAN host use the local world, a dedicated server ignores client changes.

- Current world: `saves/<world>/serverconfig/hamsterscreatecompat-server.toml`
- Template for new worlds: `defaultconfigs/hamsterscreatecompat-server.toml`

| Key | Default | Meaning |
| --- | --- | --- |
| `hamsterExitsWheelOnItsOwn` | `false` | If false, hamsters stay until a player takes them out. |
| `generatedRpm` | `32` | RPM while a hamster is running. |
| `stressCapacityPerRpm` | `8.0` | Create stress capacity per RPM (hand crank default). |

## Development

JDK 17:

```bash
./gradlew -b logic.gradle test
./gradlew build collectReleaseArtifacts
```

`logicTest` does not launch Minecraft. `build` does a full ForgeGradle remap on first run.

## Releases

Jars live on [GitHub Releases](https://github.com/Nergan/hamsters-create-compat-mod/releases/latest). A push to `main` updates the files on the current version’s release. A new `mod_version` in `gradle.properties` creates a new `vX.Y.Z` release.

The workflow builds the compat jar and attaches Create, Hamsters, Kotlin for Forge, and GeckoLib. GitHub shows a SHA-256 digest next to each file. Do not install `*-sources.jar`.

## License

[MPL-2.0](LICENSE). Create, Hamsters, Kotlin for Forge, and GeckoLib stay under their own licenses.
