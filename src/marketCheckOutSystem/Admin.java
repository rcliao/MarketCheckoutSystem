package marketCheckOutSystem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Admin extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton jbtAdd, jbtDel, jbtList, jbtSoft, jbtSave, jbtLoad,
			jbtDone;

	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel modelInventory = new DefaultTableModel();
	DefaultTableModel modelTransactions = new DefaultTableModel();
	private JTable table = new JTable(model);

	private JRadioButton jrbInventory, jrbTransactions;

	private boolean inventorySource = false;

	private Inventory[] inventory;
	private Transactions[] transactions;

	public Admin() {

		// create a top menu panel to contain the object user want to make
		// change by using radio buttons since user can change only one at the
		// time
		JPanel topMenu = new JPanel(new GridLayout(1, 2, 5, 5));
		topMenu.add(jrbInventory = new JRadioButton("Inventory"));
		topMenu.add(jrbTransactions = new JRadioButton("Transactions"));

		ButtonGroup group = new ButtonGroup();
		group.add(jrbInventory);
		group.add(jrbTransactions);

		jrbInventory.setMnemonic('I');
		jrbTransactions.setMnemonic('T');

		jrbInventory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inventorySource = true;
			}
		});
		jrbTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inventorySource = false;
			}
		});

		// create another panel to contain the main menu
		JPanel mainMenu = new JPanel(new GridLayout(8, 1, 5, 5));
		mainMenu.add(jbtList = new JButton("List"));
		mainMenu.add(jbtAdd = new JButton("Add"));
		mainMenu.add(jbtDel = new JButton("Del"));
		mainMenu.add(jbtSoft = new JButton("Sort"));
		mainMenu.add(jbtSave = new JButton("Save"));
		mainMenu.add(jbtLoad = new JButton("Load"));
		mainMenu.add(jbtDone = new JButton("Done"));

		// create a list panel to display all the item of that object
		JPanel list = new JPanel(new BorderLayout(5, 5));
		JScrollPane pane = new JScrollPane(table);
		list.add(pane, BorderLayout.CENTER);
		table.setShowVerticalLines(false);
		table.setColumnSelectionAllowed(false);

		JPanel subPanel = new JPanel(new BorderLayout(10, 10));
		subPanel.add(mainMenu, BorderLayout.WEST);
		subPanel.add(list, BorderLayout.CENTER);

		JPanel main = new JPanel(new BorderLayout(10, 10));

		main.add(topMenu, BorderLayout.NORTH);
		main.add(subPanel, BorderLayout.CENTER);

		add(main);

		ButtonListener listener = new ButtonListener();

		jbtList.addActionListener(listener);
		jbtAdd.addActionListener(listener);
		jbtDel.addActionListener(listener);
		jbtSoft.addActionListener(listener);
		jbtSave.addActionListener(listener);
		jbtLoad.addActionListener(listener);
		jbtDone.addActionListener(listener);
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == jbtList) {
				// if user clicked inventory radio button, then display the list
				// as inventory
				if (inventorySource) {

					String[] tmpBuffer = Store.readFile("inventory.txt");
					int menuSize = tmpBuffer.length;

					inventory = new Inventory[menuSize];

					for (int i = 0; i < menuSize; i++) {
						String[] cols = tmpBuffer[i].split("\\s+");

						inventory[i] = new Inventory();

						inventory[i] = new Inventory(Integer.parseInt(cols[0]),
								cols[1], Double.parseDouble(cols[2]),
								Integer.parseInt(cols[3]),
								Integer.parseInt(cols[4]),
								Integer.parseInt(cols[5]));
					}
					
					// by setting the model of table to be model inventory
					table.setModel(modelInventory);

					modelInventory.setColumnCount(0);
					modelInventory.setRowCount(0);

					// refresh the list everytime the list button is clicked

					table.revalidate();

					if (modelInventory.getColumnCount() < 6) {
						modelInventory.addColumn("Item Code");
						modelInventory.addColumn("Item Name");
						modelInventory.addColumn("Item Price");
						modelInventory.addColumn("Item Tax Code");
						modelInventory.addColumn("Item Quanity");
						modelInventory.addColumn("Item Re Order Level");
					}
					for (int i = 0; i < inventory.length; i++) {
						if (modelInventory.getRowCount() < inventory.length) {
							modelInventory.addRow(new Object[] {
									inventory[i].getItemCode(),
									inventory[i].getDescription(),
									inventory[i].getPrice(),
									inventory[i].getTaxCode(),
									inventory[i].getQuanity(),
									inventory[i].getReOrderLevel() });
						}
					}
				} else {
					
					String[] buffer = readFile("transactions.txt");
					int bufferSize = buffer.length;

					transactions = new Transactions[bufferSize];

					for (int i = 0; i < bufferSize; i++) {
						String[] cols = buffer[i].split("\\s+");

						transactions[i] = new Transactions(Integer.parseInt(cols[0]),
								Integer.parseInt(cols[1]));

					}

					// else dispaly model transactions
					table.setModel(modelTransactions);

					modelTransactions.setColumnCount(0);
					modelTransactions.setRowCount(0);

					table.revalidate();

					if (modelTransactions.getColumnCount() < 2) {
						modelTransactions.addColumn("ID");
						modelTransactions.addColumn("Number");
					}
					for (int i = 0; i < transactions.length; i++) {
						if (modelTransactions.getRowCount() < transactions.length) {
							modelTransactions.addRow(new Object[] {
									transactions[i].getID(),
									transactions[i].getNumber() });
						}
					}
				}

			} else if (e.getSource() == jbtAdd) {
				// if the table displaying model inventory
				if (table.getModel() == modelInventory) {
					// and user didnt choose any row yet
					// then add row at the end
					if (table.getSelectedRow() != -1) {
						modelInventory.insertRow(table.getSelectedRow(),
								new Object[6]);
						// else add the row to be in front of the selected row
					} else {
						modelInventory.addRow(new Object[6]);
					}
				} else {
					if (table.getSelectedRow() != -1) {
						modelTransactions.insertRow(table.getSelectedRow(),
								new Object[2]);
					} else {
						modelTransactions.addRow(new Object[2]);
					}
				}

			} else if (e.getSource() == jbtDel) {
				// delete the selected row(s)
				if (table.getModel() == modelInventory) {
					if (table.getSelectedRow() < modelInventory.getRowCount()
							&& table.getSelectedRow() >= 0) {
						int rowsNumber = table.getSelectedRows().length;
						for (int i = 0; i < rowsNumber; i++) {
							modelInventory.removeRow(table.getSelectedRow());
						}
					}
				} else {
					if (table.getSelectedRow() < modelTransactions
							.getRowCount() && table.getSelectedRow() >= 0) {
						int rowsNumber = table.getSelectedRows().length;
						for (int i = 0; i < rowsNumber; i++) {
							modelTransactions.removeRow(table.getSelectedRow());
						}
					}
				}

			} else if (e.getSource() == jbtSoft) {
				// soft the inventory list by the item code except the last row,
				// which suppose to be zero
				if (table.getModel() == modelInventory) {
					for (int i = 0; i < inventory.length; i++) {

						double currentMin = inventory[i].getItemCode();
						int currentMinIndex = i;

						for (int j = i + 1; j < inventory.length - 1; j++) {
							if (currentMin > inventory[j].getItemCode()) {
								currentMin = inventory[j].getItemCode();
								currentMinIndex = j;
							}
						}

						if (currentMinIndex != i) {
							Inventory temp = new Inventory();
							temp = inventory[currentMinIndex];
							inventory[currentMinIndex] = inventory[i];
							inventory[i] = temp;
						}

					}

					modelInventory.setColumnCount(0);
					modelInventory.setRowCount(0);

					table.revalidate();

					if (modelInventory.getColumnCount() < 6) {
						modelInventory.addColumn("Item Code");
						modelInventory.addColumn("Item Name");
						modelInventory.addColumn("Item Price");
						modelInventory.addColumn("Item Tax Code");
						modelInventory.addColumn("Item Quanity");
						modelInventory.addColumn("Item Re Order Level");
					}
					for (int i = 0; i < inventory.length; i++) {
						if (modelInventory.getRowCount() < inventory.length) {
							modelInventory.addRow(new Object[] {
									inventory[i].getItemCode(),
									inventory[i].getDescription(),
									inventory[i].getPrice(),
									inventory[i].getTaxCode(),
									inventory[i].getQuanity(),
									inventory[i].getReOrderLevel() });
						}
					}
				}

			} else if (e.getSource() == jbtSave) {
				// save the object to file
				if (table.getModel() == modelInventory) {
					if (modelInventory.getRowCount() != table.getRowCount()) {
						modelInventory.setRowCount(table.getRowCount());
					}

					for (int i = 0; i < table.getRowCount(); i++) {
						for (int j = 0; j < table.getColumnCount(); j++) {

							modelInventory.setValueAt(table.getValueAt(i, j),
									i, j);
						}
					}

					while (inventory.length != modelInventory.getRowCount()) {
						inventory = new Inventory[modelInventory.getRowCount()];
					}

					for (int i = 0; i < modelInventory.getRowCount(); i++) {
						inventory[i] = new Inventory(
								Integer.parseInt(String.valueOf(modelInventory
										.getValueAt(i, 0))),
								String.valueOf(modelInventory.getValueAt(i, 1)),
								Double.parseDouble(String
										.valueOf(modelInventory
												.getValueAt(i, 2))), Integer
										.parseInt(String.valueOf(modelInventory
												.getValueAt(i, 3))), Integer
										.parseInt(String.valueOf(modelInventory
												.getValueAt(i, 4))), Integer
										.parseInt(String.valueOf(modelInventory
												.getValueAt(i, 5))));
					}

					try {
						saveFile(inventory, "inventory.txt");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					while (modelTransactions.getRowCount() != table
							.getRowCount()) {
						modelTransactions.setRowCount(table.getRowCount());
					}

					for (int i = 0; i < table.getRowCount(); i++) {
						for (int j = 0; j < table.getColumnCount(); j++) {

							modelTransactions.setValueAt(
									table.getValueAt(i, j), i, j);
						}
					}

					while (transactions.length != modelTransactions
							.getRowCount()) {
						transactions = new Transactions[modelTransactions
								.getRowCount()];
					}
					for (int i = 0; i < modelTransactions.getRowCount(); i++) {
						transactions[i] = new Transactions(
								Integer.parseInt(String
										.valueOf(modelTransactions.getValueAt(
												i, 0))),
								Integer.parseInt(String
										.valueOf(modelTransactions.getValueAt(
												i, 1))));
					}

					try {
						saveFile(transactions, "transactions.txt");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			} else if (e.getSource() == jbtLoad) {
				// load the object from file and refresh the list
				if (inventorySource) {
					java.io.File transactionsFile = new java.io.File(
							"transactions.txt");
					if (transactionsFile.exists()) {
						String[] buffer = readFile("transactions.txt");
						int bufferSize = buffer.length;

						transactions = new Transactions[bufferSize];

						for (int i = 0; i < bufferSize; i++) {
							String[] cols = buffer[i].split("\\s+");

							transactions[i] = new Transactions(
									Integer.parseInt(cols[0]),
									Integer.parseInt(cols[1]));

						}
					} else {
						try {
							new java.io.PrintWriter("transactions.txt");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

					modelInventory.setColumnCount(0);
					modelInventory.setRowCount(0);

					table.revalidate();

					if (modelInventory.getColumnCount() < 6) {
						modelInventory.addColumn("Item Code");
						modelInventory.addColumn("Item Name");
						modelInventory.addColumn("Item Price");
						modelInventory.addColumn("Item Tax Code");
						modelInventory.addColumn("Item Quanity");
						modelInventory.addColumn("Item Re Order Level");
					}
					for (int i = 0; i < inventory.length; i++) {
						if (modelInventory.getRowCount() < inventory.length) {
							modelInventory.addRow(new Object[] {
									inventory[i].getItemCode(),
									inventory[i].getDescription(),
									inventory[i].getPrice(),
									inventory[i].getTaxCode(),
									inventory[i].getQuanity(),
									inventory[i].getReOrderLevel() });
						}
					}

				} else {
					java.io.File inventoryFile = new java.io.File(
							"inventory.txt");
					if (inventoryFile.exists()) {
						String[] tmpBuffer = readFile("inventory.txt");
						int menuSize = tmpBuffer.length;

						inventory = new Inventory[menuSize];

						for (int i = 0; i < menuSize; i++) {
							String[] cols = tmpBuffer[i].split("\\s+");

							inventory[i] = new Inventory();

							inventory[i] = new Inventory(
									Integer.parseInt(cols[0]), cols[1],
									Double.parseDouble(cols[2]),
									Integer.parseInt(cols[3]),
									Integer.parseInt(cols[4]),
									Integer.parseInt(cols[5]));
						}
					} else {
						try {
							new java.io.PrintWriter("inventory.txt");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
					modelTransactions.setColumnCount(0);
					modelTransactions.setRowCount(0);

					table.revalidate();

					if (modelTransactions.getColumnCount() < 2) {
						modelTransactions.addColumn("ID");
						modelTransactions.addColumn("Number");
					}
					for (int i = 0; i < transactions.length; i++) {
						if (modelTransactions.getRowCount() < transactions.length) {
							modelTransactions.addRow(new Object[] {
									transactions[i].getID(),
									transactions[i].getNumber() });
						}
					}
				}
			} else if (e.getSource() == jbtDone) {

				// hide admin panel
				Store.admin.setVisible(false);
			}
		}
	}

	void saveFile(Inventory[] array, String filename) throws IOException {

		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < array.length; i++) {

				output.write(array[i].toString());
				output.write("\r\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} finally {
			output.close();
		}
	}

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

	void saveFile(Transactions[] array, String filename) throws IOException {

		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < array.length; i++) {

				output.write(array[i].toString());
				output.write("\r\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} finally {
			output.close();
		}
	}

	public void setData(Inventory[] inventory, Transactions[] transactions) {
		this.inventory = inventory;
		this.transactions = transactions;
	}

}
