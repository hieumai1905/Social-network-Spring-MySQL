package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostInteractRepository  extends JpaRepository<PostInteract, Long> {
}
