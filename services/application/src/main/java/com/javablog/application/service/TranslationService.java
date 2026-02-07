package com.javablog.application.service;

import com.javablog.domain.blog.BlogRepository;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostNotFoundException;
import com.javablog.domain.blog.TranslationJobId;
import com.javablog.domain.blog.TranslationPort;
import com.javablog.domain.blog.TranslationRepository;
import com.javablog.domain.blog.TranslationRequest;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private final BlogRepository blogRepository;
    private final TranslationPort translationPort;
    private final TranslationRepository translationRepository;

    public TranslationService(BlogRepository blogRepository,
                               TranslationPort translationPort,
                               TranslationRepository translationRepository) {
        this.blogRepository = blogRepository;
        this.translationPort = translationPort;
        this.translationRepository = translationRepository;
    }

    public void requestTranslation(TranslationRequest request) {
        Post post = blogRepository.findPostById(request.postId())
                .orElseThrow(() -> new PostNotFoundException(request.postId()));

        for (Language language : request.languages().values()) {
            TranslationJobId jobId = translationPort.translate(post, language);
            translationRepository.saveTranslationJob(jobId, post.id(), language);
        }
    }
}
