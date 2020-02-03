/**
 * 
 */
package com.yojanpardo.lazyloading.view.resources;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yojanpardo.lazyloading.model.Participante;
import com.yojanpardo.lazyloading.resoruces.ParticipanteServices;
import com.yojanpardo.lazyloading.view.controller.ProcesadorArchivos;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author yojan
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping("api/participante")
@Api(tags = "participante")
public class ParticipanteResources {
	private final ParticipanteServices participanteServices;
	private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public ParticipanteResources(ParticipanteServices participanteServices) {
		this.participanteServices = participanteServices;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(
			value = "Registrar Participante", 
			notes = "Método para hacer el registro de un participante"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Participante registrado correctamente"),
			@ApiResponse(code = 400, message = "Error al intentar crear el registro")
	})
	public ResponseEntity<Participante> createRegistroParticipante(@RequestParam String cedula, @RequestParam MultipartFile archivo){
		Participante participante = new Participante();
		participante.setCedulaParticipante(cedula);
		participante.setFechaRegistro(timestamp);
		participante.setDatos(ProcesadorArchivos.leerArchivo(archivo));
		participante.setDias(Integer.parseInt(participante.getDatos()[0]));
		if(participante.getDias()<1 || participante.getDias() >500) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		participante.setItemsDia(ProcesadorArchivos.itemsDia(participante.getDatos()));
		participante.setViajesXDia(ProcesadorArchivos.maximizarViajes(participante.getItemsDia(), participante.getDias()));
		
		return new ResponseEntity<>(this.participanteServices.create(participante), HttpStatus.CREATED);
	}

	@GetMapping
	@ApiOperation(value = "Listar todos los participantes", notes = "Lista de todos los registros realizados en el programa")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Participantes encontrados satisfactoriamente"),
			@ApiResponse(code = 404, message = "Participantes no encontrados") })
	public ResponseEntity<List<Participante>> listarTodos() {
		return ResponseEntity.ok(this.participanteServices.listarTodos());
	}
	
	@GetMapping("/descargar/{idParticipante}")
	@ApiOperation(value = "Descargar un archivo", notes = "Descarga el archivo de salida de un participante")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Archivo descargado exitosamente"),
			@ApiResponse(code = 404, message = "Participante no encontrado")
	})
	public ResponseEntity<InputStreamResource> descargarArchivo(@PathVariable String idParticipante) throws Exception {
		Participante participante = this.participanteServices.buscarParticipante(idParticipante);
		File archivo = ProcesadorArchivos.generarArchivo(participante.getViajesXDia(), participante.getIdParticipante());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(archivo));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + archivo.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(archivo.length()) 
                .body(resource);
	}
}
