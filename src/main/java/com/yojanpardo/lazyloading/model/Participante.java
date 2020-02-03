/**
 * 
 */
package com.yojanpardo.lazyloading.model;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * Entidad para manejar el mapeo a la base de datos	
 * @author yojan
 *
 */
@Data
@Entity
@Table(name = "participantes")
public class Participante {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String idParticipante;
	private String[] datos;
	private int dias;
	private ArrayList<int[]> itemsDia;
	private String cedulaParticipante;
	private int[] viajesXDia;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaRegistro;
	
	public Participante() {
		super();
	}
}
