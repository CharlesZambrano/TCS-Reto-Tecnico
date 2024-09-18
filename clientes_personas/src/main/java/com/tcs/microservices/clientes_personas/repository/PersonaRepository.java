package com.tcs.microservices.clientes_personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.microservices.clientes_personas.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
