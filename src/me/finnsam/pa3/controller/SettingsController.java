package me.finnsam.pa3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigInteger;

/**
 * @author Finn Bon
 */
public class SettingsController {

	private static final BigInteger FIRST_PRIME = new BigInteger("2");

	@FXML
	private Button startButton;
	@FXML
	private TextField inputN;
	@FXML
	private Label resultDisplay;

	public void initialize() {
		startButton.setOnAction(event -> onStep1());
	}

	private void onStep1() {
		// start calculating P and Q.
		BigInteger n;
		try {
			n = new BigInteger(inputN.getText());
		} catch (NumberFormatException e) {
			resultDisplay.setText("N must be an integer");
			return;
		}

		long startTime = System.currentTimeMillis();
		// we start with dividing by 2, and then go up everytime we fail to the next prime number.
		// As long as P remains less than or equal to half of N, we still have a chance to find one.
		for (BigInteger p = new BigInteger(FIRST_PRIME.toString());
			 p.compareTo(n.divide(FIRST_PRIME)) <= 0;
			 p = p.nextProbablePrime()) {
			// calculate the remainder of the division q = n / p. If there is no division, we have found a proper q.
			BigInteger remainder = n.mod(p);
			if (remainder.equals(BigInteger.ZERO)) {
				// we've found a number that's dividable
				// if n = p * q, then q = n / p.
				BigInteger q = n.divide(p);
				long endTime = System.currentTimeMillis();
				resultDisplay.setText(String.format("P is %s\nQ is %s\nAmount of time busy finding p and q: %d ms", p.toString(), q.toString(), endTime - startTime));
				return;
			}
			// move onto next prime number
			p = p.nextProbablePrime();
		}
		resultDisplay.setText("No outcome for N could be found.");
	}
}
