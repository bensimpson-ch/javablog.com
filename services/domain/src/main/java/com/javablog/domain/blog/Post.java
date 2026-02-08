package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record Post(
        PostId id,
        Slug slug,
        Title title,
        Summary summary,
        Content content,
        Language language,
        CreatedAt createdAt
) {

    public Post {
        Guard.againstNull(id, "Post.id");
        Guard.againstNull(slug, "Post.slug");
        Guard.againstNull(title, "Post.title");
        Guard.againstNull(summary, "Post.summary");
        Guard.againstNull(content, "Post.content");
        Guard.againstNull(language, "Post.language");
        Guard.againstNull(createdAt, "Post.createdAt");
    }
}
