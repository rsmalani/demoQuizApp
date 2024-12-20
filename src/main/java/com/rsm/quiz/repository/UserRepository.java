package com.rsm.quiz.repository;

import com.rsm.quiz.model.AppUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String string);
}
