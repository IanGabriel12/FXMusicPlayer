package br.ufrn.imd.control;

import br.ufrn.imd.model.Playlist;
import javafx.scene.control.ListCell;

public class PlaylistNameCell extends ListCell<Playlist> {
    @Override
    public void updateItem(Playlist item, boolean empty) {
    	super.updateItem(item, empty);
    	if(empty) {
    		this.setText(null);
    	} else {
    		this.setText(item.getName().getValue());
    	}
    }
}
