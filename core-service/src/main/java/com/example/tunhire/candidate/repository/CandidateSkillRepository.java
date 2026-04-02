package com.example.tunhire.candidate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateSkillRepository extends JpaRepository<com.example.tunhire.candidate.entity.CandidateSkill, Long> {
    List<com.example.tunhire.candidate.entity.CandidateSkill> findByProfileId(Long profileId);
    void deleteByIdAndProfileId(Long id, Long profileId);
}
