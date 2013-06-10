package marketCheckOutSystem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Seller extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// create seller's inventory and transactions
	private Inventory[] inventory;
	private Transactions[] transactions;
	private int ID = -1;

	private JTextField jtfItemId, jtfItemName, jtfQuanity, jtfSubTotal,
			jtfTotal, jtfCash;
	private JTextArea jtaReciept;
	private JButton jbtBuy, jbtDel, jbtClear, jbtEndOfBuying, jbtEndOfDay;

	// use jtable to display the shopping list
	DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);

	// create another frame for displaying receipt
	private JFrame reciept = new JFrame();

	public Seller() {
		// create a item panel for user to input the purchased item
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2,1,5,5));
		
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new GridLayout(6, 1, 5, 5));
		itemPanel.add(new JLabel("Item Code"));
		itemPanel.add(jtfItemId = new JTextField(4));
		itemPanel.add(new JLabel("Item Name"));
		itemPanel.add(jtfItemName = new JTextField(8));
		itemPanel.add(new JLabel("Quanity"));
		itemPanel.add(jtfQuanity = new JTextField(4));

		JPanel CashPanel = new JPanel();
		CashPanel.setLayout(new GridLayout(2,1,5,5));
		CashPanel.add(new JLabel("Cash"));
		CashPanel.add(jtfCash = new JTextField("0", 4));
		
		TitledBorder CashTitle;
		CashTitle = BorderFactory.createTitledBorder("Money Receive");
		CashPanel.setBorder(CashTitle);
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Item Information");
		itemPanel.setBorder(title);
		
		inputPanel.add(itemPanel);
		inputPanel.add(CashPanel);

		// create a button panel for user to choose action
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new GridLayout(5, 1, 5, 5));
		ButtonPanel.add(jbtBuy = new JButton("Sell"));
		ButtonPanel.add(jbtDel = new JButton("Delete Item"));
		ButtonPanel.add(jbtClear = new JButton("Clear list"));
		ButtonPanel.add(jbtEndOfBuying = new JButton("Check Out"));
		ButtonPanel.add(jbtEndOfDay = new JButton("End of Day"));
		
		TitledBorder ButtonTitle;
		ButtonTitle = BorderFactory.createTitledBorder("Main Menu");
		ButtonPanel.setBorder(ButtonTitle);

		// create list sub panel to display how much items cost so far
		JPanel listSubPanel = new JPanel();
		listSubPanel.add(new JLabel("SubTotal:"));
		listSubPanel.add(jtfSubTotal = new JTextField("0", 12));
		jtfSubTotal.setEditable(false);
		listSubPanel.add(new JLabel("Total:"));
		listSubPanel.add(jtfTotal = new JTextField("0", 12));
		jtfTotal.setEditable(false);

		// create a list panel to contain jtable which is shopping list
		JPanel ListPanel = new JPanel();
		ListPanel.setLayout(new BorderLayout(2, 1));
		model.addColumn("Name");
		model.addColumn("Quanity");
		model.addColumn("Price");
		table.setShowVerticalLines(false);
		table.setColumnSelectionAllowed(false);
		JScrollPane pane = new JScrollPane(table);
		ListPanel.add(pane, BorderLayout.CENTER);
		ListPanel.add(listSubPanel, BorderLayout.SOUTH);

		// set all the panel in order
		setLayout(new BorderLayout(10, 10));
		add(inputPanel, BorderLayout.WEST);
		add(ButtonPanel, BorderLayout.CENTER);
		add(ListPanel, BorderLayout.EAST);

		// set receipt to be invisible unless displaying it
		JScrollPane Recieptpane = new JScrollPane(jtaReciept = new JTextArea());
		reciept.add(Recieptpane);
		reciept.setVisible(false);

		ButtonListener listener = new ButtonListener();

		jbtBuy.addActionListener(listener);
		jbtDel.addActionListener(listener);
		jbtClear.addActionListener(listener);
		jbtEndOfBuying.addActionListener(listener);
		jbtEndOfDay.addActionListener(listener);

		jtfItemId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < inventory.length; i++) {
					if (Integer.parseInt(jtfItemId.getText()) == inventory[i]
							.getItemCode()) {
						jtfItemName.setText(inventory[i].getDescription());
						jtfQuanity.setText("1");
						ID = i;
					}
				}
			}
		});
		jtfItemName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < inventory.length; i++) {
					if (jtfItemName.getText().equalsIgnoreCase(
							inventory[i].getDescription())) {
						jtfItemId.setText(String.valueOf(inventory[i]
								.getItemCode()));
						jtfQuanity.setText("1");
						ID = i;
					}
				}
			}
		});
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// create method for buy button clicked
			if (e.getSource() == jbtBuy) {

				// add the item from the item panel to the list
				if (ID != -1
						&& Integer.parseInt(jtfQuanity.getText()) > 0
						&& Integer.parseInt(jtfQuanity.getText()) <= inventory[ID]
								.getQuanity()) {
					model.addRow(new Object[] {
							inventory[ID].getDescription(),
							jtfQuanity.getText(),
							inventory[ID].getPrice() + "*"
									+ jtfQuanity.getText() });

					// add the item price to the subtotal
					jtfSubTotal.setText(String.format(
							"%.2f",
							Double.parseDouble(jtfSubTotal.getText())
									+ inventory[ID].getPrice()
									* Integer.parseInt(jtfQuanity.getText())));

					// if item is taxable, add price * 1.09 to the total
					if (inventory[ID].getTaxCode() == 1) {
						jtfTotal.setText(String.format(
								"%.2f",
								Double.parseDouble(jtfTotal.getText())
										+ inventory[ID].getPrice()
										* Integer.parseInt(jtfQuanity.getText())
										* 1.09));
					} else
						jtfTotal.setText(String.format(
								"%.2f",
								Double.parseDouble(jtfTotal.getText())
										+ inventory[ID].getPrice()
										* Integer.parseInt(jtfQuanity.getText())));
				} else {
					// if item is not enter yet, display the error message by
					// using receipt frame
					jtaReciept.setText("");
					reciept.setVisible(true);
					reciept.setSize(400, 70);
					jtaReciept.setEditable(false);
					jtaReciept
							.append("System Error: The Item you enter is in wrong format!"
									+ "\n or there is not this item yet"
									+ "\n or the quantity you enter is lower the stock in");
				}

			} else if (e.getSource() == jbtDel) {
				// if user select a row or rows from jtable then do the delete
				// action
				if (table.getSelectedRow() < model.getRowCount()
						&& table.getSelectedRow() >= 0) {
					int rowsNumber = table.getSelectedRows().length;
					for (int i = 0; i < rowsNumber; i++) {

						for (int j = 0; j < inventory.length; j++) {
							if (table.getSelectedRow() < model.getRowCount()
									&& table.getSelectedRow() >= 0) {
								if (String.valueOf(
										model.getValueAt(
												table.getSelectedRow(), 0))
										.equalsIgnoreCase(
												inventory[j].getDescription())) {

									model.removeRow(table.getSelectedRow());
									// while deleting row from shopping list,
									// also minus the price of item from sub
									// total and total
									jtfSubTotal.setText(String.valueOf(Double
											.parseDouble(jtfSubTotal.getText())
											- inventory[j].getPrice()
											* Integer.parseInt(jtfQuanity
													.getText())));

									if (inventory[j].getTaxCode() == 1) {
										jtfTotal.setText(String.format(
												"%.2f",
												Double.parseDouble(jtfTotal
														.getText())
														- inventory[j]
																.getPrice()
														* Integer
																.parseInt(jtfQuanity
																		.getText())
														* 1.09));
									} else
										jtfTotal.setText(String.valueOf(Double
												.parseDouble(jtfTotal.getText())
												- inventory[j].getPrice()
												* Integer.parseInt(jtfQuanity
														.getText())));
								}
							}
						}
					}
				}

			} else if (e.getSource() == jbtClear) {
				// claer all the list to be none by removing all the rows
				int length = model.getRowCount();
				for (int i = 0; i < length; i++) {
					model.removeRow(0);
				}
				// and set the sub total and total to be zero again since the
				// shopping list contains nothing
				jtfSubTotal.setText("0");
				jtfTotal.setText("0");
			} else if (e.getSource() == jbtEndOfBuying) {
				// refresh the receipt frame to be none first, then display the
				// error message of cash < total
				if (Double.parseDouble(jtfCash.getText()) < Double
						.parseDouble(jtfTotal.getText())) {
					jtaReciept.setText("");
					reciept.setVisible(true);
					reciept.setSize(400, 70);
					jtaReciept.setEditable(false);
					jtaReciept
							.append("System Error: The Cash is less than Total, you cannot check out!");
				} else if (model.getRowCount() == 0) {
					// if the shopping list is empty, then not allowing user to
					// check out
					jtaReciept.setText("");
					reciept.setVisible(true);
					reciept.setSize(400, 70);
					jtaReciept.setEditable(false);
					jtaReciept
							.append("System Error: The shopping list is empty!");
				} else {
					// if no error happened, then do the check out method
					int transactionLength;
					if (transactions != null)
						transactionLength = transactions.length;
					else
						transactionLength = 0;

					// refresh the receipt to be none again, so that it can be
					// appeneed stuffes
					jtaReciept.setText("");
					reciept.setVisible(true);
					reciept.setSize(400, 400);
					jtaReciept.setEditable(true);
					// appened the title, the address, and cashier name of store
					jtaReciept
							.append("\tWelcome to\n"
									+ "\tTest Store Name\n"
									+ "\tTest Address 1234\n"
									+ "\tLos Angeles, CA 90032\n"
									+ "\n"
									+ "\tTest Cashier Name: Eric\n"
									+ "--------------------------------------------------------------------------\n" +
									"Code\tName\tQty\tPrice\n");
					if (transactions != null) {

						// adding SYSTEM_START to the transactions object
						Transactions[] tmp = new Transactions[transactionLength];
						tmp = transactions.clone();
						transactions = new Transactions[transactionLength + 1];
						for (int k = 0; k < transactionLength; k++) {
							transactions[k] = tmp[k];
						}
						transactions[transactionLength] = new Transactions(
								Transactions.SYSTEM_START, 0);

					} else {
						transactions = new Transactions[1];
						transactions[0] = new Transactions(
								Transactions.SYSTEM_START, 0);
					}

					int length = model.getRowCount();
					for (int i = 0; i < length; i++) {
						// while removing each item, updated the inventory, and
						// transactions files and objects
						// and adding information to the receipt
						for (int j = 0; j < inventory.length; j++)
							if (String.valueOf(model.getValueAt(0, 0))
									.equalsIgnoreCase(
											inventory[j].getDescription())) {
								inventory[j].sellout(Integer.parseInt(String
										.valueOf(model.getValueAt(0, 1))));
								jtaReciept.append(""
										+ inventory[j].getItemCode()
										+ "\t"
										+ inventory[j].getDescription()
										+ "     \t"
										+ model.getValueAt(0, 1)
										+ "\t"
										+ inventory[j].getPrice()
										* Integer.parseInt(String.valueOf(model
												.getValueAt(0, 1))) + "\n");
								transactionLength = transactions.length;
								Transactions[] tmp = new Transactions[transactionLength];
								tmp = transactions.clone();
								transactions = new Transactions[transactionLength + 1];
								for (int k = 0; k < transactionLength; k++) {
									transactions[k] = tmp[k];
								}
								transactions[transactionLength] = new Transactions(
										inventory[j].getItemCode(),
										Integer.parseInt(String.valueOf(model
												.getValueAt(0, 1))));
							}
						model.removeRow(0);
					}

					// after removing all the rows in shopping list, append the
					// cash and subtotal to the receipt
					jtaReciept
							.append("\nTEST CASH:"
									+ "\t\t\t"
									+ jtfCash.getText()
									+ "\n"
									+ "SubTotal:\t\t\t"
									+ jtfSubTotal.getText()
									+ "\n"
									+ "Tax:\t\t\t"
									+ String.format("%.2f",
											(Double.parseDouble(jtfTotal
													.getText()) - Double
													.parseDouble(jtfSubTotal
															.getText())))
									+ "\n"
									+ "Total:\t\t\t"
									+ jtfTotal.getText()
									+ "\n"
									+ "Change:\t\t\t"
									+ String.format(
											"%.2f",
											Double.parseDouble(jtfCash
													.getText())
													- Double.parseDouble(jtfTotal
															.getText())));
					// reset the subtotal and total to be zero
					jtfSubTotal.setText("0");
					jtfTotal.setText("0");

					// then adding SYSTEM_END to transactions object
					transactionLength = transactions.length;
					Transactions[] tmp = new Transactions[transactionLength];
					tmp = transactions.clone();
					transactions = new Transactions[transactionLength + 1];
					for (int i = 0; i < transactionLength; i++) {
						transactions[i] = tmp[i];
					}
					transactions[transactionLength] = new Transactions(
							Transactions.SYSTEM_END, 0);

					// update inventory and transactions to the file
					try {
						saveFile(inventory, "inventory.txt");
						saveFile(transactions, "transactions.txt");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			} else if (e.getSource() == jbtEndOfDay) {

				int length = model.getRowCount();

				// clear all the shopping list remaining
				for (int i = 0; i < length; i++) {
					model.removeRow(0);
				}

				// add the SYSTEM_EXIT to the transactions object and save it to
				// file
				int transactionLength = transactions.length;
				Transactions[] tmp = new Transactions[transactionLength];
				tmp = transactions.clone();
				transactions = new Transactions[transactionLength + 1];
				for (int i = 0; i < transactionLength; i++) {
					transactions[i] = tmp[i];
				}
				transactions[transactionLength] = new Transactions(
						Transactions.SYSTEM_EXIT, 0);

				try {
					saveFile(transactions, "transactions.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Store.seller.setVisible(false);
			}

		}
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

	// save the transfered data to this class
	public void setData(Inventory[] inventory, Transactions[] transactions) {
		this.inventory = inventory;
		this.transactions = transactions;
	}

}
