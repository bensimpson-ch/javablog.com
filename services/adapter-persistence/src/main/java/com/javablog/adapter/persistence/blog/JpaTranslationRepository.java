package com.javablog.adapter.persistence.blog;

import com.javablog.domain.*;
import com.javablog.domain.blog.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
    public Optional<TranslationJob> findTranslationJob(TranslationJobId jobId) {
        TranslationJobEntity entity = entityManager.find(TranslationJobEntity.class, jobId.value());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(new TranslationJob(
                new TranslationJobId(entity.getTranslationJobId()),
                new PostId(entity.getOriginalPostId()),
                Language.fromCode(entity.getLanguageCode())
        ));
    }

    @Override
    public void saveTranslatedPost(PostId originalPostId, Language language, Title title, Summary summary, Slug slug, Content content) {
        PostEntity postEntity = entityManager.getReference(PostEntity.class, originalPostId.value());
        TranslatedPostEntity entity = new TranslatedPostEntity(
                UUID.randomUUID(),
                postEntity,
                language.code(),
                title.value(),
                summary.value(),
                content.value()
        );
        entityManager.persist(entity);
    }

    @Override
    public void deleteTranslationJob(TranslationJobId jobId) {
        TranslationJobEntity entity = entityManager.find(TranslationJobEntity.class, jobId.value());
        if (entity != null) {
            entityManager.remove(entity);
        }
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
