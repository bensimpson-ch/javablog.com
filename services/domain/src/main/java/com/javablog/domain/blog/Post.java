package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Post(
        PostId id,
        Slug slug,
        Title title,
        Content content,
        CreatedAt createdAt
) {

    public Post {
        Guard.againstNull(id, "Post.id");
        Guard.againstNull(slug, "Post.slug");
        Guard.againstNull(title, "Post.title");
        Guard.againstNull(content, "Post.content");
        Guard.againstNull(createdAt, "Post.createdAt");
    }
}
