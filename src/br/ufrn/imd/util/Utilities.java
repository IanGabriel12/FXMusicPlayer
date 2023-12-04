package br.ufrn.imd.util;

import java.io.File;

public class Utilities {
	/**
	 * Verifica se o arquivo é um arquivo de áudio suportado
	 * @param file Arquivo a ser verificado
	 * @return True se é um arquivo de áudio válido. False c.c
	 */
	public static boolean isAudioFile(File file) {
		return file.getName().endsWith(".mp3") || file.getName().endsWith(".wav");
	}
	
	public static String getTimeString(double millis) {
		int seconds = (int) Math.floor(millis / 1000);
		String sec  = String.format("%02d", seconds % 60);
		String minutes = String.format("%02d", seconds / 60);
		return minutes + ":" + sec;
	}
}
