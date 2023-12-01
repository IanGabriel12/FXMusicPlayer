package br.ufrn.imd.util;

import java.io.File;

public class Utilities {
	/**
	 * Verifica se o arquivo é um arquivo de áudio suportado
	 * @param file Arquivo a ser verificado
	 * @return True se é um arquivo de áudio válido. False c.c
	 */
	public static boolean isAudioFile(File file) {
		return file.getName().endsWith(".mp3");
	}
}
