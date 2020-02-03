/**
 * 
 */
package com.yojanpardo.lazyloading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yojanpardo.lazyloading.model.Participante;

/**
 * @author yojan
 *
 */
public interface ParticipanteRepository extends JpaRepository<Participante, String>{
	public Participante findByIdParticipante(String idParticipante);
	public List<Participante> findAll();
}
