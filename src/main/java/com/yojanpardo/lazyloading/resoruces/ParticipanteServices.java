/**
 * 
 */
package com.yojanpardo.lazyloading.resoruces;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yojanpardo.lazyloading.model.Participante;
import com.yojanpardo.lazyloading.repository.ParticipanteRepository;

/**
 * @author yojan
 *
 */
@Service
@Transactional(readOnly = true)
public class ParticipanteServices {
	private final ParticipanteRepository participanteRepository;
	
	public ParticipanteServices(ParticipanteRepository participanteRepository) {
		this.participanteRepository = participanteRepository;
	}
	
	@Transactional
	public Participante create(Participante participante) {
		return this.participanteRepository.save(participante);
	}
	
	public List<Participante> listarTodos(){
		return this.participanteRepository.findAll();
	}
	
	public Participante buscarParticipante(String idParticipante){
		System.out.println(idParticipante);
		return this.participanteRepository.findByIdParticipante(idParticipante);
	}
}
