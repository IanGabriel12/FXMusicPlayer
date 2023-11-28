package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import br.ufrn.imd.model.Song;
import br.ufrn.imd.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class SongDAO {  
	private String songsFolder = "src/br/ufrn/imd/data/songs";
	private static SongDAO instance;
	
	public static SongDAO getInstance() {
		if(instance == null) {
			instance = new SongDAO();
		}
		
		return instance;
	}
	
	public void loadSongsFromUser(User user) {
		String filePath = songsFolder + String.format("/%s_songs.txt", user.getName().getValue());
		File file = new File(filePath);
		user.setSongs(FXCollections.observableArrayList());
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
					Song s = new Song();
					s.setAbsolutePath(new SimpleStringProperty(path));
					user.addSong(s);
				} while(path != null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveSongsFromUser(User user) {
		String filePath = songsFolder + String.format("/%s_songs.txt", user.getName().getValue());
		System.out.println(filePath);
		File file = new File(filePath);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				for(Song s : user.getSongs()) {
					bw.write(s.getAbsolutePath().getValue());
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
