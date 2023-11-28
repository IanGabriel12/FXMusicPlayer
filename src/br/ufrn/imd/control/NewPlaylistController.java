package br.ufrn.imd.control;


import br.ufrn.imd.dao.PlaylistDAO;
import br.ufrn.imd.dao.UserDAO;
import br.ufrn.imd.model.Song;
import br.ufrn.imd.model.UserVIP;
import br.ufrn.imd.model.Playlist;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewPlaylistController {
	
	@FXML
	private TextField nameField;
	@FXML
	private ListView<Song> availableSongs;
	@FXML
	private ListView<Song> selectedSongs;
	
	private ObservableList<Song> availableItems;
	private ObservableList<Song> selectedItems;
	
	private UserDAO userDao = UserDAO.getInstance();
	private PlaylistDAO playlistDao = PlaylistDAO.getInstance();
	
	@FXML
	public void initialize() {
		availableItems = FXCollections.observableArrayList(
				userDao.getLoggedUser().getDefaultPlaylist().getSongs()
		);
		selectedItems = FXCollections.observableArrayList();
		
		availableSongs.setItems(availableItems);
		availableSongs.setCellFactory(view -> new SongNameCell());
		selectedSongs.setItems(selectedItems);
		selectedSongs.setCellFactory(view -> new SongNameCell());
	}
	
	public void selectMusic() {
		Song selected = availableSongs.getSelectionModel().selectedItemProperty().getValue();
		if(selected == null) return;
		selectedItems.add(selected);
		availableItems.remove(selected);
	}
	
	public void removeMusic() {
		Song selected = selectedSongs.getSelectionModel().selectedItemProperty().getValue();
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
			p.setName(new SimpleStringProperty(name));
			p.setSongs(selectedItems);
			UserVIP loggedUser = ((UserVIP) userDao.getLoggedUser());
			loggedUser.addPlaylist(p);
			playlistDao.savePlaylistsFromUser(loggedUser);
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText("A playlist foi criada.");
			a.show();
			closeWindow(event);
		}
	}
}
