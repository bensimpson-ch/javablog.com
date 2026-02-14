CREATE TABLE articles (
    article_id UUID PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    language_code VARCHAR(2) NOT NULL DEFAULT 'en',
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE translated_articles (
    translated_article_id UUID PRIMARY KEY,
    article_id UUID NOT NULL REFERENCES articles(article_id),
    language_code VARCHAR(2) NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    UNIQUE (article_id, language_code)
);

CREATE TABLE article_translation_jobs (
    article_translation_job_id UUID PRIMARY KEY,
    original_article_id UUID NOT NULL,
    language_code VARCHAR(2) NOT NULL,
    slug VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    UNIQUE (original_article_id, language_code)
);
