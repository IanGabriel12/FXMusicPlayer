package br.ufrn.imd.control;

import java.io.IOException;
import java.util.ArrayList;

import br.ufrn.imd.dao.MusicaDAO;
import br.ufrn.imd.dao.UsuarioDAO;
import br.ufrn.imd.model.Musica;
import br.ufrn.imd.model.Playlist;
import br.ufrn.imd.model.UsuarioVIP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NovaPlaylistController {
	
	@FXML
	private TextField nameField;
	@FXML
	private ListView<Musica> availableSongs;
	@FXML
	private ListView<Musica> selectedSongs;
	
	private ObservableList<Musica> availableItems;
	private ObservableList<Musica> selectedItems;
	
	private MusicaDAO musicDao = MusicaDAO.getInstance();
	private UsuarioDAO userDao = UsuarioDAO.getInstance();
	
	@FXML
	public void initialize() {
		availableItems = FXCollections.observableArrayList(musicDao.getMusicas());
		selectedItems = FXCollections.observableArrayList();
		
		availableSongs.setItems(availableItems);
		availableSongs.setCellFactory(view -> new MusicaNomeCell());
		selectedSongs.setItems(selectedItems);
		selectedSongs.setCellFactory(view -> new MusicaNomeCell());
	}
	
	public void selectMusic() {
		Musica selected = availableSongs.getSelectionModel().selectedItemProperty().getValue();
		if(selected == null) return;
		selectedItems.add(selected);
		availableItems.remove(selected);
	}
	
	public void removeMusic() {
		Musica selected = selectedSongs.getSelectionModel().selectedItemProperty().getValue();
		if(selected == null) return;
		availableItems.add(selected);
		selectedItems.remove(selected);
	}

	
	public void closeWindow(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	public void createPlaylist(ActionEvent event) {
		String name = nameField.getText();
		if(name.isBlank()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("A playlist deve ter um nome");
			a.show();
		} else if (selectedSongs.getItems().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("A playlist deve ter pelo menos uma música");
			a.show();
		} else {
			Playlist p = new Playlist();
			p.setNome(name);
			p.setMusicas(new ArrayList<>(selectedSongs.getItems()));
			((UsuarioVIP) userDao.getUsuarioLogado()).addPlaylist(p);
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText("A playlist foi criada.");
			a.show();
			closeWindow(event);
		}
	}
}
