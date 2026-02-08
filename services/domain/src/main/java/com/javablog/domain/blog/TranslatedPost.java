package com.javablog.domain.blog;

import com.javablog.domain.Guard;

import java.time.LocalDateTime;

public record TranslatedPost(
        PostId postId,
        PostId originalPostId,
        Language language,
        Title title,
        Slug slug,
        CreatedAt createdDate,
        LocalDateTime completedDate
) {

    public TranslatedPost {
        Guard.againstNull(postId, "TranslatedPost.postId");
        Guard.againstNull(originalPostId, "TranslatedPost.originalPostId");
        Guard.againstNull(language, "TranslatedPost.language");
        Guard.againstNull(createdDate, "TranslatedPost.createdDate");
    }
}
