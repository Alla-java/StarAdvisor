package org.skypro.staradvisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.RecommendationRule;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<RecommendationRule, UUID> {
    Optional<RecommendationRule> findByProductId(UUID productId);

    void deleteByProductId(UUID productId);
}
