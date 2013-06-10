package marketCheckOutSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
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
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Boss extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Inventory[] inventory;
	private Transactions[] transactions;

	private JTextField jtfQuanity, jtfValue;
	private JButton jbtList, jbtBuy, jbtDel, jbtClear, jbtEndOfBuying,
			jbtEndOfDay, jbtPrintReceipt;
	private JSlider jslHort = new JSlider(JSlider.HORIZONTAL);

	DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);

	private JTextArea jtaReceipt = new JTextArea();
	private JFrame receipt = new JFrame();

	public Boss() {

		// prepare to print the whole receipt
		JScrollPane Recieptpane = new JScrollPane(jtaReceipt = new JTextArea());
		receipt.add(Recieptpane);
		receipt.setVisible(false);

		// setting slider of item
		jslHort.setMaximum(500);
		jslHort.setPaintLabels(true);
		jslHort.setPaintTicks(true);
		jslHort.setMajorTickSpacing(100);
		jslHort.setMinorTickSpacing(1);
		jslHort.setPaintTrack(false);

		// create an information panel for user to input how much they want to
		// buy or order
		JPanel InfoPanel = new JPanel();
		InfoPanel.setLayout(new GridLayout(5, 1));
		InfoPanel.add(new JLabel("Quanity"), FlowLayout.LEFT);
		InfoPanel.add(jtfQuanity = new JTextField());
		InfoPanel.add(jslHort);
		InfoPanel.add(new JLabel("Value"));
		InfoPanel.add(jtfValue = new JTextField("0"));
		jtfValue.setEditable(false);
		
		TitledBorder InfoTitle;
		InfoTitle = BorderFactory.createTitledBorder("Input Info");
		InfoPanel.setBorder(InfoTitle);

		// create a button panel to contains buttons
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new GridLayout(7, 1, 5, 5));
		ButtonPanel.add(jbtList = new JButton("List"));
		ButtonPanel.add(jbtBuy = new JButton("Buy"));
		ButtonPanel.add(jbtDel = new JButton("Cancel Buying"));
		ButtonPanel.add(jbtClear = new JButton("Select All"));
		ButtonPanel.add(jbtEndOfBuying = new JButton("Order"));
		ButtonPanel.add(jbtEndOfDay = new JButton("End of Day"));
		ButtonPanel.add(jbtPrintReceipt = new JButton("Today's Receipt"));

		// create a list panel to contain the jtable
		JPanel ListPanel = new JPanel();
		ListPanel.setLayout(new BorderLayout(2, 1));
		model.addColumn("Name");
		model.addColumn("Quanity");
		model.addColumn("Stock");
		model.addColumn("Price");
		table.setShowVerticalLines(false);
		table.setColumnSelectionAllowed(false);
		// setting personal cell render to display the item which is under
		// reorderlevel
		table.setDefaultRenderer(Object.class, new MyTableCellRender());
		JScrollPane pane = new JScrollPane(table);
		ListPanel.add(pane, BorderLayout.CENTER);

		setLayout(new BorderLayout(10, 10));
		add(InfoPanel, BorderLayout.WEST);
		add(ButtonPanel, BorderLayout.CENTER);
		add(ListPanel, BorderLayout.EAST);

		ButtonListener listener = new ButtonListener();

		jslHort.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = jslHort.getValue();

				jtfQuanity.setText(String.valueOf(value));
			}
		});

		jbtList.addActionListener(listener);
		jbtBuy.addActionListener(listener);
		jbtDel.addActionListener(listener);
		jbtClear.addActionListener(listener);
		jbtEndOfBuying.addActionListener(listener);
		jbtEndOfDay.addActionListener(listener);
		jbtPrintReceipt.addActionListener(listener);
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jbtList) {
				// for everytime the list button is clickced, readFile from the
				// inventory.txt first so that the inventory object is updated
				// all the time

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

				// refresh model
				model.setRowCount(0);

				// add every inventory object to the row of model in order to
				// dispaly them
				for (int i = 0; i < inventory.length; i++) {
					if (model.getRowCount() < inventory.length) {
						model.addRow(new Object[] {
								inventory[i].getDescription(),
								inventory[i].getQuanity(), "0",
								inventory[i].getPrice() });
						if (inventory[i].getQuanity() < inventory[i]
								.getReOrderLevel()) {

						}
					}
				}

			} else if (e.getSource() == jbtDel) {
				// if user selected a row in table
				if (table.getSelectedRow() < model.getRowCount()
						&& table.getSelectedRow() >= 0) {
					int rowsNumber = table.getSelectedRows().length;
					for (int i = 0; i < rowsNumber; i++) {

						for (int j = 0; j < inventory.length; j++) {

							if (table.getSelectedRow() < model.getRowCount()
									&& table.getSelectedRow() >= 0) {
								// reduce the value of this buying for every
								// deleting row
								jtfValue.setText(String.format(
										"%.2f",
										Double.parseDouble(jtfValue.getText())
												- Double.parseDouble(String.valueOf(model.getValueAt(
														table.getSelectedRow(),
														3)))
												* Integer
														.parseInt((String) model.getValueAt(
																table.getSelectedRow(),
																2))));
								// not deleting row, but set the stock to be
								// zero
								model.setValueAt("0", table.getSelectedRow(), 2);
								// after setting it to be zero, change to
								// another selected row
								table.removeRowSelectionInterval(
										table.getSelectedRow(),
										table.getSelectedRow());

							}
						}
					}
				}

			} else if (e.getSource() == jbtBuy) {
				// after order, update the inventory's quantity
				if (table.getSelectedRow() < model.getRowCount()
						&& table.getSelectedRow() >= 0) {
					int rowsNumber = table.getSelectedRows().length;
					for (int i = 0; i < rowsNumber; i++) {

						if (table.getSelectedRow() < model.getRowCount()
								&& table.getSelectedRow() >= 0) {
							if (Integer.parseInt(String.valueOf(jtfQuanity
									.getText())) > 0) {
								jtfValue.setText(String.format(
										"%.2f",
										Double.parseDouble(jtfValue.getText())
												+ Double.parseDouble(String.valueOf(model.getValueAt(
														table.getSelectedRow(),
														3)))
												* Integer.parseInt(jtfQuanity
														.getText())));
								model.setValueAt(
										""
												+ (Integer
														.parseInt((String) model.getValueAt(
																table.getSelectedRow(),
																2)) + Integer
														.parseInt(jtfQuanity
																.getText())),
										table.getSelectedRow(), 2);
								table.removeRowSelectionInterval(
										table.getSelectedRow(),
										table.getSelectedRow());
							}

						}
					}
				}

			} else if (e.getSource() == jbtClear) {
				// set selection to be all the list on table
				table.addRowSelectionInterval(0, table.getRowCount() - 1);

			} else if (e.getSource() == jbtEndOfBuying) {
				// after end of day, update the inventory object and save it to
				// file
				for (int i = 0; i < table.getRowCount(); i++) {
					model.setValueAt(
							""
									+ (Integer.parseInt(String.valueOf(model
											.getValueAt(i, 2))) + Integer
											.parseInt(String.valueOf(model
													.getValueAt(i, 1)))), i, 1);
					model.setValueAt("0", i, 2);
					inventory[i].setQuanity(Integer.parseInt(String
							.valueOf(model.getValueAt(i, 1))));
				}
			} else if (e.getSource() == jbtEndOfDay) {
				for (int i = 0; i < table.getRowCount(); i++) {
					try {
						saveFile(inventory, "inventory.txt");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				Store.boss.setVisible(false);
			} else if (e.getSource() == jbtPrintReceipt) {
				receipt.setVisible(true);
				receipt.setSize(400,600);
				jtaReceipt.setText("");
				for (int i = 0; i < transactions.length; i++) {
					if (transactions[i].getID() == 9991) {
						jtaReceipt.append("-----------------------------------------------------------\n");
						jtaReceipt.append("Starting Of Buying\n");
						jtaReceipt.append("-----------------------------------------------------------\n");
					}
					for (int j = 0; j < inventory.length; j++) {
						if (transactions[i].getID() == inventory[j]
								.getItemCode()) {
							jtaReceipt
									.append("" + inventory[j].getItemCode()
											+ "\t"
											+ inventory[j].getDescription()
											+ "\t"
											+ transactions[i].getNumber()
											+ "\n");
						}
					}
					if (transactions[i].getID() == 9992) {
						jtaReceipt.append("-----------------------------------------------------------\n");
						jtaReceipt.append("End Of Customer Buying\n");
						jtaReceipt.append("-----------------------------------------------------------\n");
					}
					if (transactions[i].getID() == 9993) {
						jtaReceipt.append("-----------------------------------------------------------\n");
						jtaReceipt.append("End of Day\n");
						jtaReceipt.append("-----------------------------------------------------------\n");
					}
				}
			}
			repaint();

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

	public void setData(Inventory[] inventory, Transactions[] transactions) {
		this.inventory = inventory;
		this.transactions = transactions;
	}

	class MyTableCellRender extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyTableCellRender() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			// if this item is under the re order level, the display red as back
			// ground color, else display normally
			if (Integer.parseInt(String.valueOf(table.getValueAt(row, 1))) < inventory[row]
					.getReOrderLevel()) {
				setForeground(Color.black);
				setBackground(Color.red);
			} else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			// setting the selecting color to be blue
			if (isSelected) {
				setForeground(Color.blue);
			}
			setText(value != null ? value.toString() : "");
			return this;
		}
	}
}
