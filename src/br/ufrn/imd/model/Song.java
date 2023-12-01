package br.ufrn.imd.model;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;

public class Song {
	private SimpleStringProperty absolutePath;

	public SimpleStringProperty getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(SimpleStringProperty absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public String getName() {
		String pathValue = absolutePath.getValue();
		return new File(pathValue).getName();
	}
}
