package me.finnsam.pa3;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Finn Bon
 */
public class EncryptionManager {

	private static EncryptionManager EM = new EncryptionManager();

	public static EncryptionManager getInstance() {
		return EM;
	}

	private BigInteger p, q, e, n, phi;

	public EncryptionManager setP(BigInteger p) {
		this.p = p;
		if (q != null) {
			generatePhiAndN();
		}
		return this;
	}

	public EncryptionManager setQ(BigInteger q) {
		this.q = q;
		if (p != null) {
			generatePhiAndN();
		}
		return this;
	}

	public BigInteger getP() {
		return this.p;
	}

	public BigInteger getQ() {
		return this.q;
	}

	private void generatePhiAndN() {
		n = q.multiply(p);
		phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
	}

	/**
	 * Generate and set E, which must be a number above 3 and lower than P*Q.
	 */
	public EncryptionManager generateE() {
		if (p == null || q == null) {
			throw new NullPointerException("P and Q must be set before generating E");
		}
		try {
			int length = phi.bitLength() - 1;
			SecureRandom secureRandom = SecureRandom.getInstanceStrong();
			BigInteger e = BigInteger.probablePrime(length, secureRandom);
			// if the greatest common divider is not one, they are not coprime!!!
			while (!phi.gcd(e).equals(BigInteger.ONE)) {
				e = BigInteger.probablePrime(length, secureRandom);
			}
			this.e = e;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public BigInteger getE() {
		return e;
	}

	public String encrypt(String text) {
		int[] encodedArray = new int[text.length()];
		StringBuilder message = new StringBuilder(text);
		for (int i = 0; i < message.length(); i++) {
			encodedArray[i] = message.codePointAt(i);
		}

		System.out.println(Arrays.toString(encodedArray));

		StringBuilder encryptedMessage = new StringBuilder();
		for (int i : encodedArray) {
			int c = mpMod(i, e.intValue(), n.intValue());
			encryptedMessage.append(c).append(',');
		}
		return encryptedMessage.toString();
	}

	private int mpMod(int base, int exponent, int modulus) {
		if (base < 1 || exponent < 0 | modulus < 1) {
			throw new IllegalArgumentException("Base and Modulus must be greater than 1, and exponent must be greater than or equal to 0.");
		}
		int result = 1;
		while (exponent > 0) {
			boolean isExponentUneven = exponent % 2 == 1;
			if (isExponentUneven) {
				result = (result * base) % modulus;
			}
			base = (base * base) % modulus;
			exponent = (int) Math.floor(exponent / 2.0);
		}
		return result;
	}
}
