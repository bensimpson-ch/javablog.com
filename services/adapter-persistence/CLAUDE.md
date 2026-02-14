# Persistence Layer Guidelines

## JPA Standards

Use only JPA specification annotations and JPQL. Never use vendor-specific annotations or query languages (e.g., no Hibernate-specific `@Type`, `@Formula`, or HQL extensions).

### Allowed
- `jakarta.persistence.*` annotations
- JPQL for queries
- Spring Data JPA repository interfaces

### Not Allowed
- Hibernate-specific annotations (`org.hibernate.annotations.*`)
- Native SQL queries (except when absolutely necessary for performance)
- HQL-specific syntax

## Database Naming Conventions

### Tables
- Use **snake_case** for all names
- Table names must be **plural** (e.g., `users`, `blog_posts`, `comments`)

### Primary Keys
- Spell out as `{tablename}_id` (e.g., `users.user_id`, `blog_posts.blog_post_id`)
- Never use just `id`

### Foreign Keys
- Must match the primary key column name they reference
- Example: `comments.user_id` references `users.user_id`

### Columns
- Use **snake_case**
- Avoid ambiguous names (e.g., use `created_at` not `date`, use `user_email` not `email` if context is unclear)

## JPA Entity Example

UUIDs are generated in the domain layer (e.g., `PostId.generate()`), not by the database. Entities use no `@GeneratedValue`.

```java
@Entity
@Table(name = "posts")
@NamedQuery(name = "PostEntity.findAll", query = "SELECT p FROM PostEntity p WHERE p.languageCode = :languageCode")
@NamedQuery(name = "PostEntity.findBySlug", query = "SELECT p FROM PostEntity p WHERE p.slug = :slug")
public class PostEntity {

    @Id
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected PostEntity() {} // JPA requires no-arg constructor
}
```

## Flyway Migrations

- Location: `src/main/resources/db/migration/`
- Naming: `V{version}__{description}.sql` (e.g., `V1__create_users_table.sql`)
- Always use snake_case in SQL DDL matching the conventions above
