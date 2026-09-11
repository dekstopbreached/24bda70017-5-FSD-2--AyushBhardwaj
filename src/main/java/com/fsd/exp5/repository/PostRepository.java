package com.fsd.exp5.repository;

import com.fsd.exp5.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    boolean deleteById(Long id);
    boolean existsById(Long id);
    void deleteAll();
}
