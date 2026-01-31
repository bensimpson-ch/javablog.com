-- Add summary column to posts table
ALTER TABLE posts ADD COLUMN summary VARCHAR(500);

-- Update existing posts with their summaries
UPDATE posts SET summary = 'Internal debate about the role of Jakarta Validation Annotations in the domain. Using the "everything is an object" philosophy where Title, Slug, Content, and Author each have their own record.'
WHERE slug = 'domain-object-validation';

UPDATE posts SET summary = 'In Spring Boot 4, dependencies like Flyway are configured differently. Auto-configuration works without additional properties. If your Coding Assistant didn''t configure it correctly, you will learn why here.'
WHERE slug = 'flyway-spring-boot-4-cutoff';

UPDATE posts SET summary = 'Converting a monolithic Spring Boot application to multi-module hexagonal architecture with domain, application, adapter-rest, adapter-persistence, and bootstrap modules. Adding Flyway migrations and PostgreSQL for production with H2 for tests.'
WHERE slug = 'hexagonal-architecture-springboot';

UPDATE posts SET summary = 'Connecting the Angular frontend to the Java API backend. Deployed via Github Action to create Dockerized container and then use SSH to copy container and deploy to api.javablog.com'
WHERE slug = 'springboot-docker-with-claude';

UPDATE posts SET summary = 'First steps setting up SEO for javablog.com with Claude AI assistance.'
WHERE slug = 'seo-with-claude';

UPDATE posts SET summary = 'How I built this blog using Angular, Azure Static Web Apps, and Anthropic Claude.'
WHERE slug = 'building-javablog';

-- Make summary required
ALTER TABLE posts ALTER COLUMN summary SET NOT NULL;
