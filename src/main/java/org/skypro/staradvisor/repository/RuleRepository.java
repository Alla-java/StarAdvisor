package org.skypro.staradvisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  org.skypro.staradvisor.model.RecommendationRule;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<RecommendationRule, UUID> {
}
