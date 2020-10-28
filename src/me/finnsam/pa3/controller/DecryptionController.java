package me.finnsam.pa3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigInteger;

/**
 * @author Sam Joosten
 */
public class DecryptionController {

    @FXML
    private TextField decryptionInputE;
    @FXML
    private Label decryptionResultDisplay;
    @FXML
    private Button step1Button;
    @FXML
    private TextField decryptionInputN;

    public void initialize() {
        step1Button.setOnAction(event -> step1());
    }

    private void step1() {
        BigInteger n;
        BigInteger e;
        BigInteger p = new BigInteger("17");
        BigInteger q = new BigInteger("19");
        try {
            n = new BigInteger(decryptionInputN.getText());
            e = new BigInteger(decryptionInputE.getText());
        } catch (NumberFormatException error) {
            decryptionResultDisplay.setText("N must be an integer");
            return;
        }

        BigInteger d = e.modInverse(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        decryptionResultDisplay.setText(String.format("D is %s", d.toString()));
    }
}
