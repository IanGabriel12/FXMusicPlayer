package br.ufrn.imd.control;

import br.ufrn.imd.dao.UserDAO;
import br.ufrn.imd.model.User;
import br.ufrn.imd.model.UserVIP;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class RegistrationController {
	@FXML
	private TextField nameField;
	@FXML
	private TextField passwordField;
	@FXML
	private CheckBox vipField;
	
	private UserDAO uDao = UserDAO.getInstance();
	
	/**
	 * Valida os campos do formulário e envia os dados para o repositório de usuários.
	 * @param event
	 */
	public void createUser(ActionEvent event) {
		String name = nameField.getText();
		String password = passwordField.getText();
		Boolean isVip = vipField.isSelected();
		
		if(name.isBlank()) {
			showAlert("Campo 'nome' é obrigatório", true);
		} else if (password.isBlank()) {
			showAlert("Campo 'senha' é obrigatório", true);
		} else {
			User u = uDao.findByUsername(name);
			
			if(u != null) {
				showAlert("Nome de usuário já está em uso", true);
			} else {
				User newUser;
				
				if(isVip) {
					newUser = new UserVIP();
				} else {
					newUser = new User();
				}
				newUser.setName(new SimpleStringProperty(name));
				newUser.setPassword(new SimpleStringProperty(password));
				uDao.addUser(newUser);
				showAlert("Usuário cadastrado com sucesso", false);
				closeRegistrationScreen(event);
			}
		}
	}
	
	/**
	 * Desativa a tela de cadastro
	 * @param event
	 */
	public void closeRegistrationScreen(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	/**
	 * Mostra alerta na tela
	 * @param mensagem Mensagem do alerta
	 * @param isErro Determina se o alerta é de erro ou de sucesso
	 */
	private void showAlert(String mensagem, boolean isErro) {
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
