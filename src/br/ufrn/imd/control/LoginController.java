package br.ufrn.imd.control;

import java.io.IOException;

import br.ufrn.imd.dao.UserDAO;
import br.ufrn.imd.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController {
	@FXML
	private TextField nameField;
	
	@FXML
	private TextField passwordField;
	
	private UserDAO userDao = UserDAO.getInstance();
	
	/**
	 * Mostra a tela de cadastro de usuário.
	 */
	public void openRegistrationScreen() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/RegistrationScreen.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Cadastro");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Autentica o usuário no sistema, fazendo a janela principal aparecer quando as credenciais
	 * estão corretas
	 * @param event evento que acionou o método
	 */
	public void signIn(ActionEvent event) {
		String name = nameField.getText();
		String password = passwordField.getText();
		User u = userDao.findByCredentials(name, password);
		
		if(u == null) {
			showErrorAlert();
		} else {
			userDao.setLoggedUser(u);
			Window loginScreen = ((Node) event.getSource()).getScene().getWindow();
			openMainScreen(loginScreen);
		}
	}
	
	/**
	 * Mostra um alerta de erro de credenciais incorretas
	 */
	private void showErrorAlert() {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle("ERRO");
		a.setHeaderText("Não autorizado");
		a.setContentText("Usuário ou senha incorretos");
		a.show();
	}
	
	/**
	 * Muda a janela da aplicação da tela de login para a tela principal
	 * @param telaLogin Instância da janela de login
	 */
	private void openMainScreen(Window loginScreen) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/MainScreen.fxml"));
			Stage stage = new Stage();
			stage.setTitle("FXMusicPlayer");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			loginScreen.hide();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
}
