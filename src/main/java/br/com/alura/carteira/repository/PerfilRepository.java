package br.com.alura.carteira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.carteira.modelo.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
