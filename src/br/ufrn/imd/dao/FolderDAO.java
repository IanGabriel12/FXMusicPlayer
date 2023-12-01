package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import br.ufrn.imd.model.Folder;
import br.ufrn.imd.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class FolderDAO {
	private String folderDirectory = "src/br/ufrn/imd/data/folders";
	private static FolderDAO instance;
	
	/**
	 * 
	 * @return Retorna a instância do padrão singleton do FolderDAO
	 */
	public static FolderDAO getInstance() {
		if(instance == null) {
			instance = new FolderDAO();
		}
		
		return instance;
	}
	
	
	/**
	 * Carrega os diretórios adicionados pelo usuário recebido para 
	 * a memória
	 * @param user Intância do usuário a ter as pastas carregadas
	 */
	public void loadFoldersFromUser(User user) {
		String filePath = folderDirectory + String.format("/%s_folders.txt", user.getName().getValue());
		File file = new File(filePath);
		user.setFolders(FXCollections.observableArrayList());
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileReader fr = new FileReader(file);
			try (BufferedReader br = new BufferedReader(fr)) {
				String path;
				do {
					path = br.readLine();
					if(path == null) break;
					Folder f = new Folder();
					f.setPath(new SimpleStringProperty(path));
					user.addFolder(f);
				} while(path != null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Salva os diretórios do usuário em um arquivo
	 * @param user Instância do usuário contendo os diretórios
	 */
	public void saveFoldersFromUser(User user) {
		String filePath = folderDirectory + String.format("/%s_folders.txt", user.getName().getValue());
		System.out.println(filePath);
		File file = new File(filePath);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				for(Folder f : user.getFolders()) {
					bw.write(f.getPath().getValue());
					bw.newLine();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
