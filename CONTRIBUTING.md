# StudyLock - Contributing Guidelines

Thank you for your interest in contributing to StudyLock! This document outlines the coding standards, conventions, and workflows we follow.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the project
- Show empathy toward other contributors

## Development Workflow

### Branching Strategy

We use a simplified Git workflow:

```
main          <- Production-ready code
├── feature/*  <- New features
├── fix/*      <- Bug fixes
└── refactor/* <- Code refactoring
```

### Creating a Feature Branch

```bash
git checkout main
git pull origin main
git checkout -b feature/your-feature-name
```

### Committing Changes

Follow these commit message conventions:

```
feat: add user authentication
fix: resolve timer crash on orientation change
refactor: simplify repository pattern
docs: update API documentation
test: add unit tests for XP calculation
chore: upgrade dependencies
```

### Pull Request Process

1. Ensure your code passes all tests
2. Update documentation if needed
3. Submit a clear PR description:
   - What changes were made and why
   - How to test the changes
   - Any breaking changes

## Coding Standards

### Kotlin Style

We follow the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class UserRepository @Inject constructor(
    private val api: UserApi,
    private val database: AppDatabase
) {
    fun getUser(id: String): Flow<User?> = database.userDao().getById(id)
}

// Bad
class UserRepository(private val api: UserApi) {
    fun getUser(id: String): User? { ... }
}
```

### Naming Conventions

- **Classes**: PascalCase (`class UserRepository`)
- **Functions**: camelCase (`fun getUser()`)
- **Variables**: camelCase (`val userName`)
- **Constants**: UPPER_SNAKE_CASE (`const val MAX_ATTEMPTS = 3`)
- **Private Members**: camelCase with underscore prefix (`private var _cache: Map<_, _>?`)

### File Organization

```
presentation/
├── auth/
│   ├── LoginScreen.kt
│   ├── SignUpScreen.kt
│   └── RoleSelectionScreen.kt
├── home/
│   └── HomeScreen.kt
└── navigation/
    └── StudyLockNavigation.kt
```

### Compose Best Practices

#### State Management

```kotlin
// Good - Hoist state
@Composable
fun HomeScreen(
    onStartStudySession: () -> Unit,
    onNavigateToQuizzes: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    // ...
}

// Bad - Keep state in composable
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    // ...
}
```

#### Component Structure

```kotlin
@Composable
fun UserCard(
    user: User,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick(user.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            UserInfo(user)
        }
    }
}
```

#### Preview Support

```kotlin
@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    StudyLockTheme {
        UserCard(
            user = User.sample,
            onClick = {}
        )
    }
}
```

### Dependency Injection (Hilt)

#### Module Structure

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "study_lock.db"
        ).build()
    }
}
```

#### Constructor Injection

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStudySessionsUseCase: GetStudySessionsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel()
```

### Coroutines & Flow

#### Flow Usage

```kotlin
// Good - Expose Flow from ViewModel
class UserRepository @Inject constructor(
    private val api: UserApi
) {
    fun getUser(id: String): Flow<User?> = flow {
        emit(loading())
        try {
            emit(success(api.getUser(id)))
        } catch (e: Exception) {
            emit(error(e))
        }
    }
}

// Bad - Block on main thread
suspend fun getUser(id: String): User? {
    return runBlocking { api.getUser(id) }
}
```

#### ViewModel Coroutines

```kotlin
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val startTimerUseCase: StartTimerUseCase
) : ViewModel() {

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    fun startTimer(duration: Long) {
        viewModelScope.launch {
            startTimerUseCase(duration).collect { state ->
                _timerState.value = state
            }
        }
    }
}
```

### Error Handling

```kotlin
// Good - Use Result or sealed class
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

// Bad - Use nullable
fun getUser(id: String): User? { ... }
```

### Testing

#### Unit Tests

```kotlin
class UserRepositoryTest {
    private lateinit var repository: UserRepository
    private val mockApi: UserApi = mockk()
    private val mockDb: AppDatabase = mockk()

    @Before
    fun setup() {
        repository = UserRepository(mockApi, mockDb)
    }

