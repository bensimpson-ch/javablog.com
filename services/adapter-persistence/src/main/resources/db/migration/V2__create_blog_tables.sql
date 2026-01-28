-- Blog tables for posts and comments

CREATE TABLE posts (
    post_id UUID PRIMARY KEY,
    content TEXT
);

CREATE TABLE comments (
    comment_id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts(post_id),
    content TEXT
);

CREATE INDEX idx_comments_post_id ON comments(post_id);
