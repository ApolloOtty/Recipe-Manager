import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.TextArea;
import javax.swing.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.FlowLayout;

/**

The Dashboard class creates a GUI for the user to interact with. It allows the user to create a recipe and add ingredients to it.

It also displays a table of all the user's recipes and their details.

@author Diaconu Mihai Alexandru

@version 1.0

@since 28/01/2023
*/

public class Dashboard {

	JFrame dashboard;
	private JTextField Name;
	private JTextField Category;
	public static JComboBox ingredientName;
	private JTextField weight;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void dashboard() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard frame = new Dashboard(null);
					frame.dashboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Functie pentru filtrarea retetelor pe mai multe criterii. 
	//Returneaza un model pentru a adauga numele retetelor in JList.
	private DefaultListModel updateQuery(String filterOption, String category, String sortOrder, int userID) throws ClassNotFoundException, SQLException {
		// Initialize the query with the basic SELECT statement 
		if(filterOption.equals("Filter by Calories")) {
			filterOption = "ingredients.ingredientCalories";
		} else if(filterOption.equals("Filter by Protein")) {
			filterOption = "ingredients.protein";
		} else if(filterOption.equals("Filter by Sugar")) {
			filterOption = "ingredients.sugar";
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
		ResultSet res = null;
		if(sortOrder.equals("ASC") && filterOption.equals("Filter by Time")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT name from recipe WHERE userID='"+userID+"' ORDER BY time ASC");
			res = pstmt.executeQuery();
		}
		else if (sortOrder.equals("DESC") && filterOption.equals("Filter by Time")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT name from recipe WHERE userID='"+userID+"' ORDER BY time DESC");
			res = pstmt.executeQuery();
		}
		else if (sortOrder.equals("ASC") && !category.equals("All")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT recipe.name as name, SUM("+filterOption+"*recipeingredients.weight/100)"
					+ "as total FROM recipe" +
					" JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID"+
					" JOIN ingredients ON recipeingredients.recipeIngredientName = ingredients.ingredientName"+
					" WHERE recipe.category = ? AND recipe.userID = ?"+
					" GROUP BY recipe.recipeID, recipe.name"+
					" ORDER BY total ASC");
			pstmt.setString(1, category);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
		} else if (sortOrder.equals("DESC") && !category.equals("All")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT recipe.name as name, SUM("+filterOption+"*recipeingredients.weight/100)"
					+ "as total FROM recipe" +
					" JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID"+
					" JOIN ingredients ON recipeingredients.recipeIngredientName = ingredients.ingredientName"+
					" WHERE recipe.category = ? AND recipe.userID = ?"+
					" GROUP BY recipe.recipeID, recipe.name"+
					" ORDER BY total DESC");
			pstmt.setString(1, category);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
		}else if (sortOrder.equals("DESC") && category.equals("All")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT recipe.name as name, SUM("+filterOption+"*recipeingredients.weight/100)"
					+ "as total FROM recipe" +
					" JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID"+
					" JOIN ingredients ON recipeingredients.recipeIngredientName = ingredients.ingredientName"+
					" WHERE recipe.userID = ?"+
					" GROUP BY recipe.recipeID, recipe.name"+
					" ORDER BY total DESC");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
		}else if (sortOrder.equals("ASC") && category.equals("All")) {
			PreparedStatement pstmt = con.prepareStatement("SELECT recipe.name as name, SUM("+filterOption+"*recipeingredients.weight/100)"
					+ "as total FROM recipe" +
					" JOIN recipeingredients ON recipe.recipeID = recipeingredients.recipeID"+
					" JOIN ingredients ON recipeingredients.recipeIngredientName = ingredients.ingredientName"+
					" WHERE recipe.userID = ?"+
					" GROUP BY recipe.recipeID, recipe.name"+
					" ORDER BY total ASC");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
		}

		if(!res.isBeforeFirst()){
			System.out.println("No result found");
		}
		DefaultListModel model = new DefaultListModel();
		while(res.next()) {
			model.addElement(res.getString("name"));
		}
		return model;

	}


	public Dashboard(User utilizator) throws ClassNotFoundException, SQLException {
		initialize(utilizator);
	}
