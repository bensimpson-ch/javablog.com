-- Blog tables for posts and comments

CREATE TABLE posts (
    post_id UUID PRIMARY KEY,
    slug VARCHAR(255) UNIQUE,
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comments (
    comment_id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts(post_id),
    author VARCHAR(100),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_posts_slug ON posts(slug);
