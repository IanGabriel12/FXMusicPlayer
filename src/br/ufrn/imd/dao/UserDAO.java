package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.ufrn.imd.model.User;
import br.ufrn.imd.model.UserVIP;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserDAO {
	private ObservableList<User> users;
	private String path = "src/br/ufrn/imd/data/usuarios.txt";
	private static UserDAO instance;
	private User loggedUser;
	
	/**
	 * Método getInstance do padrão SINGLETON
	 * @return Instancia do repositório
	 */
	public static UserDAO getInstance() {
		if(instance == null) {
			instance = new UserDAO();
		}
		
		return instance;
	}
	
	/**
	 * @return Retorna o usuário logado no sistema
	 */
	public User getLoggedUser() {
		return loggedUser;
	}

	/**
	 * Salva o usuário logado no sistema
	 * @param usuarioLogado Instancia do usuário logado
	 */
	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	/**
	 * @return Retorna uma lista com todos os usuários
	 */
	public ObservableList<User> getUsers() {
		return users;
	}
	
	/**
	 * Adiciona um novo usuário no sistema
	 * @param u Instancia do novo usuário
	 */
	public void addUser(User u) {
		users.add(u);
		u.setId(new SimpleIntegerProperty(users.size()));
		saveIntoFile();
	}
	
	/**
	 * Encontra o usuário pelo username
	 * @param nome Nome para ser pesquisado
	 * @return Instancia do usuário com o username correspondente
	 */
	public User findByUsername(String name) {
		for(User u : users) {
			if(u.getName().getValue().equals(name)) {
				return u;
			}
		}
		return null;
	}
	
	
	/**
	 * Encontra o usuário pelas suas credenciais (nome e senha)
	 * @param nome Nome para ser pesquisado
	 * @param senha Senha para ser pesquisada
	 * @return Instancia do usuário que possui estas credenciais
	 */
	public User findByCredentials(String name, String password) {
		for(User u : users) {
			if(u.getName().getValue().equals(name) && u.getPassword().getValue().equals(password)) {
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Carrega os usuários do arquivo para a memória.
	 */
	public void loadUsers() {
		users = FXCollections.observableArrayList();
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			try (BufferedReader br = new BufferedReader(fr)) {
				String name;
				do {
					name = br.readLine();
					if(name == null) break;
					String password = br.readLine();
					Integer id = Integer.parseInt(br.readLine());
					Boolean isVip = Boolean.parseBoolean(br.readLine());
					
					User u;
					if(isVip) {
						u = new UserVIP();
					} else {
						u = new User();
					}
					u.setName(new SimpleStringProperty(name));
					u.setPassword(new SimpleStringProperty(password));
					u.setId(new SimpleIntegerProperty(id));
					
					users.add(u);
				} while(name != null);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Atualiza o arquivo de usuários
	 */
	private void saveIntoFile() {
		try {
			File file = new File(path);
			FileWriter fr = new FileWriter(file);
			try (BufferedWriter br = new BufferedWriter(fr)) {
				for(User u : users) {
					br.write(u.getName().getValue());
					br.newLine();
					br.write(u.getPassword().getValue());
					br.newLine();
					br.write(Integer.toString(u.getId().getValue()));
					br.newLine();
					br.write(Boolean.toString(u instanceof UserVIP));
					br.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