    @Test
    fun `getUser returns user when found`() = runTest {
        // Arrange
        val expectedUser = User(id = "1", name = "John")
        coEvery { mockApi.getUser("1") } returns expectedUser

        // Act
        val result = repository.getUser("1").first()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
    }
}
```

#### UI Tests

```kotlin
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `startStudySession button navigates correctly`() {
        var navigated = false
        composeTestRule.setContent {
            HomeScreen(
                onStartStudySession = { navigated = true }
            )
        }

        composeTestRule
            .onNodeWithText("Start Study Session")
            .performClick()

        assertTrue(navigated)
    }
}
```

## Documentation

### Code Comments

```kotlin
/**
 * Starts a new study session with the given configuration.
 *
 * @param config The session configuration including subject, duration, etc.
 * @return Flow emitting state updates for the session
 * @throws IllegalArgumentException if config is invalid
 */
fun startSession(config: SessionConfig): Flow<SessionState>
```

### KDoc

Always document public APIs:

```kotlin
/**
 * Represents a user in the StudyLock system.
 *
 * @property id Unique user identifier
 * @property email User's email address
 * @property role User's role (Student or Teacher)
 * @property xp Current experience points
 * @property level Current level based on XP
 */
data class User(
    val id: String,
    val email: String,
    val role: UserRole,
    val xp: Int = 0,
    val level: Int = 1
)
```

## Project Structure Guidelines

### Domain Layer

- **Models**: Pure data classes with no dependencies
- **Use Cases**: Single-responsibility business logic
- **Interfaces**: Define contracts for repositories

### Presentation Layer

- **Screens**: Compose UI components
- **ViewModels**: Manage UI state and user interactions
- **Navigation**: Define routes and navigation logic

### Data Layer

- **Repositories**: Implement data source interfaces
- **Data Sources**: API clients, database access
- **Mappers**: Convert between domain and DTO objects

## Performance Best Practices

### Compose

```kotlin
// Good - Use remember to avoid recomposition
@Composable
fun ExpensiveList(items: List<Item>) {
    val sortedItems = remember(items) { items.sortedBy { it.name } }
    LazyColumn {
        items(sortedItems) { item ->
            ItemRow(item)
        }
    }
}

// Bad - Sort on every composition
@Composable
fun ExpensiveList(items: List<Item>) {
    LazyColumn {
        items(items.sortedBy { it.name }) { item ->
            ItemRow(item)
        }
    }
}
```

### Coroutines

```kotlin
// Good - Use appropriate dispatcher
class MyViewModel : ViewModel() {
    fun doWork() {
        viewModelScope.launch(Dispatchers.IO) {
            // IO work
            withContext(Dispatchers.Main) {
                // UI updates
            }
        }
    }
}
```

## Security Considerations

1. **Never commit secrets** (API keys, passwords)
2. **Validate all user input** on both client and server
3. **Use HTTPS** for all network calls
4. **Sanitize database queries** to prevent SQL injection
5. **Implement rate limiting** for API endpoints
6. **Obfuscate code** in release builds (ProGuard)

## Accessibility

```kotlin
// Good - Add content descriptions
Icon(
    Icons.Default.Menu,
    contentDescription = "Open menu"
)

Button(
    onClick = { /* ... */ }
) {
    Text("Submit")
}
```

## Git Hooks

We recommend using these Git hooks:

### Pre-commit
```bash
#!/bin/bash
# Run tests
./gradlew test
# Format code
./gradlew ktlintFormat
```

### Pre-push
```bash
#!/bin/bash
# Run full test suite
./gradlew test connectedAndroidTest
```

## Review Process

1. **Self-Review**: Review your own changes first
2. **Small PRs**: Keep PRs focused and manageable
3. **Tests**: Ensure tests pass
4. **Documentation**: Update docs as needed
5. **CI Checks**: Wait for CI to pass

## Questions?

- Check the [README.md](README.md) for general information
- See [QUICKSTART.md](QUICKSTART.md) for setup instructions
- Review [IMPLEMENTATION_STATUS.md](IMPLEMENTATION_STATUS.md) for current progress
- Refer to [mayur app.md](mayur%20app.md) for detailed requirements

Thank you for contributing to StudyLock! 🎉
