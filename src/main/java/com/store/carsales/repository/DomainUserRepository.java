package com.store.carsales.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.carsales.domain.DomainUser;

@Repository
public interface DomainUserRepository extends JpaRepository<DomainUser, UUID> {

    Optional<DomainUser> findByUsername(String username);
}
