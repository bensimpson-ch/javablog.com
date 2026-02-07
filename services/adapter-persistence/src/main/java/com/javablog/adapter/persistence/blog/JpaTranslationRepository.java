package com.javablog.adapter.persistence.blog;

import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.TranslationJobId;
import com.javablog.domain.blog.TranslationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@Transactional
public class JpaTranslationRepository implements TranslationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveTranslationJob(TranslationJobId jobId, PostId postId, Language language) {
        TranslationJobEntity entity = new TranslationJobEntity(
                jobId.value(),
                postId.value(),
                language.code(),
                LocalDateTime.now()
        );
        entityManager.persist(entity);
    }
}
