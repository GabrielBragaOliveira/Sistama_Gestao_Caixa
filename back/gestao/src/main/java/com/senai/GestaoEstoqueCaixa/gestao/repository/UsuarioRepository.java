/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.repository;

import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.enums.UsuarioEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aluno
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByAtivo(Boolean ativo);

    List<Usuario> findByNome(String nome);

    List<Usuario> findByPerfil(UsuarioEnum perfil);

    List<Usuario> findByNomeIgnoreCaseContainingAndEmailIgnoreCaseContainingAndAtivoAndPerfil(String nome, String email, Boolean ativo, UsuarioEnum perfil);

    List<Usuario> findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingOrderByNomeAsc(String nome, String email);

    List<Usuario> findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingAndAtivoOrderByNomeAsc(
            String nome, String email, Boolean ativo);

    List<Usuario> findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingAndPerfilOrderByNomeAsc(
            String nome, String email, UsuarioEnum perfil);

    List<Usuario> findByAtivoAndPerfilOrderByNomeAsc(Boolean ativo, UsuarioEnum perfil);

    List<Usuario> findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingAndAtivoAndPerfilOrderByNomeAsc(String nome, String email, Boolean ativo, UsuarioEnum perfil);

    //public List<Usuario> findAll(Specification<Usuario> spec, Sort ascending);
    
    @Query("""
    SELECT u FROM Usuario u
    WHERE 
        (:ativo IS NULL OR u.ativo = :ativo)
        AND (:perfil IS NULL OR u.perfil = :perfil)
        AND (
            :filtro IS NULL 
            OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :filtro, '%')) 
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :filtro, '%'))
        )
    ORDER BY u.nome ASC
""")
    List<Usuario> filtrarUsuarios(@Param("filtro") String filtro,
            @Param("ativo") Boolean ativo,
            @Param("perfil") UsuarioEnum perfil);
}
