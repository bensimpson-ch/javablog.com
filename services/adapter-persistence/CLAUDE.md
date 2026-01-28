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

```java
@Entity
@Table(name = "blog_posts")
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_post_id")
    private Long blogPostId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
```

## Flyway Migrations

- Location: `src/main/resources/db/migration/`
- Naming: `V{version}__{description}.sql` (e.g., `V1__create_users_table.sql`)
- Always use snake_case in SQL DDL matching the conventions above
