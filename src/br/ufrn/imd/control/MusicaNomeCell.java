package br.ufrn.imd.control;

import br.ufrn.imd.model.Musica;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MusicaNomeCell extends ListCell<Musica> {
    @Override
    public void updateItem(Musica item, boolean empty) {
    	super.updateItem(item, empty);
    	if(empty) {
    		this.setText(null);
    	} else {
    		this.setText(item.getNome());
    	}
    }
}
