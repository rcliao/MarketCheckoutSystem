package marketCheckOutSystem;

public class Transactions extends Inventory {
	// create constant to represent System operation
	public final static int SYSTEM_EXIT = 9993;
	public final static int SYSTEM_START = 9991;
	public final static int SYSTEM_END = 9992;
	
	// variables
	private int ID;
	private int number;
	
	// constructors
	public Transactions () {
		
	}
	
	public Transactions (int ID, int number) {
		this.ID = ID;
		this.number = number;
	}
	
	// return as String type
	public String toString() {
		
		if (ID != SYSTEM_EXIT || ID != SYSTEM_START || ID != SYSTEM_END)
			return ID + "\t" + number;
		else
			return ID + "\t" + "0";
	}
	
	// methods for each variables
	public int getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	
}
