package application.controller;

import java.io.File;
import java.util.Scanner;

import application.Main;
import application.model.Logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Event handler for View.fxml
 * @author Patrick Armstrong
 *
 */
public class ViewController {

	@FXML private Label textL;
	@FXML private ListView<String> historyLV;
	@FXML private TextField loadTF;
	
	private Logic brain;
	
	@FXML
	/**
	 * Runs when view is created. Creates the brain object
	 */
	public void initialize() {
		brain = new Logic();
	}
	
	/**
	 * adds a char to end of the brain text
	 * @param ch
	 */
	private void appendText(String ch) {
		brain.appendText(ch);
		textL.setText(brain.getText());
	}
	
	@FXML
	/**
	 * handles the zero button
	 * @param arg0
	 */
	public void handleZero(ActionEvent arg0) {
		appendText("0");
	}
	
	@FXML
	/**
	 * handles the one button
	 * @param arg0
	 */
	public void handleOne(ActionEvent arg0) {
		appendText("1");
	}
	
	@FXML
	/**
	 * handles the two button
	 * @param arg0
	 */
	public void handleTwo(ActionEvent arg0) {
		appendText("2");
	}
	
	@FXML
	/**
	 * handles the three button
	 * @param arg0
	 */
	public void handleThree(ActionEvent arg0) {
		appendText("3");
	}
	
	@FXML
	/**
	 * handles the four button
	 * @param arg0
	 */
	public void handleFour(ActionEvent arg0) {
		appendText("4");
	}
	
	@FXML
	/**
	 * handles the five button
	 * @param arg0
	 */
	public void handleFive(ActionEvent arg0) {
		appendText("5");
	}
	
	@FXML
	/**
	 * handles the six button
	 * @param arg0
	 */
	public void handleSix(ActionEvent arg0) {
		appendText("6");
	}
	
	@FXML
	/**
	 * handles the seven button
	 * @param arg0
	 */
	public void handleSeven(ActionEvent arg0) {
		appendText("7");
	}
	
	@FXML
	/**
	 * handles the eight button
	 * @param arg0
	 */
	public void handleEight(ActionEvent arg0) {
		appendText("8");
	}
	
	@FXML
	/**
	 * handles the nine button
	 * @param arg0
	 */
	public void handleNine(ActionEvent arg0) {
		appendText("9");
	}
	
	@FXML
	/**
	 * handles the left parentheses button
	 * @param arg0
	 */
	public void handleLeftParen(ActionEvent arg0) {
		appendText("(");
	}
	
	@FXML
	/**
	 * handles the right parentheses button
	 * @param arg0
	 */
	public void handleRightParen(ActionEvent arg0) {
		appendText(")");
	}
	
	@FXML
	/**
	 * handles the decimal point button
	 * @param arg0
	 */
	public void handlePeriod(ActionEvent arg0) {
		appendText(".");
	}
	
	@FXML
	/**
	 * handles the clear entry button
	 * @param arg0
	 */
	public void handleClearEntry(ActionEvent arg0) {
		brain.setText("");
		textL.setText(brain.getText());
	}
	
	@FXML
	/**
	 * handles the divide button
	 * @param arg0
	 */
	public void handleDivide(ActionEvent arg0) {
		appendText("/");
	}
	
	@FXML
	/**
	 * handles the multiply button
	 * @param arg0
	 */
	public void handleMultiply(ActionEvent arg0) {
		appendText("*");
	}
	
	@FXML
	/**
	 * handles the add button
	 * @param arg0
	 */
	public void handleAdd(ActionEvent arg0) {
		appendText("+");
	}
	
	@FXML
	/**
	 * handles the minus button
	 * @param arg0
	 */
	public void handleMinus(ActionEvent arg0) {
		appendText("-");
	}
	
	@FXML
	/**
	 * handles the equals button
	 * @param arg0
	 */
	public void handleEquals(ActionEvent arg0) {
		textL.setText(brain.parseText());
		historyLV.getItems().add(brain.getText());
		historyLV.getItems().add(" = " + brain.getResult());
		brain.setText("");
	}
	
	@FXML
	/**
	 * handles the back button
	 * @param arg0
	 */
	public void handleBack(ActionEvent arg0) {
		String str = brain.getText();
		if (str.length() > 0) {
			brain.setText(str.substring(0, str.length() - 1));
			textL.setText(brain.getText());
		}
	}
	
	@FXML
	/**
	 * handles the load button.
	 * When the button is clicked, the method gets the directory of the jar file
	 * and appends the string from the text field on to it. It then opens the text 
	 * file and read the text as a single string. It then sets the text of the brain 
	 * object to that sting.
	 * @param arg0
	 */
	public void handleLoad(ActionEvent arg0) {
		try {
			String jarDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			String fileName = jarDir.replace("Calculator.jar", loadTF.getText());
			Scanner scan = new Scanner(new File(fileName));
			String line = "";
			while(scan.hasNext())
				line = line + scan.nextLine();
			scan.close();
			brain.setText(line);
			textL.setText(brain.getText());
		} catch (Exception e) {
			brain.setText("Could not load file '" + loadTF.getText() + "'");
			textL.setText(brain.getText());
		}
	}
}
