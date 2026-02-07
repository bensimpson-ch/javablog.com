package com.javablog.domain.blog;

import com.javablog.domain.Guard;

public record TranslationRequest(PostId postId, Author author, Languages languages) {

    public TranslationRequest {
        Guard.againstNull(postId, "TranslationRequest.postId");
        Guard.againstNull(author, "TranslationRequest.author");
        Guard.againstNull(languages, "TranslationRequest.languages");
    }
}
