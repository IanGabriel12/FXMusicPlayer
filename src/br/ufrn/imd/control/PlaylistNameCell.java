package br.ufrn.imd.control;

import br.ufrn.imd.model.Musica;
import br.ufrn.imd.model.Playlist;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PlaylistNameCell extends ListCell<Playlist> {
    @Override
    public void updateItem(Playlist item, boolean empty) {
    	super.updateItem(item, empty);
    	if(empty) {
    		this.setText(null);
    	} else {
    		this.setText(item.getNome().getValue());
    	}
    }
}
