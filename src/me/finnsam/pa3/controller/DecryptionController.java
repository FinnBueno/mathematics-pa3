package me.finnsam.pa3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import me.finnsam.pa3.EncryptionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sam Joosten
 */
public class DecryptionController {

    private EncryptionManager EM = EncryptionManager.getInstance();
    private BigInteger d;
    private BigInteger n;

    @FXML
    private TextArea decryptedMessageTextField;
    @FXML
    private TextArea encodedMessageTextArea;
    @FXML
    private TextArea encryptedMessage;
    @FXML
    private Button step2Button;
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
        step2Button.setOnAction(event -> step2());
    }

    private void step1() {
        BigInteger e;
        BigInteger p = EM.getP();
        BigInteger q = EM.getQ();
        try {
            n = new BigInteger(decryptionInputN.getText());
            e = new BigInteger(decryptionInputE.getText());
        } catch (NumberFormatException error) {
            decryptionResultDisplay.setText("N must be an integer");
            return;
        }

        BigInteger f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        if (e.gcd(f).equals(BigInteger.ONE)) {
            d = e.modInverse(f);
            decryptionResultDisplay.setText(String.format("D is %s", d.toString()));
        } else {
            decryptionResultDisplay.setText("Wrong input for E, try a different number");
        }
    }

    private void step2() {
        String encryptedMessageString = encryptedMessage.getText();
        List<String> encryptedIntegers = Arrays.asList(encryptedMessageString.split("\\s*,\\s*"));
        int[] encodedArray = new int[encryptedIntegers.size()];
        StringBuilder decryptedString = new StringBuilder();
        for (int i = 0; i < encryptedIntegers.size(); i++) {
            BigInteger encryptedChar = new BigInteger(encryptedIntegers.get(i));
            encodedArray[i] = decrypt(encryptedChar).intValue();
            decryptedString.append(encodedToString(decrypt(encryptedChar)));
        }

        StringBuilder encodedMessage = new StringBuilder();
        for (int i: encodedArray) {
            encodedMessage.append(i).append(",");
        }
        encodedMessageTextArea.setText(encodedMessage.toString());
        decryptedMessageTextField.setText(decryptedString.toString());

    }

    private BigInteger decrypt(BigInteger message) {
        return message.modPow(d, n);
    }

    private char encodedToString(BigInteger message) {
        String encodedString = message.toString();
        return (char) Integer.parseInt(encodedString);
    }
}
