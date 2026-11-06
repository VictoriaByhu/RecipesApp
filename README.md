# Recipes — KMP (Android, Desktop, Web)

Лабораторні роботи №4–5 з курсу «Крос-платформне програмування». Варіант 2: клієнт для відкритого API
[DummyJSON](https://dummyjson.com/docs/recipes), галузь — **рецепти**.

## Що зроблено в ЛР4

- Kotlin Multiplatform проєкт з таргетами Android, Desktop (JVM) і Web (Wasm/JS).
- Дизайн для [Stitch](https://stitch.withgoogle.com/) описано промптом нижче (палітра «Paprika & Herbs», шрифт Montserrat).
- Кастомна тема Material 3 (`shared/.../theme`): кольори «Paprika & Herbs» (світла й темна схеми),
  шрифт Montserrat (`composeResources/font`) і заокруглені форми.
- [Kermit](https://github.com/touchlab/Kermit) для логування (зміна слайдів, завершення онбордингу, зміна теми).
- [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings) для налаштувань користувача:
  чи пройдено онбординг і вибрана тема (системна, світла, темна).
- Онбординг на `HorizontalPager` із 4 слайдами, анімацією масштабу та прозорості, індикатором сторінок
  і кнопками «Назад», «Далі», «Пропустити», «Почати готувати».
- Локалізація українською (`values-uk`) та англійською (`values`). Мову можна змінити в налаштуваннях застосунку
  (Системна / Українська / English), вибір зберігається через multiplatform-settings.

## Що зроблено в ЛР5

- **Navigation 3** (`navigation3-ui`, `lifecycle-viewmodel-navigation3`): маршрути `Route.Onboarding`, `Route.Auth`,
  `Route.Home` (`navigation/Route.kt`), back stack через `rememberNavBackStack`, екрани в `NavDisplay` (`navigation/AppNavigation.kt`).
- **Koin** для впровадження залежностей (`di/Koin.kt`): модулі `dataModule`, `domainModule`, `presentationModule`.
  Ініціалізація в `RecipesApplication` (Android) та у функціях `main` для Desktop і Web.
- **multiplatform-settings** працює через репозиторії `OnboardingRepositoryImpl` і `PreferencesRepositoryImpl`.
- **MVI**: базовий клас `MviViewModel<State, Intent, Effect>`. Для онбордингу, авторизації та головного екрана є
  `*Contract.kt` (стан, наміри, ефекти) і `*ViewModel.kt`.
- **Use cases**: `CompleteOnboardingUseCase`, `IsOnboardingCompletedUseCase`, `ResetOnboardingUseCase`,
  `ValidateUsernameUseCase`, `ValidatePasswordUseCase`, а також для теми та мови.
- `MainViewModel` перевіряє, чи пройдено онбординг і чи користувач уже увійшов, і обирає стартовий екран:
  онбординг, авторизація або головний. Сесія зберігається через `SessionRepository` (multiplatform-settings)
  до натискання «Вийти»; повторний перегляд онбордингу після входу повертає на головний екран.
- Екран авторизації: валідація імені користувача (3–30 символів, латинські літери, цифри, `.`, `_`, `-`) і пароля
  (від 8 символів, без пробілів). Клавіша дії на клавіатурі в полі імені переводить фокус на пароль, а в полі пароля
  виконує вхід, так само як кнопка «Увійти».
- Оновлений дизайн за макетами у Figma: кремова та темна теми з теракотовим і персиковим акцентами, фони з кухонним
  начинням, маскот-котик на слайдах онбордингу, прогрес-бар на всю ширину зверху, картки з контентом унизу,
  перемикачі-сегменти для теми та мови.

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