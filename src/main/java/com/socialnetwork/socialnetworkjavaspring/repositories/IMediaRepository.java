package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Hashtag;
import com.socialnetwork.socialnetworkjavaspring.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMediaRepository extends JpaRepository<Media, Long> {
}