//Initialize dashboard. Here is where most of the operations are.
	private void initialize(User utilizator) throws ClassNotFoundException, SQLException {
		List<String> ingredientNamesArray = new ArrayList<String>();
		List<Float> weightArray = new ArrayList<Float>();
		dashboard = new JFrame();
		dashboard.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 643, 526);
		dashboard.getContentPane().add(tabbedPane);



		JPanel panel = new JPanel();
		panel.setBackground(new Color(72, 61, 139));
		panel.setToolTipText("");
		tabbedPane.addTab("View Recipes", null, panel, null);
		JComboBox comboBox = new JComboBox();
		
		JButton favorite = new JButton("");
		favorite.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\pngwing.com.png"));
		favorite.setBounds(166, 77, 33, 22);
		panel.add(favorite);
		favorite.setOpaque(false);
		favorite.setFocusPainted(false);
		favorite.setBorderPainted(false);
		favorite.setContentAreaFilled(false);
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT distinct category FROM recipe where userID='" + User.UserID + "'");

			// Create a JComboBox

			comboBox.setBounds(335, 11, 216, 22);
			JLabel caloriesMain = new JLabel("");
			caloriesMain.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
			caloriesMain.setForeground(new Color(255, 255, 255));
			caloriesMain.setBounds(10, 434, 56, 14);
			panel.add(caloriesMain);


			JLabel proteinMain = new JLabel("");
			proteinMain.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
			proteinMain.setForeground(new Color(255, 255, 255));
			proteinMain.setBounds(83, 434, 46, 14);
			panel.add(proteinMain);

			JLabel sugarMain = new JLabel("");
			sugarMain.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
			sugarMain.setForeground(new Color(255, 255, 255));
			sugarMain.setBounds(153, 434, 46, 14);
			panel.add(sugarMain);
			// Add each item from the ResultSet to the combo box
			while (rs.next()) {
				comboBox.addItem(rs.getString("category"));
			}
			
			comboBox.setSelectedIndex(-1);
			panel.setLayout(null);
			panel.add(comboBox);
			JList list = new JList();
			list.setBackground(SystemColor.activeCaption);
			list.setBounds(10, 110, 189, 288);
			panel.add(list);
			JTextArea instructionsTextArea = new JTextArea();
			JTextArea ingredientsTextArea = new JTextArea();
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					caloriesMain.setText("");
					proteinMain.setText("");
					sugarMain.setText("");
					instructionsTextArea.setText("");
					ingredientsTextArea.setText("");
					Object selectedItemCat = comboBox.getSelectedItem();
					JLabel lblNewLabel_1_1 = new JLabel("Name");
					lblNewLabel_1_1.setForeground(new Color(245, 255, 250));
					lblNewLabel_1_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
					lblNewLabel_1_1.setBounds(55, 69, 107, 30);
					panel.add(lblNewLabel_1_1);
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root",
								"");
						Statement st = con.createStatement();
						String sql = "Select name from recipe where category='" + selectedItemCat + "' AND userID='"+User.UserID+"'";
						ResultSet res = st.executeQuery(sql);
						DefaultListModel model = new DefaultListModel();
						while (res.next()) {
							String name = res.getString("name");
							model.addElement(name);
							Recipe recipe = new Recipe();
							list.addListSelectionListener(new ListSelectionListener() {
								public void valueChanged(ListSelectionEvent e) {
									try {
										if(recipe.checkIfFavorite(list.getSelectedValue(), User.UserID)) {
											favorite.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\pngwing.com.png"));
											favorite.repaint();
										}else favorite.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\pngwing.com (1).png"));
										favorite.repaint();
									} catch (SQLException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
									Object selectedItem = list.getSelectedValue();
									Statement st;
									try {

										JLabel ingredientsLabel = new JLabel("Ingredients");
										ingredientsLabel.setForeground(new Color(245, 255, 250));
										ingredientsLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
										ingredientsLabel.setBounds(209, 100, 107, 30);
										panel.add(ingredientsLabel);
										ingredientsLabel.repaint();

										JLabel recipeLabel = new JLabel("Recipe ");
										recipeLabel.setForeground(new Color(245, 255, 250));
										recipeLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
										recipeLabel.setBounds(223, 289, 67, 22);
										panel.add(recipeLabel);

										recipeLabel.repaint();
		

										caloriesMain.setText(recipe.getCalories(User.UserID, selectedItem));
										proteinMain.setText(recipe.getProtein(User.UserID, selectedItem));
										sugarMain.setText(recipe.getSugar(User.UserID, selectedItem));

										ingredientsTextArea.setText(recipe.viewIngredients(selectedItem, User.UserID));
										// textArea_1.setEnabled(false); // Disable the text area
										ingredientsTextArea.setEditable(false); // Make the text area read-only
										//ingredientsTextArea.setLineWrap(true);
										ingredientsTextArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
										ingredientsTextArea.setWrapStyleWord(true);
										ingredientsTextArea.setColumns(10); // Set the word limit to 10 words per row
										ingredientsTextArea.setBounds(335, 47, 216, 118);
										panel.add(ingredientsTextArea);

										JScrollPane scrollIngredients = new JScrollPane(ingredientsTextArea);
										scrollIngredients.setBounds(335, 47, 216, 118);
										scrollIngredients.setVerticalScrollBarPolicy(
												ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
										panel.add(scrollIngredients);

										instructionsTextArea.setText(recipe.viewInstructions(selectedItem) + "\n\n\n"+
										"Time needed: "+recipe.getRecipeTime(selectedItem, User.UserID));
										// textArea_2.setEnabled(false); // Disable the text area
										instructionsTextArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
										instructionsTextArea.setEditable(false); // Make the text area read-only
										instructionsTextArea.setLineWrap(true);
										instructionsTextArea.setWrapStyleWord(true);
										instructionsTextArea.setColumns(10); // Set the word limit to 10 words per row
										instructionsTextArea.setBounds(333, 211, 232, 187);
										panel.add(instructionsTextArea);

										JScrollPane scroll = new JScrollPane(instructionsTextArea);
										scroll.setBounds(333, 211, 232, 187);
										scroll.setVerticalScrollBarPolicy(
												ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
										panel.add(scroll);

										JComboBox comboBox = new JComboBox();
										comboBox.setBounds(335, 11, 216, 22);
										panel.add(comboBox);


									} catch (SQLException e1) {
										e1.printStackTrace();
									}

									// do something with the selected item
								}
							});
							favorite.repaint();

							favorite.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
								
									boolean buttonPressed = false;
									if (!buttonPressed) {
										buttonPressed = true;
										favorite.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\pngwing.com.png"));
									}
									favorite.setPressedIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\pngwing.com.png"));
									favorite.repaint();
									try {
										if(!recipe.checkIfFavorite(list.getSelectedValue(), User.UserID)) {
											recipe.insertFavorite(list.getSelectedValue(), User.UserID);
										}
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									User user = new User(User.UserID);
									dashboard.dispose();
									Dashboard reg;
									try {
										reg = new Dashboard(user);
										reg.dashboard.setVisible(true);
									} catch (ClassNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								}
							});
						}
						list.setModel(model);
					} catch (Exception err) {
						System.out.print(err);
					}

				}
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel categoryLabel = new JLabel("Category");
		categoryLabel.setForeground(new Color(245, 255, 250));
		categoryLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		categoryLabel.setBounds(183, 4, 107, 30);
		panel.add(categoryLabel);


		JLabel lblNewLabel_1 = new JLabel("Calories");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 409, 56, 14);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Protein");
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(83, 409, 46, 14);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Sugar");
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(153, 409, 46, 14);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\Screenshot_14_69x62.png"));
		lblNewLabel.setBounds(10, 11, 69, 62);
		panel.add(lblNewLabel);


		// Add Recipe Tab
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(72, 61, 139));
		tabbedPane.addTab("Add Recipe", null, panel_1, null);
		panel_1.setLayout(null);

		Name = new JTextField();
		Name.setBounds(178, 11, 172, 29);
		panel_1.add(Name);
		Name.setColumns(10);

		JLabel nameLabelAdd = new JLabel("Name");
		nameLabelAdd.setBounds(10, 15, 79, 14);
		nameLabelAdd.setForeground(new Color(245, 255, 250));
		nameLabelAdd.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(nameLabelAdd);

		JLabel categoryLabelAdd = new JLabel("Category");
		categoryLabelAdd.setBounds(10, 52, 90, 26);
		categoryLabelAdd.setForeground(new Color(245, 255, 250));
		categoryLabelAdd.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(categoryLabelAdd);

		JLabel ingredientNameAdd = new JLabel("Ingredient name");
		ingredientNameAdd.setBounds(10, 105, 145, 29);
		ingredientNameAdd.setForeground(new Color(245, 255, 250));
		ingredientNameAdd.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(ingredientNameAdd);

		JTextArea Recipe = new JTextArea();
		Recipe.setBounds(181, 287, 169, 123);
		panel_1.add(Recipe);
		JScrollPane scrollPane = new JScrollPane(Recipe);
		scrollPane.setBounds(181, 287, 169, 123);
		panel_1.add(scrollPane);
		Recipe.setLineWrap(true);
		Recipe.setWrapStyleWord(true);
		Recipe.setRows(7);

		JLabel recipeLabelAdd = new JLabel("Recipe");
		recipeLabelAdd.setBounds(10, 337, 90, 28);
		recipeLabelAdd.setForeground(new Color(245, 255, 250));
		recipeLabelAdd.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(recipeLabelAdd);
		// JScrollPane sp = new JScrollPane(Ingredients,
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// panel_1.add(sp);

		JLabel timeLabelAdd = new JLabel("Time needed");
		timeLabelAdd.setBounds(10, 420, 114, 28);
		timeLabelAdd.setForeground(new Color(245, 255, 250));
		timeLabelAdd.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(timeLabelAdd);

		JComboBox Time = new JComboBox();
		Time.setModel(new DefaultComboBoxModel(new String[] { "10 minutes", "15 minutes", "30 minutes", "45 minutes",
				"1 hour", "1:30 hours", "2 hours" }));
		Time.setBounds(178, 426, 172, 22);
		panel_1.add(Time);

		JButton addRecipe = new JButton("Add Recipe");
		addRecipe.setBounds(203, 464, 128, 23);

		

		addRecipe.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		panel_1.add(addRecipe);

		Category = new JTextField();
		Category.setBounds(178, 55, 172, 26);
		panel_1.add(Category);
		Category.setColumns(10);

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT distinct ingredientName FROM ingredients");
			ingredientName = new JComboBox();
			ingredientName.setBounds(178, 109, 172, 26);
			panel_1.add(ingredientName);
			while (rs.next()) {
				ingredientName.addItem(rs.getString("ingredientName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		AutoCompleteDecorator.decorate(ingredientName);

		weight = new JTextField();
		weight.setBounds(178, 146, 172, 26);
		weight.setColumns(10);
		panel_1.add(weight);

		JLabel lblNewLabel_5_1 = new JLabel("Weight (grams)");
		lblNewLabel_5_1.setBounds(10, 142, 145, 29);
		lblNewLabel_5_1.setForeground(new Color(245, 255, 250));
		lblNewLabel_5_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_1.add(lblNewLabel_5_1);

		String[] columnNames = { "Ingredient name", "Weight (grams)" };

		// Create a new table model with 0 rows
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		// Create a new table using the model
		JTable table = new JTable(model);
		table.setBounds(448, 61, 168, 333);

		// Add rows to the table
		panel_1.add(table);

		JButton addIngredient = new JButton("Add");
		addIngredient.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addIngredient.setBounds(359, 135, 79, 14);
		addIngredient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ingredient ing = new Ingredient();
				if (ing.verifyIngredient((String) ingredientName.getSelectedItem(),
						Float.parseFloat(weight.getText()))) {
					ingredientNamesArray.add((String) ingredientName.getSelectedItem());
					weightArray.add(Float.parseFloat(weight.getText()));
					model.addRow(new Object[] { (String) ingredientName.getSelectedItem(),
							Float.parseFloat(weight.getText()) });
				} else {
					AddIngredient reg = new AddIngredient();
					reg.setVisible(true);
				}
			}
		});
		panel_1.add(addIngredient);

		JLabel lblNewLabel_8 = new JLabel("Ingredient not on the list?");
		lblNewLabel_8.setForeground(new Color(255, 255, 255));
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_8.setBounds(188, 183, 178, 20);
		panel_1.add(lblNewLabel_8);

		JButton ingredientNotOnList = new JButton("Add it here");
		ingredientNotOnList.setBounds(203, 214, 122, 22);
		panel_1.add(ingredientNotOnList);
		ingredientNotOnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddIngredient ing = new AddIngredient();
				ing.setVisible(true);
			}
		});

		JLabel lblNewLabel_9 = new JLabel("Name");
		lblNewLabel_9.setForeground(new Color(255, 255, 255));
		lblNewLabel_9.setBounds(462, 36, 33, 14);
		panel_1.add(lblNewLabel_9);

		JLabel lblNewLabel_10 = new JLabel("Weight");
		lblNewLabel_10.setForeground(new Color(255, 255, 255));
		lblNewLabel_10.setBounds(557, 36, 46, 14);
		panel_1.add(lblNewLabel_10);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(76, 61, 139));
		tabbedPane.addTab("Filter Recipes", null, panel_2, null);
		panel_2.setLayout(null);

		JComboBox filterType = new JComboBox();
		filterType.setModel(new DefaultComboBoxModel(new String[] {"Filter by Calories", "Filter by Protein", "Filter by Sugar", "Filter by Time"}));
		filterType.setBounds(136, 11, 234, 22);
		filterType.setSelectedIndex(-1);
		panel_2.add(filterType);

		JButton Asc = new JButton("Sort Ascending");
		Asc.setBounds(10, 96, 130, 23);
		panel_2.add(Asc);

		JButton Desc = new JButton("Sort Descending");
		Desc.setBounds(10, 143, 130, 23);
		panel_2.add(Desc);

		JList listFilter = new JList();
		listFilter.setBounds(10, 195, 163, 279);
		panel_2.add(listFilter);



		JTextArea ingredientArea = new JTextArea();
		ingredientArea.setBounds(410, 50, 197, 200);
		panel_2.add(ingredientArea);

		JTextArea recipeArea = new JTextArea();
		recipeArea.setBounds(410, 261, 197, 200);
		panel_2.add(recipeArea);


		JLabel calories = new JLabel("");
		calories.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		calories.setForeground(new Color(255, 255, 255));
		calories.setBounds(185, 457, 46, 14);
		panel_2.add(calories);

		JLabel protein = new JLabel("");
		protein.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		protein.setForeground(new Color(255, 255, 255));
		protein.setBounds(254, 460, 46, 14);
		panel_2.add(protein);

		
		JLabel sugar = new JLabel("");
		sugar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		sugar.setForeground(new Color(255, 255, 255));
		sugar.setBounds(324, 457, 46, 14);
		panel_2.add(sugar);

		listFilter.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Recipe recipe = new Recipe();
				Object selectedItem = listFilter.getSelectedValue();
				calories.setText("");
				Statement st;
				try {
					ingredientArea.setText(recipe.viewIngredients(selectedItem, User.UserID));
					// textArea_1.setEnabled(false); // Disable the text area
					ingredientArea.setEditable(false); // Make the text area read-only
					ingredientArea.setLineWrap(true);
					ingredientArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
					ingredientArea.setWrapStyleWord(true);
					ingredientArea.repaint();
					JScrollPane scrollIngredients = new JScrollPane(ingredientArea);
					scrollIngredients.setBounds(410, 50, 197, 200);
					scrollIngredients.setVerticalScrollBarPolicy(
							ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					panel_2.add(scrollIngredients);
					calories.setText(recipe.getCalories(User.UserID, selectedItem));
					protein.setText(recipe.getProtein(User.UserID, selectedItem));
					sugar.setText(recipe.getSugar(User.UserID, selectedItem));
					recipeArea.setText(recipe.viewInstructions(selectedItem) + "\n\n\n" +"Time needed: "+recipe.getRecipeTime(selectedItem, User.UserID));
					// textArea_2.setEnabled(false); // Disable the text area
					recipeArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
					recipeArea.setEditable(false); // Make the text area read-only
					recipeArea.setLineWrap(true);
					recipeArea.setWrapStyleWord(true);


					JScrollPane scroll = new JScrollPane(recipeArea);
					scroll.setBounds(410, 261, 197, 200);
					scroll.setVerticalScrollBarPolicy(
							ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					panel_2.add(scroll);

					JComboBox comboBox = new JComboBox();
					comboBox.setBounds(335, 11, 216, 22);
					panel.add(comboBox);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});


		JLabel lblNewLabel_14 = new JLabel("Ingredients");
		lblNewLabel_14.setForeground(new Color(255, 255, 255));
		lblNewLabel_14.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_14.setBounds(254, 128, 116, 23);
		panel_2.add(lblNewLabel_14);

		JLabel lblNewLabel_15 = new JLabel("Recipe");
		lblNewLabel_15.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_15.setForeground(new Color(255, 255, 255));
		lblNewLabel_15.setBounds(254, 336, 85, 22);
		panel_2.add(lblNewLabel_15);

		JLabel lblNewLabel_16 = new JLabel("Calories");
		lblNewLabel_16.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_16.setForeground(new Color(255, 255, 255));
		lblNewLabel_16.setBounds(185, 432, 59, 14);
		panel_2.add(lblNewLabel_16);

		JLabel lblNewLabel_17 = new JLabel("Protein");
		lblNewLabel_17.setForeground(new Color(255, 255, 255));
		lblNewLabel_17.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_17.setBounds(254, 432, 60, 14);
		panel_2.add(lblNewLabel_17);

		JLabel lblNewLabel_18 = new JLabel("Sugar");
		lblNewLabel_18.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		lblNewLabel_18.setForeground(new Color(255, 255, 255));
		lblNewLabel_18.setBounds(324, 432, 46, 14);
		panel_2.add(lblNewLabel_18);

		JComboBox categoryFilter = new JComboBox();
		categoryFilter.setBounds(136, 44, 234, 22);
		panel_2.add(categoryFilter);
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proitectp3", "root", "");
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT distinct category FROM recipe where userID='" + User.UserID + "'");
		while (rs.next()) {
			categoryFilter.addItem(rs.getString("category"));
		}
		categoryFilter.addItem("All");	
		categoryFilter.setSelectedIndex(-1);
		
		JLabel lblNewLabel_13 = new JLabel("Pick a filter ");
		lblNewLabel_13.setForeground(new Color(255, 255, 255));
		lblNewLabel_13.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		lblNewLabel_13.setBounds(10, 15, 102, 14);
		panel_2.add(lblNewLabel_13);
		
		JLabel lblNewLabel_22 = new JLabel("Pick a category");
		lblNewLabel_22.setForeground(new Color(255, 255, 255));
		lblNewLabel_22.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		lblNewLabel_22.setBounds(10, 42, 116, 22);
		panel_2.add(lblNewLabel_22);
		dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dashboard.setBounds(100, 100, 669, 576);
		String filterOption=null;
		filterType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		String filterOptionFinal = filterOption;
		// Add the event listener to the category comboBox
		categoryFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to execute when the category comboBox value is changed
				String category = (String) categoryFilter.getSelectedItem();
				// Add the event listener to the ascending button 
			}    
		});
		Asc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to execute when the ascending button is clicked
				try {
					listFilter.setModel(updateQuery((String) filterType.getSelectedItem(), (String) categoryFilter.getSelectedItem(), "ASC", User.UserID));
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
		});
		// Add the event listener to the descending button
		Desc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to execute when the descending button is clicked
				try {
					listFilter.setModel(updateQuery((String) filterType.getSelectedItem(), (String) categoryFilter.getSelectedItem(), "DESC", User.UserID));
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		Recipe recipe = new Recipe();
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(76, 61, 128));
		tabbedPane.addTab("Favorite Recipes", null, panel_3, null);
		panel_3.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(392, 42, 236, 201);
		panel_3.add(textArea);
		textArea.setEditable(false); // Make the text area read-only
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(392, 270, 236, 206);
		panel_3.add(textArea_1);
		textArea_1.setEditable(false); // Make the text area read-only
		textArea_1.setLineWrap(true);
		textArea_1.setWrapStyleWord(true);
		
		JList list = new JList();
		list.setBounds(10, 46, 206, 343);
		panel_3.add(list);
		list.setModel(recipe.getFavoriteRecipes(User.UserID));
		
		JLabel caloriesFav = new JLabel("");
		caloriesFav.setForeground(new Color(255, 255, 255));
		caloriesFav.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
		caloriesFav.setBounds(10, 425, 56, 14);
		panel_3.add(caloriesFav);
		
		JLabel proteinFav = new JLabel("");
		proteinFav.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
		proteinFav.setForeground(new Color(255, 255, 255));
		proteinFav.setBounds(91, 425, 46, 14);
		panel_3.add(proteinFav);
		
		JLabel sugarFav = new JLabel("");
		sugarFav.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
		sugarFav.setForeground(new Color(255, 255, 255));
		sugarFav.setBounds(170, 425, 46, 14);
		panel_3.add(sugarFav);
		
		JButton deleteFav = new JButton("Delete from favorites");
		deleteFav.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
		deleteFav.setBounds(10, 12, 167, 23);
		panel_3.add(deleteFav);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				calories.setText("");
				Object selectedItem = list.getSelectedValue();
				try {
				list.repaint();
				textArea.setText(recipe.viewIngredients(selectedItem, User.UserID));
				// textArea_1.setEnabled(false); // Disable the text area
				textArea.setEditable(false); // Make the text area read-only
				textArea.setLineWrap(true);
				textArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
				textArea.setWrapStyleWord(true);
				textArea.repaint();
				JScrollPane scrollIngredients = new JScrollPane(textArea);
				scrollIngredients.setBounds(392, 42, 236, 201);
				scrollIngredients.setVerticalScrollBarPolicy(
						ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				panel_3.add(scrollIngredients);
				textArea_1.setText(recipe.viewInstructions(selectedItem) + "\n\n\n" + "Time needed: "+ recipe.getRecipeTime(selectedItem, User.UserID));
				// textArea_2.setEnabled(false); // Disable the text area
				textArea_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
				textArea_1.setEditable(false); // Make the text area read-only
				textArea_1.setLineWrap(true);
				textArea_1.setWrapStyleWord(true);

				caloriesFav.setText(recipe.getCalories(User.UserID, selectedItem));
				proteinFav.setText(recipe.getProtein(User.UserID, selectedItem));
				sugarFav.setText(recipe.getSugar(User.UserID, selectedItem));
				JScrollPane scroll = new JScrollPane(textArea_1);
				scroll.setBounds(392, 270, 236, 206);
				scroll.setVerticalScrollBarPolicy(
						ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				panel_3.add(scroll);
							
				
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				deleteFav.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							recipe.deleteFavorite(selectedItem, User.UserID);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						User user = new User(User.UserID);
						dashboard.dispose();
						Dashboard reg;
						try {
							reg = new Dashboard(user);
							reg.dashboard.setVisible(true);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			}
		});

		JLabel lblNewLabel_4 = new JLabel("Ingredients");
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(256, 139, 114, 23);
		panel_3.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Recipe");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(289, 348, 81, 23);
		panel_3.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Calories");
		lblNewLabel_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_6.setBackground(new Color(255, 255, 255));
		lblNewLabel_6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		lblNewLabel_6.setBounds(10, 400, 64, 14);
		panel_3.add(lblNewLabel_6);
		
		JLabel lblNewLabel_19 = new JLabel("Sugar");
		lblNewLabel_19.setForeground(new Color(255, 255, 255));
		lblNewLabel_19.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		lblNewLabel_19.setBounds(170, 400, 46, 14);
		panel_3.add(lblNewLabel_19);
		
		JLabel lblNewLabel_20 = new JLabel("Protein");
		lblNewLabel_20.setForeground(new Color(255, 255, 255));
		lblNewLabel_20.setBackground(new Color(255, 255, 255));
		lblNewLabel_20.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		lblNewLabel_20.setBounds(91, 400, 56, 14);
		panel_3.add(lblNewLabel_20);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(76, 61, 128));
		tabbedPane.addTab("Delete Recipe", null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel_21 = new JLabel("Pick the recipe you want to delete:");
		lblNewLabel_21.setForeground(new Color(255, 255, 255));
		lblNewLabel_21.setBounds(166, 24, 306, 21);
		lblNewLabel_21.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		panel_4.add(lblNewLabel_21);
		
		JButton deleteRecipe = new JButton("Delete");
		deleteRecipe.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		deleteRecipe.setBounds(280, 449, 89, 23);
		panel_4.add(deleteRecipe);
		
		JComboBox comboBoxDelete = new JComboBox();

		//Apelam functia getRecipeNames pentru a adauga elemente in comboBox
		comboBoxDelete.setModel(recipe.getRecipeNames(User.UserID));
		
		comboBoxDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected item: " + comboBoxDelete.getSelectedItem());
				deleteRecipe.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							recipe.deleteRecipe(comboBoxDelete.getSelectedItem(), User.UserID);
							User user = new User(User.UserID);
							dashboard.dispose();
							Dashboard reg = new Dashboard(user);
							reg.dashboard.setVisible(true);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			}
		});
		comboBoxDelete.setBounds(166, 98, 306, 30);
		panel_4.add(comboBoxDelete);
		
		JLabel lblNewLabel_7 = new JLabel("");
		lblNewLabel_7.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\Screenshot_13_3_200x158.png"));
		lblNewLabel_7.setBounds(10, 170, 200, 158);
		panel_4.add(lblNewLabel_7);
		
		JLabel lblNewLabel_11 = new JLabel("New label");
		lblNewLabel_11.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\Screenshot_15_200x158.png"));
		lblNewLabel_11.setBounds(217, 170, 200, 158);
		panel_4.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("New label");
		lblNewLabel_12.setIcon(new ImageIcon("C:\\Users\\alexk\\Desktop\\Screenshot_16_200x158.png"));
		lblNewLabel_12.setBounds(424, 170, 200, 158);
		panel_4.add(lblNewLabel_12);
		
		
		
		addRecipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (ingredientNamesArray.size() != 0) {
						// INSERT recipeName, recipeTime, category, recipe, then select recipeID and
						// store it to add the ingredients
						// TODO : Modifica functia
						// Pune ingredientele intr-un array, dupa itereaza prin array si introdu in baza
						// de date ingredientele 1 cate 1, pe un rand diferit, pentru Recpe
						Recipe recipe = new Recipe(User.UserID, Name.getText(), Category.getText(), Recipe.getText(),
								(String) Time.getSelectedItem());
						int id = recipe.insertRecipeAndGetID(User.UserID, Name.getText(), Category.getText(),
								Recipe.getText(), (String) Time.getSelectedItem());
						if (recipe.verifyCategory(Category.getText())) {
							boolean categoryExists = false;
							boolean categoryExists2 = false;
							boolean nameExists = false;
							for (int i = 0; i < comboBox.getItemCount(); i++) {
								if (comboBox.getItemAt(i).equals(Category.getText())) {
									categoryExists = true;
									break;
								}
							}
							
							for (int i = 0; i < categoryFilter.getItemCount(); i++) {
								if (categoryFilter.getItemAt(i).equals(Category.getText())) {
									categoryExists2 = true;
									break;
								}
							}
							
							for (int i = 0; i < comboBoxDelete.getItemCount(); i++) {
								if (comboBoxDelete.getItemAt(i).equals(Name.getText())) {
									nameExists = true;
									break;
								}
							}

							if (!categoryExists) {
								comboBox.addItem(Category.getText());
							}
							if (!categoryExists2) {
								categoryFilter.addItem(Category.getText());
							}
							if(!nameExists) {
								comboBoxDelete.addItem(Name.getText());
							}
						}
						recipe.insertIngredients(User.UserID, id, ingredientNamesArray, weightArray);
						weightArray.clear();
						ingredientNamesArray.clear();
						model.setRowCount(0);
						Recipe.setText("");
						weight.setText("");
						Category.setText("");
						Name.setText("");
					} else
						JOptionPane.showMessageDialog(null, "The recipe cannot have 0 ingredients!");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}
}
