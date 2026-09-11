package com.fsd.exp5.repository;

import com.fsd.exp5.model.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final ConcurrentHashMap<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(idSequence.getAndIncrement());
            if (post.getCreatedAt() == null) {
                post.setCreatedAt(LocalDateTime.now());
            }
        }
        store.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return store.remove(id) != null;
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && store.containsKey(id);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
