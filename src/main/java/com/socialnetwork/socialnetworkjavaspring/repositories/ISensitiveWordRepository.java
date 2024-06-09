package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.SensitiveWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISensitiveWordRepository extends JpaRepository<SensitiveWord, Long> {
}
