package com.javablog.domain;

import com.javablog.domain.blog.PostId;

public record Comment(
        CommentId id,
        PostId postId,
        Author author,
        Content content,
        CreatedAt createdAt
) {

    public Comment {
        Guard.againstNull(id, "Comment.id");
        Guard.againstNull(postId, "Comment.postId");
        Guard.againstNull(author, "Comment.author");
        Guard.againstNull(content, "Comment.content");
        Guard.againstNull(createdAt, "Comment.createdAt");
    }
}
