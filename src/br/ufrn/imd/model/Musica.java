package br.ufrn.imd.model;

import java.io.File;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Musica {
	private SimpleStringProperty caminhoAbsoluto;
	private SimpleBooleanProperty deDiretorio;

	public SimpleStringProperty getCaminhoAbsoluto() {
		return caminhoAbsoluto;
	}

	public void setCaminhoAbsoluto(String caminhoAbsoluto) {
		this.caminhoAbsoluto = new SimpleStringProperty(caminhoAbsoluto);
	}
	
	public String getNome() {
		String caminhoValue = caminhoAbsoluto.getValue();
		return new File(caminhoValue).getName();
	}

	public SimpleBooleanProperty getDeDiretorio() {
		return deDiretorio;
	}

	public void setDeDiretorio(Boolean deDiretorio) {
		this.deDiretorio = new SimpleBooleanProperty(deDiretorio);
	}
	
	
}
