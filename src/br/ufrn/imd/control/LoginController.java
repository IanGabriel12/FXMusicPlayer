package br.ufrn.imd.control;

import java.io.IOException;

import br.ufrn.imd.dao.UsuarioDAO;
import br.ufrn.imd.model.Usuario;
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
	private TextField nomeField;
	
	@FXML
	private TextField senhaField;
	
	private UsuarioDAO uDao = UsuarioDAO.getInstance();
	
	/**
	 * Mostra a tela de cadastro de usuário.
	 */
	public void abrirTelaCadastro() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/TelaCadastro.fxml"));
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
	public void fazerLogin(ActionEvent event) {
		String nome = nomeField.getText();
		String senha = senhaField.getText();
		Usuario u = uDao.findByCredentials(nome, senha);
		
		if(u == null) {
			mostrarAlertaErro();
		} else {
			uDao.setUsuarioLogado(u);
			Window telaLogin = ((Node) event.getSource()).getScene().getWindow();
			abrirTelaPrincipal(telaLogin);
		}
	}
	
	/**
	 * Mostra um alerta de erro de credenciais incorretas
	 */
	private void mostrarAlertaErro() {
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
	private void abrirTelaPrincipal(Window telaLogin) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/TelaPrincipal.fxml"));
			Stage stage = new Stage();
			stage.setTitle("FXMusicPlayer");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			telaLogin.hide();
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
}
