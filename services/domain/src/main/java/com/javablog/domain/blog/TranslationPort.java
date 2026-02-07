package com.javablog.domain.blog;

public interface TranslationPort {

    TranslationJobId translate(Post post, Language targetLanguage);

    TranslationJobStatus translationJobStatus(TranslationJobId jobId);
}
