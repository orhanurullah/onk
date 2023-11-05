package com.onk.repository;

import com.onk.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByPath(String imagePath);

    boolean existsByPath(String path);
}
