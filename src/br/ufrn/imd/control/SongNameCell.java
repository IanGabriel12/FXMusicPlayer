package br.ufrn.imd.control;

import br.ufrn.imd.model.Song;
import javafx.scene.control.ListCell;

public class SongNameCell extends ListCell<Song> {
    @Override
    public void updateItem(Song item, boolean empty) {
    	super.updateItem(item, empty);
    	if(empty) {
    		this.setText(null);
    	} else {
    		this.setText(item.getName());
    	}
    }
}
