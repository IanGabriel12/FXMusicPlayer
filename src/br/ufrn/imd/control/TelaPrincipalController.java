package br.ufrn.imd.control;

import java.io.File;
import java.io.IOException;

import br.ufrn.imd.dao.MusicaDAO;
import br.ufrn.imd.dao.UsuarioDAO;
import br.ufrn.imd.model.Musica;
import br.ufrn.imd.model.Playlist;
import br.ufrn.imd.model.UsuarioVIP;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TelaPrincipalController {
	@FXML
	private Label nomeLabel;
	@FXML
	private Label vipLabel;
	@FXML
	private ListView<Musica> listaMusicas;
	@FXML
	private Label musicTitle;
	@FXML
	private Button toggleButton;
	@FXML
	private ListView<Playlist> playlistList;
	
	private UsuarioDAO usuarioDao = UsuarioDAO.getInstance();
	private MusicaDAO musicaDao = MusicaDAO.getInstance();
	
	private MediaPlayer currentMediaPlayer;
	ObservableList<Musica> musicList;
	ObservableList<Playlist> playlistItems;
	
	
	public void logout(ActionEvent event) {
		try {
			usuarioDao.setUsuarioLogado(null);
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/TelaLogin.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			((Node) event.getSource()).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void abrirSelecaoArquivo(ActionEvent event) {
		Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Adicionar arquivo de áudio");
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Arquivos de áudio", "*.wav", "*.mp3")
		);
		File selectedFile = fileChooser.showOpenDialog(ownerWindow);
		if(selectedFile != null) {
			Musica m = new Musica();
			m.setCaminhoAbsoluto(selectedFile.getAbsolutePath());
			m.setDeDiretorio(false);
			musicaDao.addMusica(m);
			musicList.add(m);
		}
	}
	
	public void abrirSelecaoDiretorio(ActionEvent event) {
		Window ownerWindow = ((Node) event.getSource()).getScene().getWindow();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Adicionar arquivo de áudio");
		File selectedDir = directoryChooser.showDialog(ownerWindow);
	}
	
	@FXML
	public void initialize() {
		nomeLabel.setText(usuarioDao.getUsuarioLogado().getNome().getValue());
		Boolean isVip = usuarioDao.getUsuarioLogado() instanceof UsuarioVIP;
		
		if(isVip) {
			vipLabel.setText("USUÁRIO VIP");
			playlistItems = playlistDao.getLoggedUserPlaylists();
		} else {
			vipLabel.setText("USUÁRIO COMUM");
		}
		
		listaMusicas.setCellFactory(listView -> new MusicaNomeCell());
		musicList = FXCollections.observableArrayList(musicaDao.getMusicas());
		listaMusicas.setItems(musicList);
		listaMusicas.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Musica>() {
				    @Override
				    public void changed(ObservableValue<? extends Musica> observable, Musica oldValue, Musica newValue) {
				    	playMusic(newValue);
				    }
				}
		);
		
		playlistList.setItems(playlistItems);
		playlistList.setCellFactory(listView -> new PlaylistNameCell());
		musicTitle.setText("Nada tocando ainda.");
		toggleButton.setText("Play");
	}
	
	public void playMusic(Musica newMusic) {
		String filePath = newMusic.getCaminhoAbsoluto().getValue();
        Media media = new Media(new File(filePath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if(currentMediaPlayer != null) currentMediaPlayer.stop();
        currentMediaPlayer = mediaPlayer;
        currentMediaPlayer.play();
        musicTitle.setText(newMusic.getNome());
        toggleButton.setText("Pause");
	}
	
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
	
	public void openNewPlaylistDialog() {
		try {
			if(!(usuarioDao.getUsuarioLogado() instanceof UsuarioVIP)) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Usuário não possui plano VIP");
				a.show();
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/TelaNovaPlaylist.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
