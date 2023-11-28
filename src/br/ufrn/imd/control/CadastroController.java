package br.ufrn.imd.control;

import br.ufrn.imd.dao.UsuarioDAO;
import br.ufrn.imd.model.Usuario;
import br.ufrn.imd.model.UsuarioVIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CadastroController {
	@FXML
	private TextField nomeField;
	@FXML
	private TextField senhaField;
	@FXML
	private CheckBox vipField;
	
	private UsuarioDAO uDao = UsuarioDAO.getInstance();
	
	/**
	 * Valida os campos do formulário e envia os dados para o repositório de usuários.
	 * @param event
	 */
	public void cadastrarUsuario(ActionEvent event) {
		String nome = nomeField.getText();
		String senha = senhaField.getText();
		Boolean isVip = vipField.isSelected();
		
		if(nome.isBlank()) {
			mostrarAlerta("Campo 'nome' é obrigatório", true);
		} else if (senha.isBlank()) {
			mostrarAlerta("Campo 'senha' é obrigatório", true);
		} else {
			Usuario u = uDao.findByUsername(nome);
			
			if(u != null) {
				mostrarAlerta("Nome de usuário já está em uso", true);
			} else {
				Usuario novoUsuario;
				
				if(isVip) {
					novoUsuario = new UsuarioVIP();
				} else {
					novoUsuario = new Usuario();
				}
				novoUsuario.setNome(nome);
				novoUsuario.setSenha(senha);
				uDao.addUsuario(novoUsuario);
				mostrarAlerta("Usuário cadastrado com sucesso", false);
				fecharTelaCadastro(event);
			}
		}
	}
	
	/**
	 * Desativa a tela de cadastro
	 * @param event
	 */
	public void fecharTelaCadastro(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	/**
	 * Mostra alerta na tela
	 * @param mensagem Mensagem do alerta
	 * @param isErro Determina se o alerta é de erro ou de sucesso
	 */
	private void mostrarAlerta(String mensagem, boolean isErro) {
		Alert a = new Alert(AlertType.ERROR);
		if(isErro) {
			a.setAlertType(AlertType.ERROR);
		} else {
			a.setAlertType(AlertType.INFORMATION);
		}
		
		a.setContentText(mensagem);
		a.show();
	}
}
