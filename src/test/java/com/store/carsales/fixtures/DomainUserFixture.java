package com.store.carsales.fixtures;

import java.util.UUID;

import com.store.carsales.domain.DomainUser;

public class DomainUserFixture {
    public static DomainUser build() {
        return DomainUser
                .builder()
                .id(UUID.randomUUID())
                .name("Usuário de teste")
                .username("usuario")
                .password("$2a$10$5FWBdzgPub4.8WsZWGQ3auJZfEGXWmoP8SLBBPR33NEvdCeN72uR2")
                .authorities("ROLE_USER")
                .build();
    }
}
