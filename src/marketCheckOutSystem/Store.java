package marketCheckOutSystem;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Store extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// construct inventory and transactions object
	private static Inventory[] inventory;
	private static Transactions[] transactions;

	// create JButtons
	private JButton jbtSeller, jbtBoss, jbtAdmin, jbtExit;

	// construct seller, boss, admin class
	static Seller seller = new Seller();
	static Boss boss = new Boss();
	static Admin admin = new Admin();

	// default constructor
	public Store() {
		// read file from inventory and transactions from file to create
		// inventory and transactions object array
		try {
			readConfigurationFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for some reason after reading inventory, the inventory file is empty
		// after reading; therefore save again just in case
		try {
			seller.saveFile(inventory, "inventory.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create top menuPanel to contains buttons
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(1, 4, 5, 5));
		menu.add(jbtSeller = new JButton("Seller"));
		menu.add(jbtBoss = new JButton("Boss"));
		menu.add(jbtAdmin = new JButton("Admin"));
		menu.add(jbtExit = new JButton("Exit"));

		// create button listener for each button
		ButtonListener listener = new ButtonListener();

		jbtSeller.addActionListener(listener);
		jbtBoss.addActionListener(listener);
		jbtAdmin.addActionListener(listener);
		jbtExit.addActionListener(listener);

		// set the menu panel at top
		add(menu, BorderLayout.NORTH);
	}

	// read file method which used in readConfigurationFiles
	static String[] readFile(String filename) {
		ArrayList<String> myBuffer = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				str.replaceFirst("\\s+", ""); // replace all leading space with
												// ""
				if (str.startsWith("#"))
					continue;
				myBuffer.add(str);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		String[] buffer = new String[myBuffer.size()];
		myBuffer.toArray(buffer);

		return buffer;

	}

	private void readConfigurationFiles() throws IOException {
		// read Inventory file
		java.io.File transactionsFile = new java.io.File("transactions.txt");
		if (transactionsFile.exists()) {
			String[] buffer = readFile("transactions.txt");
			int bufferSize = buffer.length;

			transactions = new Transactions[bufferSize];

			for (int i = 0; i < bufferSize; i++) {
				String[] cols = buffer[i].split("\\s+");

				transactions[i] = new Transactions(Integer.parseInt(cols[0]),
						Integer.parseInt(cols[1]));

			}
		} else {
			// if transactoins file does not exist, then create a transaction
			// file
			new java.io.PrintWriter("transactions.txt");

		}

		// read inventory file
		java.io.File inventoryFile = new java.io.File("inventory.txt");
		if (inventoryFile.exists()) {
			String[] tmpBuffer = readFile("inventory.txt");
			int menuSize = tmpBuffer.length;

			inventory = new Inventory[menuSize];

			for (int i = 0; i < menuSize; i++) {
				String[] cols = tmpBuffer[i].split("\\s+");

				inventory[i] = new Inventory();

				inventory[i] = new Inventory(Integer.parseInt(cols[0]),
						cols[1], Double.parseDouble(cols[2]),
						Integer.parseInt(cols[3]), Integer.parseInt(cols[4]),
						Integer.parseInt(cols[5]));
			}
			// if file does not exist, then create inventory file
		} else {
			new java.io.PrintWriter("inventory.txt");

		}

		java.io.File reportFile = new java.io.File("report.txt");

		if (reportFile.exists()) {

		} else {
			new java.io.PrintWriter("inventory.txt");

		}
	}

	// button listener class
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// if seller button is clicked, display the seller panel at the
			// bottom of menu panel, and transfer the inventory and transactions
			// data to the seller class
			// same as other buttons
			if (e.getSource() == jbtSeller) {
				try {
					readConfigurationFiles();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				seller.setVisible(true);
				boss.setVisible(false);
				admin.setVisible(false);
				seller.setData(inventory, transactions);
				add(seller, BorderLayout.CENTER);
			} else if (e.getSource() == jbtBoss) {
				boss.setVisible(true);
				seller.setVisible(false);
				admin.setVisible(false);
				add(boss, BorderLayout.CENTER);
				boss.setData(inventory, transactions);
			} else if (e.getSource() == jbtAdmin) {
				admin.setVisible(true);
				seller.setVisible(false);
				boss.setVisible(false);
				add(admin, BorderLayout.CENTER);
				admin.setData(inventory, transactions);
			} else if (e.getSource() == jbtExit) {
				System.exit(0);
			}
			// refresh the panel everytime a button is clicked
			invalidate();
			validate();
		}
	}

	public static void main(String[] args) {
		Store frame = new Store();
		frame.setSize(800, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle("Market Checkout System");
	}
}
