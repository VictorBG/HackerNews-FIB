package com.svvc.hackernews.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.svvc.hackernews.model.Likes;


public interface LikesRepository extends JpaRepository<Likes, Long>
{
    @Query(
        value = "SELECT * FROM likes WHERE likes.user_id_id = ?1 AND likes.contribution_id_id = ?2",
        nativeQuery = true)
    Optional<Likes> findIfLikeExist(String usId, Long cId);

    @Query(
        value = "SELECT * FROM likes WHERE likes.user_id_id = ?1",
        nativeQuery = true)
    List<Likes> findAllContributionsVotedById(String name);
}
