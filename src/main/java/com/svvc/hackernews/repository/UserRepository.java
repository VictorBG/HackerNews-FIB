package com.svvc.hackernews.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.svvc.hackernews.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByUsername(String username);
}
