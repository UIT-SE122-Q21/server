package edu.uit.se122.server.social.internal.repository;

import edu.uit.se122.server.social.internal.entity.ChatDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatDetailRepository extends MongoRepository<ChatDetail, Integer> {
    Slice<ChatDetail> findByTogetherIdOrderByCreatedAtAsc(Integer togetherId, Pageable pageable);
}
