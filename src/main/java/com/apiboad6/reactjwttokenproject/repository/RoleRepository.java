package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.member.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findById (int id);
}
