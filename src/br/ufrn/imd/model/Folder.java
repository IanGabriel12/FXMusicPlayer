package br.ufrn.imd.model;

import javafx.beans.property.SimpleStringProperty;

public class Folder {
	private SimpleStringProperty path;
	

	public SimpleStringProperty getPath() {
		return path;
	}

	public void setPath(SimpleStringProperty path) {
		this.path = path;
	}
}
