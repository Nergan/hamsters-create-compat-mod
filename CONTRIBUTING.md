# Contributing

## Build

You need JDK 17.

```bash
./gradlew -b logic.gradle test
./gradlew build
```

`logicTest` covers the loader-agnostic rules (shaft axis, insert/extract, energy, advancement). `build` remaps the Minecraft jar and needs a first-time ForgeGradle setup.

## Layout

- `src/logic/kotlin` — rules that should stay stable when porting to 1.21.1 NeoForge
- `src/main/kotlin` — 1.20.1 Forge / NeoForge 20.1 wiring
- `src/main/java/com/hamsterscreate/compat/mixin` — mixins into Hamsters and vanilla block entities

## 1.21.1

Keep new gameplay rules in `src/logic`. The port will mainly replace `mods.toml` with `neoforge.mods.toml`, `net.minecraftforge` with `net.neoforged`, Kotlin for Forge 4.x with 5.x, and Hamsters `1.21.1-Neo`.
