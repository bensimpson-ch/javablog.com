# Domain Layer Guidelines

Pure Java domain layer with no framework dependencies.

## Philosophy

- **Everything is an object** - Each concept gets its own record (Title, Slug, Content, Author, etc.)
- **No framework dependencies** - Domain remains pure Java
- **Validation in constructors** - Use Guard class for constraint enforcement

## Guard Pattern

Domain validation uses the `Guard` class with static methods that throw `ConstraintViolationException` on failure.

### Available Guards

```java
Guard.againstNull(Object value, String fieldName)      // value must not be null
Guard.againstEmpty(String value, String fieldName)     // value must not be null or empty
Guard.againstBlank(String value, String fieldName)     // value must not be null, empty, or whitespace
Guard.againstInvalidMaxLength(String value, String fieldName, int maxLength)  // value must not exceed max length
```

### Usage in Records

Use compact constructors to apply guards:

```java
public record Title(String value) {

    public Title {
        Guard.againstBlank(value, "Title.value");
    }
}

public record Slug(String value) {

    public Slug {
        Guard.againstBlank(value, "Slug.value");
        Guard.againstInvalidMaxLength(value, "Slug.value", 255);
    }
}
```

### Field Name Convention

Use `RecordName.fieldName` format for guard messages (e.g., `"Title.value"`, `"Post.id"`).

## Plural Collection Records

Collections are wrapped in plural records (e.g., `Posts`, `Comments`) that guard against null:

```java
public record Posts(Set<Post> values) {

    public Posts {
        Guard.againstNull(values, "Posts.values");
    }
}
```

## Static Factory Methods

Only create static factory methods when they provide meaningful behavior:
- `PostId.generate()` - Creates new UUID
- `CreatedAt.now()` - Creates with current timestamp

Do NOT create simple `of()` factory methods that just delegate to the constructor.

## Unit Testing Conventions

### Test Class Structure

- Test class named `{Record}Test` in same package under `src/test/java`
- Use AssertJ for assertions
- Use JUnit Jupiter parameterized tests

### validateConstraints Test

Every domain record test must have a `validateConstraints` test that verifies invalid inputs throw `ConstraintViolationException`.

#### Single Parameter Records

```java
@ParameterizedTest
@NullAndEmptySource
@MethodSource
void validateConstraints(String value) {
    assertThatThrownBy(() -> new Title(value))
            .isInstanceOf(ConstraintViolationException.class);
}

static Stream<String> validateConstraints() {
    return Stream.of("   ");  // whitespace-only for @NotBlank
}
```

#### Multi-Parameter Records

```java
@ParameterizedTest
@MethodSource
void validateConstraints(PostId id, Slug slug, Title title, Content content, CreatedAt createdAt) {
    assertThatThrownBy(() -> new Post(id, slug, title, content, createdAt))
            .isInstanceOf(ConstraintViolationException.class);
}

static Stream<Arguments> validateConstraints() {
    return Stream.of(
            Arguments.of(null, slug(), title(), content(), createdAt()),
            Arguments.of(postId(), null, title(), content(), createdAt()),
            // ... one argument for each null field
    );
}
```

#### Null-Only Records (UUIDs, Collections)

```java
@Test
void validateConstraints() {
    assertThatThrownBy(() -> new PostId(null))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("PostId.value must not be null");
}
```

### Test Annotations

- Use `@NullAndEmptySource` for String fields that must not be null or empty
- Use `@NullSource` for fields that only check null (UUID, LocalDateTime)
- Use `@MethodSource` named `validateConstraints` for additional invalid values
- Only test invalid scenarios - valid construction is implicitly tested via Fixture

### Fixture Class

Use the `Fixture` class for constructing valid domain objects in tests:

```java
import static com.javablog.domain.Fixture.*;

static Stream<Arguments> validateConstraints() {
    return Stream.of(
            Arguments.of(null, slug(), title(), content(), createdAt()),
            // ...
    );
}
```

## Dependencies

- JUnit Jupiter (test)
- AssertJ (test)
- No production dependencies beyond Java stdlib
