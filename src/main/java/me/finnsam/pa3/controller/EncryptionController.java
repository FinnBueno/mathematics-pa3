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

	private static final EncryptionManager EM = EncryptionManager.getInstance();

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

	// encryption variables
	private BigInteger p, q, n, publicKey;

	public void initialize() {
		step1Button.setOnAction(event -> onStep1());
		step2Button.setOnAction(event -> onStep2());
		step3Button.setOnAction(event -> onStep3());
	}

	private void onStep1() {
		// start calculating P and Q.
		try {
			this.n = new BigInteger(inputN.getText());
		} catch (NumberFormatException e) {
			step1Result.setText("N must be an integer");
			return;
		}

		long startTime = System.currentTimeMillis();
		// we start with dividing by 2, and then go up everytime we fail to the next prime number.
		// As long as P remains less than or equal to half of N, we still have a chance to find one.
		BigInteger[] pAndQ = EM.findQAndPForN(n);
		if (pAndQ == null) {
			step1Result.setText("No outcome for N could be found.");
		} else {
			this.p = pAndQ[0];
			this.q = pAndQ[1];
			long endTime = System.currentTimeMillis();
			step1Result.setText(String.format("P is %s\nQ is %s\nAmount of time busy finding p and q: %d ms", p.toString(), q.toString(), endTime - startTime));
		}
	}

	private void onStep2() {
		try {
			this.publicKey = EM.generateE(p, q);
			step2Result.setText(String.format("e is %s", publicKey.toString()));
		} catch (NullPointerException e) {
			step2Result.setText("P and Q must first be set!");
		}
	}

	private void onStep3() {
		String encrypted = EM.encrypt(step3Input.getText(), publicKey, n);
		step3Result.setText(encrypted);
	}
}
