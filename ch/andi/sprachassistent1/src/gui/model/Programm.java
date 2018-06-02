package gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Programm {
	
	private final ObjectProperty<CheckBox> aktiv;
	private final ObjectProperty<TextField> name;
	private final ObjectProperty<TextField> sprache;
	private final ObjectProperty<TextField> pfad;

	public Programm() {
		this(true, null, null, null);
	}
	
	public Programm(boolean aktiv, String sprache, String name, String pfad) {
		CheckBox checkbox = new CheckBox();
		checkbox.setSelected(aktiv);
		checkbox.setUserData(name.replaceAll(" ", "_"));

		this.aktiv = new SimpleObjectProperty<CheckBox>(checkbox);
		this.sprache = new SimpleObjectProperty<TextField>(new TextField(sprache));
		this.name = new SimpleObjectProperty<TextField>(new TextField(name));
		this.pfad = new SimpleObjectProperty<TextField>(new TextField(pfad));
	}
	
	public boolean isAktiv() {
		return aktiv.get().isSelected();
	}
	
	public void setAktiv(boolean aktiv) {
		this.aktiv.get().setSelected(aktiv);
	}
	
	public ObjectProperty<CheckBox> aktivProperty() {
		return aktiv;
	}
	
	public String getName() {
		return name.get().getText();
	}
	
	public void setName(String name) {
		this.name.set(new TextField(name));
	}
	
	public ObjectProperty<TextField> nameProperty() {
		return name;
	}
	
	public String getSprache() {
		return sprache.get().getText();
	}
	
	public void setSprache(String sprache) {
		this.sprache.set(new TextField(sprache));
	}
	
	public ObjectProperty<TextField> spracheProperty() {
		return sprache;
	}
	
	public String getPfad() {
		return pfad.get().getText();
	}
	
	public void setPfad(String pfad) {
		this.pfad.set(new TextField(pfad));
	}
	
	public ObjectProperty<TextField> pfadProperty() {
		return pfad;
	}
	
}