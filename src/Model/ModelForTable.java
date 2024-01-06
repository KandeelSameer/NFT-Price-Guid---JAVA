package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ModelForTable {
	private SimpleStringProperty collectionName;
	private SimpleStringProperty magicEdenPrice;
	private SimpleStringProperty openSeaPrice;
	private SimpleDoubleProperty diff;
	
	
	public ModelForTable(String collectionName, String magicEdenPrice,
			String openSeaPrice, Double diff) {
		
		this.collectionName = new SimpleStringProperty( collectionName);
		this.magicEdenPrice = new SimpleStringProperty(magicEdenPrice);
		this.openSeaPrice = new SimpleStringProperty(openSeaPrice);
		this.diff = new SimpleDoubleProperty(diff);
	}

	public String getCollectionName() {
		return collectionName.get();
	}
	public void setCollectionName(SimpleStringProperty collectionName) {
		this.collectionName = collectionName;
	}
	public String getMagicEdenPrice() {
		return magicEdenPrice.get();
	}
	public void setMagicEdenPrice(SimpleStringProperty magicEdenPrice) {
		this.magicEdenPrice = magicEdenPrice;
	}
	public String getOpenSeaPrice() {
		return openSeaPrice.get();
	}
	public void setOpenSeaPrice(SimpleStringProperty openSeaPrice) {
		this.openSeaPrice = openSeaPrice;
	}
	public Double getDiff() {
		return diff.get();
	}
	public void setDiff(SimpleDoubleProperty diff) {
		this.diff = diff;
	}
	
	public ModelForTable getCollect(String s) {
		if(getCollectionName().equals(s)) {
			return this;
		}
	return null;
	}

}
