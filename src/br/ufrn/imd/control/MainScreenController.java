package br.ufrn.imd.control;

import java.io.File;
import java.io.IOException;

import br.ufrn.imd.dao.FolderDAO;
import br.ufrn.imd.dao.PlaylistDAO;
import br.ufrn.imd.dao.SongDAO;
import br.ufrn.imd.dao.UserDAO;
import br.ufrn.imd.model.Song;
import br.ufrn.imd.model.User;
import br.ufrn.imd.model.UserVIP;
import br.ufrn.imd.util.Utilities;
import br.ufrn.imd.model.Folder;
import br.ufrn.imd.model.Playlist;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MainScreenController {
	@FXML
	private Label nameLabel;
	@FXML
	private Label vipLabel;
	@FXML
	private ListView<Song> songList;
	@FXML
	private Label musicTitle;
	@FXML
	private Button toggleButton;
	@FXML
	private ListView<Playlist> playlistList;
	@FXML
	private ListView<Song> playlistSongList;
	@FXML
	private Label playlistTitleLabel;
	@FXML
	private Slider timeSlider;
	@FXML
	private Label timeLabel;
	
	private UserDAO userDao = UserDAO.getInstance();
	private SongDAO songDao = SongDAO.getInstance();
	private FolderDAO folderDao = FolderDAO.getInstance();
	private PlaylistDAO playlistDao = PlaylistDAO.getInstance();
	
	private MediaPlayer currentMediaPlayer;
	private Playlist selectedPlaylist;
	private boolean playingDefault;
	private boolean isSliderChanging;
	
	/**
	 * Retorna o usuário para a tela de login
	 */
	public void logout(ActionEvent event) {
		try {
			userDao.setLoggedUser(null);
			if(currentMediaPlayer != null) currentMediaPlayer.stop();
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/LoginScreen.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("FXMusicPlayer - Login");
			stage.show();
			((Node) event.getSource()).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Faz o usuário escolher uma música para adicionar
	 * utilizando o seletor de arquivos do sistema
	 * @param event
	 */
	public void openFileChooser(ActionEvent event) {
		Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Adicionar arquivo de áudio");
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Arquivos de áudio", "*.mp3")
		);
		File selectedFile = fileChooser.showOpenDialog(ownerWindow);
		if(selectedFile != null) {
			Song m = new Song();
			m.setAbsolutePath(
					new SimpleStringProperty(selectedFile.getAbsolutePath())
			);
			userDao.getLoggedUser().addSong(m);
			songDao.saveSongsFromUser(userDao.getLoggedUser());
		}
	}
	
	/**
	 * Faz o usuário escolher um diretório para adicionar
	 * utilizando o seletor de arquivos do sistema
	 * @param event
	 */
	public void openFolderChooser(ActionEvent event) {
		Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Adicionar arquivo de áudio");
		File selectedDir = directoryChooser.showDialog(ownerWindow);
		if(selectedDir != null) {
			Folder f = new Folder();
			f.setPath(new SimpleStringProperty(selectedDir.getAbsolutePath()));
			userDao.getLoggedUser().addFolder(f);
			folderDao.saveFoldersFromUser(userDao.getLoggedUser());
		}
	}
	
	@FXML
	public void initialize() {
		User loggedUser = userDao.getLoggedUser();
		Boolean isVip = loggedUser instanceof UserVIP;
		
		loggedUser.setDefaultPlaylist(new Playlist());
		songDao.loadSongsFromUser(loggedUser);
		folderDao.loadFoldersFromUser(loggedUser);
		
		if(isVip) {
			UserVIP vipLoggedUser = (UserVIP) loggedUser;
			playlistDao.loadPlaylistsFromUser(vipLoggedUser);
			
			playlistList.setItems(vipLoggedUser.getPlaylists());
			playlistList.setCellFactory(listView -> new PlaylistNameCell());
			playlistList.getSelectionModel().selectedItemProperty().addListener(selectedPlaylistListener());
			
			vipLabel.setText("USUÁRIO VIP");
		} else {
			vipLabel.setText("USUÁRIO COMUM");
		}
		
		nameLabel.setText(loggedUser.getName().getValue());
		
		songList.setCellFactory(listView -> new SongNameCell());
		songList.setItems(loggedUser.getDefaultPlaylist().getSongs());
		songList.getSelectionModel().selectedItemProperty().addListener(selectedDefaultSongListener());
	
		playlistSongList.setCellFactory(listView -> new SongNameCell());
		playlistSongList.getSelectionModel().selectedItemProperty().addListener(selectedSongListener());
		
		musicTitle.setText("Nada tocando ainda.");
		toggleButton.setText("Play");
		selectedPlaylist = loggedUser.getDefaultPlaylist();
		playingDefault = true;
	}
	
	/**
	 * Faz o programa tocar a música recebida
	 * @param newMusic Musica a ser tocada
	 */
	public void playMusic(Song newMusic) {
		String filePath = newMusic.getAbsolutePath().getValue();
        Media media = new Media(new File(filePath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        
        if(currentMediaPlayer != null) currentMediaPlayer.stop();
        
        currentMediaPlayer = mediaPlayer;
        bindPlayerToControls();
        currentMediaPlayer.play();
        musicTitle.setText(newMusic.getName());
        toggleButton.setText("Pause");
	}
	
	/**
	 * Adiciona os listeners que fazem a barra do tempo de música funcionar.
	 * A barra é atualizada automaticamente conforme a música passa.
	 * Se o usuário arrastar a barra e soltar o player de música retoma o som
	 * a partir do tempo selecionado.
	 */
	private void bindPlayerToControls() {
		currentMediaPlayer.currentTimeProperty().addListener(ov -> {
			double total = currentMediaPlayer.getTotalDuration().toMillis();
	        double current = currentMediaPlayer.getCurrentTime().toMillis();
	        timeSlider.setMax(total);
	        timeLabel.setText(Utilities.getTimeString(current) + "/" + Utilities.getTimeString(total));
			if(!isSliderChanging) {
		        timeSlider.setValue(current);
			}
	    });
		
		timeSlider.valueChangingProperty().addListener((ov, oldValue, newValue) -> {
			isSliderChanging = newValue;
			if(!isSliderChanging) {
				currentMediaPlayer.seek(Duration.millis(timeSlider.getValue()));
			}
		});
		
		currentMediaPlayer.setOnEndOfMedia(() -> {
			playNextSong();
		});
	}
	
	/**
	 * Pausa o player se houver música tocando.
	 * Retoma o player caso contrário.
	 */
	public void toggleMusic() {
		if(currentMediaPlayer == null) return;
		
		if(currentMediaPlayer.getStatus() == Status.PLAYING) {
			currentMediaPlayer.pause();
			toggleButton.setText("Play");
		} else {
			currentMediaPlayer.play();
			toggleButton.setText("Pause");
		}
	}
	
	/**
	 * Abre a tela de criação de playlist
	 */
	public void openNewPlaylistDialog() {
		try {
			if(!(userDao.getLoggedUser() instanceof UserVIP)) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Usuário não possui plano VIP");
				a.show();
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/NewPlaylistScreen.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Nova Playlist");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Faz o player tocar a próxima música da playlist
	 * que está tocando no momento
	 */
	public void playNextSong() {
		if(playingDefault) {
			Playlist defaultPlaylist = userDao.getLoggedUser().getDefaultPlaylist();
			defaultPlaylist.next();
			SimpleIntegerProperty index = defaultPlaylist.getCurrentSongIndex();
			songList.getSelectionModel().select(index.getValue());
		} else {
			selectedPlaylist.next();
			SimpleIntegerProperty index = selectedPlaylist.getCurrentSongIndex();
			playlistSongList.getSelectionModel().select(index.getValue());
		}
	}
	
	/**
	 * Faz o player tocar a música anterior da playlist que
	 * está tocando no momento
	 */
	public void playPreviousSong() {
		if(playingDefault) {
			Playlist defaultPlaylist = userDao.getLoggedUser().getDefaultPlaylist();
			defaultPlaylist.previous();
			SimpleIntegerProperty index = defaultPlaylist.getCurrentSongIndex();
			songList.getSelectionModel().select(index.getValue());
		} else {
			selectedPlaylist.previous();
			SimpleIntegerProperty index = selectedPlaylist.getCurrentSongIndex();
			playlistSongList.getSelectionModel().select(index.getValue());
		}
	}
	
	/**
	 * Retorna um listener que faz o player tocar a música selecionada
	 * pelo usuário, caso esta música não pertença à uma playlist customizada.
	 * @return
	 */
	private ChangeListener<Song> selectedDefaultSongListener() {
		return new ChangeListener<Song>() {
		    @Override
		    public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
		    	if(newValue == null) return;
		    	playMusic(newValue);
		    	Playlist defaultPlaylist = userDao.getLoggedUser().getDefaultPlaylist();
		    	Integer selectedIndex = defaultPlaylist.getSongs().indexOf(newValue);
		    	defaultPlaylist.setSong(selectedIndex);
		    	playingDefault = true;
		    }
		};
	}
	
	/**
	 * Retorna um listener que faz o player tocar a música selecionada
	 * pelo usuário, caso esta música pertença à uma playlist customizada.
	 * @return
	 */
	private ChangeListener<Song> selectedSongListener() {
		return new ChangeListener<Song>() {
		    @Override
		    public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
		    	if(newValue == null) return;
		    	playMusic(newValue);
		    	Integer selectedIndex = selectedPlaylist.getSongs().indexOf(newValue);
		    	selectedPlaylist.setSong(selectedIndex);
		    	playingDefault = false;
		    }
		};
	}
	
	/**
	 * Retorna um listener que faz o player trocar a playlist
	 * que está sendo tocada.
	 * @return
	 */
	private ChangeListener<Playlist> selectedPlaylistListener() {
		return new ChangeListener<Playlist>() {
		    @Override
		    public void changed(
		    		ObservableValue<? extends Playlist> observable, 
		    		Playlist oldValue, 
		    		Playlist newValue
		    ) {
		    	playlistTitleLabel.setText(newValue.getName().getValue());
		    	playlistSongList.setItems(newValue.getSongs());
		    	selectedPlaylist = newValue;
		    	playMusic(newValue.start());
		    	playingDefault = false;
		    }
		};
	}
}
