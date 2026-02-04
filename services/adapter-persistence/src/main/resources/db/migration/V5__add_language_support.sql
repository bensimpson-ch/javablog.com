-- Add language_code to posts table
ALTER TABLE posts ADD COLUMN language_code VARCHAR(2) NOT NULL DEFAULT 'en';

-- Remove default after setting existing rows
ALTER TABLE posts ALTER COLUMN language_code DROP DEFAULT;

-- Create translated_posts table
CREATE TABLE translated_posts (
    translated_post_id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts(post_id) ON DELETE CASCADE,
    language_code VARCHAR(2) NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    UNIQUE (post_id, language_code)
);