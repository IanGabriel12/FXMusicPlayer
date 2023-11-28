package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.ufrn.imd.model.Usuario;
import br.ufrn.imd.model.UsuarioVIP;

public class UsuarioDAO {
	private ArrayList<Usuario> usuarios;
	private String caminho = "src/br/ufrn/imd/data/usuarios.txt";
	private static UsuarioDAO instance;
	private Usuario usuarioLogado;
	
	/**
	 * Método getInstance do padrão SINGLETON
	 * @return Instancia do repositório
	 */
	public static UsuarioDAO getInstance() {
		if(instance == null) {
			instance = new UsuarioDAO();
		}
		
		return instance;
	}
	
	/**
	 * @return Retorna o usuário logado no sistema
	 */
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	/**
	 * Salva o usuário logado no sistema
	 * @param usuarioLogado Instancia do usuário logado
	 */
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	/**
	 * @return Retorna uma lista com todos os usuários
	 */
	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}
	
	/**
	 * Adiciona um novo usuário no sistema
	 * @param u Instancia do novo usuário
	 */
	public void addUsuario(Usuario u) {
		usuarios.add(u);
		u.setId(usuarios.size());
		saveIntoFile();
	}
	
	/**
	 * Encontra o usuário pelo username
	 * @param nome Nome para ser pesquisado
	 * @return Instancia do usuário com o username correspondente
	 */
	public Usuario findByUsername(String nome) {
		for(Usuario u : usuarios) {
			if(u.getNome().equals(nome)) {
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
	public Usuario findByCredentials(String nome, String senha) {
		for(Usuario u : usuarios) {
			if(u.getNome().getValue().equals(nome) && u.getSenha().getValue().equals(senha)) {
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Carrega os usuários do arquivo para a memória.
	 */
	public void loadUsuarios() {
		usuarios = new ArrayList<Usuario>();
		try {
			File arquivo = new File(caminho);
			System.out.println(arquivo.getAbsolutePath());
			FileReader fr = new FileReader(arquivo);
			try (BufferedReader br = new BufferedReader(fr)) {
				String nome;
				do {
					nome = br.readLine();
					if(nome == null) break;
					String senha = br.readLine();
					Integer id = Integer.parseInt(br.readLine());
					Boolean isVip = Boolean.parseBoolean(br.readLine());
					
					Usuario u;
					if(isVip) {
						u = new UsuarioVIP();
					} else {
						u = new Usuario();
					}
					u.setNome(nome);
					u.setSenha(senha);
					u.setId(id);
					
					usuarios.add(u);
				} while(nome != null);
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
			File arquivo = new File(caminho);
			FileWriter fr = new FileWriter(arquivo);
			try (BufferedWriter br = new BufferedWriter(fr)) {
				for(Usuario u : usuarios) {
					br.write(u.getNome().getValue());
					br.newLine();
					br.write(u.getSenha().getValue());
					br.newLine();
					br.write(Integer.toString(u.getId().getValue()));
					br.newLine();
					br.write(Boolean.toString(u instanceof UsuarioVIP));
					br.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
