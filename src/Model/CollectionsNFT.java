package Model;

public class CollectionsNFT {

	private String Collection;

	private double floorPrice;
	public CollectionsNFT(String collection, double floorPrice) {
		super();
		Collection = collection;
		this.floorPrice = floorPrice;
	}
	public CollectionsNFT() {
		// TODO Auto-generated constructor stub
	}
	public String getCollection() {
		return Collection;
	}
	public void setCollection(String collection) {
		Collection = collection;
	}
	public double getFloorPrice() {
		return floorPrice;
	}
	public void setFloorPrice(double floorPrice) {
		this.floorPrice = floorPrice;
	}

}
