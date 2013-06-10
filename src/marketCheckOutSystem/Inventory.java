package marketCheckOutSystem;


public class Inventory {
	// create constant to represent code's end
	public final int CODE_END = 0;
	
	// variables for the inventory object
	private int itemCode;
	private String description;
	private double price;
	private int taxCode;
	private int quanity;
	private int reOrderLevel;
	
	// constructor
	public Inventory() {
		
	}
	
	public Inventory(int itemCode, String description, double price, int taxCode, int quanity, int reOrderLevel) {
		this.itemCode = itemCode;
		this.description = description;
		this.price = price;
		this.taxCode = taxCode;
		this.quanity = quanity;
		this.reOrderLevel = reOrderLevel;
	}
	
	// return to String type
	public String toString() {
		if (itemCode != 0)
			return itemCode + "\t" + description + "\t" + price + "\t" + taxCode + "\t" + quanity + "\t" + reOrderLevel;
		else
			return "0\t" + "*" + "\t0\t0\t0\t0";
	}
	
	// methods for each variable
	public int getItemCode() {
		return itemCode;
	}
	
	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getTaxCode() {
		return taxCode;
	}
	
	public void setTaxCode(int taxCode) {
		this.taxCode = taxCode;
	}
	
	public int getQuanity() {
		return quanity;
	}
	
	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}
	
	public int getReOrderLevel() {
		return reOrderLevel;
	}
	
	public void setReOrderLevel(int reOrderLevel) {
		this.reOrderLevel = reOrderLevel;
	}
	
	// method for selling item or buying item
	public void buyIn() {
		quanity += 1;
	}
	
	public void buyIn(int number) {
		quanity += number;
	}
	
	public void sellout () {
		quanity -= 1;
	}
	
	public void sellout(int number) {
		this.quanity -= number;
	}
	
	
}