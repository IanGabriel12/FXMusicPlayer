package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import br.ufrn.imd.model.Musica;
import br.ufrn.imd.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MusicaDAO {
	private ArrayList<Musica> musicas;
	private ArrayList<String> diretorios;
	private String caminhoMusicas = "src/br/ufrn/imd/data/musicas.txt";
	private String caminhoDiretorios = "src/br/ufrn/imd/data/diretorios.txt";
	public static MusicaDAO instance;
	
	private MusicaDAO() {
		musicas = new ArrayList<Musica>();
		loadMusicas();
	}
	
	public static MusicaDAO getInstance() {
		if(instance == null) {
			instance = new MusicaDAO();
		}
		
		return instance;
	}
	
	
	private void loadMusicas() {
		try {
			File arquivoMusicas = new File(caminhoMusicas);
			System.out.println(arquivoMusicas.getAbsolutePath());
			FileReader fr = new FileReader(arquivoMusicas);
			try (BufferedReader br = new BufferedReader(fr)) {
				String caminhoMusica;
				do {
					caminhoMusica = br.readLine();
					if(caminhoMusica == null) break;
					
					Musica m = new Musica();
					m.setCaminhoAbsoluto(caminhoMusica);
					m.setDeDiretorio(false);
					musicas.add(m);
				} while(caminhoMusica != null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadDiretorios() {
		// TODO
	}
	
	private void saveIntoMusicFile() {
		try {
			File arquivoMusicas = new File(caminhoMusicas);
			FileWriter fr = new FileWriter(arquivoMusicas);
			try (BufferedWriter br = new BufferedWriter(fr)) {
				for(Musica m : musicas) {
					if(m.getDeDiretorio().getValue() == true) continue;
					br.write(m.getCaminhoAbsoluto().getValue());
					br.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveIntoDirFile() {
		try {
			File arquivoDiretorios = new File(caminhoDiretorios);
			FileWriter fr = new FileWriter(arquivoDiretorios);
			try (BufferedWriter br = new BufferedWriter(fr)) {
				for(String dirPath : diretorios) {
					br.write(dirPath);
					br.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMusica(Musica m) {
		if(findMusicaByAbsolutePath(m.getCaminhoAbsoluto().getValue()) != null) {
			return;
		}
		musicas.add(m);
		saveIntoMusicFile();
	}
	
	public ArrayList<Musica> getMusicas() {
		return musicas;
	}
	
	public void addDiretorio(String dirPath) {
		diretorios.add(dirPath);
		saveIntoDirFile();
	}
	

	public Musica findMusicaByAbsolutePath(String caminhoAbsoluto) {
		for(Musica m : musicas) {
			if(m.getCaminhoAbsoluto().getValue().equals(caminhoAbsoluto)) {
				return m;
			}
		}
		return null;
	}
}
