package br.ufrn.imd.application;
	
import br.ufrn.imd.dao.UsuarioDAO;
import br.ufrn.imd.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/br/ufrn/imd/view/TelaLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UsuarioDAO uDao = UsuarioDAO.getInstance();
		uDao.loadUsuarios();
		for(Usuario u : uDao.getUsuarios()) {
			System.out.println(u.getNome());
		}
		launch(args);
	}
}
