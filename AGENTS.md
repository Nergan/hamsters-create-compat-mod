# Create: Hamsters Compat — инструкции для AI-агентов

Совместимость Create и Hamsters на Minecraft 1.20.1. Один jar на Forge 47 и NeoForge 20.1, JDK 17. Fabric снят с задач, не добавляй его. На 1.21.1 не переноси, пока об этом прямо не попросили. Версии бери из `gradle.properties` этого репозитория, таблицу 1.21.1 сюда не подставляй. Настройки здесь — `ForgeConfigSpec`.

## Опыт

Если по ходу задачи стало ясно то, что в следующий раз сэкономит неверный ход, допиши это в этот `AGENTS.md`. Ход работы и разовую ошибку не копируй. Неверную старую строку поправь, а не оставляй вторую рядом.


## Скиллы

Перед правкой загрузчика прочитай подходящий скилл из https://github.com/SirEdvin/minecraft-modding-skills (`fabric-modding`, `neoforge-modding`, `neoforge-legacy-modding`, `minecraft-26-1-migration`, `modrinth-api`). Версии бери из `gradle.properties` этого репозитория: если скилл с ними разошёлся, верь файлу.

Перед багфиксом открой https://github.com/DietrichGebert/ponytail/blob/main/skills/ponytail/SKILL.md. Раздел «Готово» этот скилл не отменяет.

Для этого мода читай `neoforge-legacy-modding`. `minecraft-26-1-migration` и таблица 1.21.1 не применяются.

## Правила

- Прежде чем писать своё или декомпилировать, поищи готовое решение, в первую очередь на GitHub.
- Код — Kotlin. Новый Java-файл не добавляй. Уже лежащий Java переписывай на Kotlin, когда трогаешь этот мод, не меняя поведение.
- Имя jar: `<mod_id>-<loader>-<minecraft_version>-<mod_version>.jar`. В Prism и в чужую папку модов его не клади. Игрок качает GitHub Release, ту же сборку.
- Issues: чинить только дефект. На каждый issue ответить и закрыть, включая те, где правки нет.
- Коммит и пуш — только когда владелец прямо просит. Для пуша человек запускает `add-commit-push.cmd`. Теги `minecraft-*` двигает workflow релиза.
- Версии в `gradle.properties` не поднимай, пока задача этого не требует. Локальный путь к JDK туда не записывай.

Перед записью на CDN прочитай https://cdn.mikchan.net/ai/SKILL.md. Сначала `GET https://cdn.mikchan.net/version`. Токен не печатать и не выдумывать. Уже опубликованный номер версии не перезаписывается.

## Готово

Из каталога мода. Сначала `.\gradlew.bat -b logic.gradle test --stacktrace`, затем `.\gradlew.bat build --stacktrace`. Потом клиент до главного меню и локальный сервер до готовности. Упавший запуск так и напиши. Сборка jar без запуска игры — не конец работы. В Prism jar не клади.



Две страницы релиза указывают на один и тот же jar: `create: hamsters compat for minecraft 1.20.1 forge` и `create: hamsters compat for minecraft 1.20.1 neoforge`.

## Локальные инструменты

Kidou и Mikchan CDN — локальные инструменты этой мастерской. Искать сведения о них в интернете бесполезно: публичной документации нет. Для Kidou источник — спецификация в `D:\DATA\soft\code\projects\minecraft\modpacks\Kidou-1.1.4-release.jar` и `D:\DATA\soft\code\projects\minecraft\modpacks\AGENTS.md`. Для Mikchan CDN — https://cdn.mikchan.net/ai/SKILL.md и `GET https://cdn.mikchan.net/version`.

## Проверка мода и сборки

Работа над модом или сборкой заканчивается только после тестов, проверки запуском локального сервера и локального клиента. Сборка jar без этого не считается готовой.

