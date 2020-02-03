/**
 * 
 */
package com.yojanpardo.lazyloading.view.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yojan
 *
 */
public class ProcesadorArchivos {

	public static String[] leerArchivo(MultipartFile archivo) {
		BufferedReader br;
		List<String> datos = new ArrayList<String>();
		String[] datosArray = new String[datos.size()];
		try {
			String linea;
			InputStream is = archivo.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((linea = br.readLine()) != null) {
				datos.add(linea);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return datos.toArray(datosArray);
	}

	public static ArrayList<int[]> itemsDia(String[] datos) {
		ArrayList<int[]> itemsXDia = new ArrayList<int[]>();
		int siguientes = 0;
		int[] items;
		for (int i = 1; i < datos.length; i++) {
			siguientes = Integer.parseInt(datos[i]);
			items = new int[siguientes];
			for (int j = 0; j < siguientes; j++) {
				items[j] = Integer.parseInt(datos[++i]);
			}
			Arrays.sort(items);
			itemsXDia.add(items);
		}
		return itemsXDia;
	}

	public static int[] maximizarViajes(ArrayList<int[]> diasItems, int dias) {
		int[] viajesXDia = new int[dias];
		for (int i = 0; i < diasItems.size(); i++) {
			int contadorReversa = 1;
			int viajes = 1;
			int[] itemsDia = diasItems.get(i);
			int itemsViaje = 0;
			int pesoEstimado = 0;
			for (int j = 0; j < itemsDia.length - contadorReversa; j++) {
				pesoEstimado = itemsDia[j] * (++itemsViaje);
				if (pesoEstimado > 50) {
					viajes++;
				} else if (itemsDia[itemsDia.length - (++contadorReversa)] < 50) {
					itemsViaje++;
					pesoEstimado = (itemsDia[itemsDia.length - contadorReversa]) * (itemsViaje);
					if (pesoEstimado > 50) {
						viajes++;
					}
				}
			}
			viajesXDia[i] = viajes;
		}
		return viajesXDia;
	}

	public static File generarArchivo(int[] datos, String idParticipante) throws Exception {
		File archivo = new File(idParticipante + ".txt");
		if (archivo.exists()) {
			return archivo;
		} else {
			BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
			for (int i = 0; i < datos.length; i++) {
				bw.write("case #" + (i+1) + ": " + datos[i] + "\n");
			}
			bw.close();
			return archivo;
		}
	}
}
