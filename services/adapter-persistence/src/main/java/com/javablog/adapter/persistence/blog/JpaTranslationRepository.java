package com.javablog.adapter.persistence.blog;

import com.javablog.domain.blog.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public TranslatedPosts findTranslations(PostId originalPostId) {
        List<TranslationJobEntity> entities = entityManager
                .createNamedQuery(TranslationJobEntity.FIND_BY_ORIGINAL_POST_ID, TranslationJobEntity.class)
                .setParameter("originalPostId", originalPostId.value())
                .getResultList();

        Set<TranslatedPost> posts = entities.stream()
                .map(e -> new TranslatedPost(
                        new PostId(e.getTranslationJobId()),
                        originalPostId,
                        Language.fromCode(e.getLanguageCode()),
                        null,
                        e.getSlug() != null ? new Slug(e.getSlug()) : null,
                        new CreatedAt(e.getCreatedAt()),
                        null
                ))
                .collect(Collectors.toSet());

        return new TranslatedPosts(posts);
    }
}
