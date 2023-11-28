module FXMusicPlayer {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.base;
	
	opens br.ufrn.imd.application to javafx.graphics, javafx.fxml;
	opens br.ufrn.imd.control to javafx.fxml;
}
