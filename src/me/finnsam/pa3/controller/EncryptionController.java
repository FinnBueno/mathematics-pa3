package me.finnsam.pa3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import me.finnsam.pa3.EncryptionManager;

import java.math.BigInteger;

/**
 * @author Finn Bon
 */
public class EncryptionController {

	private static final BigInteger FIRST_PRIME = new BigInteger("2");

	@FXML
	private Button step1Button;
	@FXML
	private TextField inputN;
	@FXML
	private Label step1Result;

	@FXML
	private Button step2Button;
	@FXML
	private Label step2Result;

	@FXML
	private Button step3Button;
	@FXML
	private TextArea step3Result;
	@FXML
	private TextArea step3Input;

	public void initialize() {
		step1Button.setOnAction(event -> onStep1());
		step2Button.setOnAction(event -> onStep2());
		step3Button.setOnAction(event -> onStep3());
	}

	private void onStep1() {
		// start calculating P and Q.
		BigInteger n;
		try {
			n = new BigInteger(inputN.getText());
		} catch (NumberFormatException e) {
			step1Result.setText("N must be an integer");
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
				step1Result.setText(String.format("P is %s\nQ is %s\nAmount of time busy finding p and q: %d ms", p.toString(), q.toString(), endTime - startTime));
				EncryptionManager.getInstance().setP(p).setQ(q);
				return;
			}
			// move onto next prime number
			p = p.nextProbablePrime();
		}
		step1Result.setText("No outcome for N could be found.");
	}

	private void onStep2() {
		try {
			BigInteger e = EncryptionManager.getInstance().generateE().getE();
			step2Result.setText(String.format("e is %s", e.toString()));
		} catch (NullPointerException e) {
			step2Result.setText("P and Q must first be set!");
		}
	}

	private void onStep3() {
		String encrypted = EncryptionManager.getInstance().encrypt(step3Input.getText());
		step3Result.setText(encrypted);
	}
}
