# Recipes — KMP (Android, Desktop, Web)

Лабораторна робота №4 з курсу «Крос-платформне програмування». Варіант 2: клієнт для відкритого API
[DummyJSON](https://dummyjson.com/docs/recipes), галузь — **рецепти**.

## Що зроблено в ЛР4

- Kotlin Multiplatform проєкт з таргетами Android, Desktop (JVM) і Web (Wasm/JS).
- Дизайн згенеровано в [Stitch](https://stitch.withgoogle.com/), з нього взято палітру й шрифт.
- Кастомна тема Material 3 (`shared/.../theme`): кольори «Paprika & Herbs» (світла й темна схеми),
  шрифт Montserrat (`composeResources/font`) і заокруглені форми.
- [Kermit](https://github.com/touchlab/Kermit) для логування (зміна слайдів, завершення онбордингу, зміна теми).
- [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings) для налаштувань користувача:
  чи пройдено онбординг і вибрана тема (системна, світла, темна).
- Онбординг на `HorizontalPager` із 4 слайдами, анімацією масштабу та прозорості, індикатором сторінок
  і кнопками «Назад», «Далі», «Пропустити», «Почати готувати».
- Локалізація українською (`values-uk`) та англійською (`values`). Мову можна змінити в налаштуваннях застосунку
  (Системна / Українська / English), вибір зберігається через multiplatform-settings.

### Промпт для Stitch

> Mobile recipe app onboarding, 4 screens with a horizontal pager: welcome, find what to cook,
> save favorites, cook step by step. Warm cozy kitchen style, paprika orange primary (#9C4A1A),
> olive green secondary, cream background (#FFF8F5), large circular illustration with a line icon,
> bold Montserrat headline, short centered description, pill-shaped page indicator, Back / Next buttons.
> Plus a simple home screen with greeting and settings card (theme: system/light/dark).

---

This is a Kotlin Multiplatform project targeting Android, Web, Desktop (JVM).

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- Web app:
  - Wasm target (faster, modern browsers): `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
  - JS target (slower, supports older browsers): `./gradlew :webApp:jsBrowserDevelopmentRun`

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- Web tests:
  - Wasm target: `./gradlew :shared:wasmJsTest`
  - JS target: `./gradlew :shared:jsTest`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).