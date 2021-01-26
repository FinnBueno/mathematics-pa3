package me.finnsam.pa3;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Finn Bon
 */
public class EncryptionManager {

	private static final BigInteger FIRST_PRIME = new BigInteger("2");

	private static EncryptionManager EM = new EncryptionManager();

	public static EncryptionManager getInstance() {
		return EM;
	}

	public BigInteger[] findQAndPForN(BigInteger n) {
		for (BigInteger p = new BigInteger(FIRST_PRIME.toString());
			 p.compareTo(n.divide(FIRST_PRIME)) <= 0;
			 p = p.nextProbablePrime()) {
			// calculate the remainder of the division q = n / p. If there is no division, we have found a proper q.
			BigInteger remainder = n.mod(p);
			if (remainder.equals(BigInteger.ZERO)) {
				// we've found a number that's dividable
				// if n = p * q, then q = n / p.
				BigInteger q = n.divide(p);
				return new BigInteger[] { p, q };
			}
			// move onto next prime number
			p = p.nextProbablePrime();
		}
		return null;
	}

	public BigInteger generatePhi(BigInteger p, BigInteger q) {
		return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	}

	/**
	 * Generate and set E, which must be a number above 3 and lower than P*Q.
	 */
	public BigInteger generateE(BigInteger p, BigInteger q) {
		BigInteger phi = generatePhi(p, q);
		try {
			int length = phi.bitLength() - 1;
			SecureRandom secureRandom = SecureRandom.getInstanceStrong();
			BigInteger e = BigInteger.probablePrime(length, secureRandom);
			// if the greatest common divider is not one, they are not coprime!!!
			while (!phi.gcd(e).equals(BigInteger.ONE)) {
				e = BigInteger.probablePrime(length, secureRandom);
			}
			return BigInteger.valueOf(16381);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public BigInteger generateD(BigInteger p, BigInteger q, BigInteger e) {
		BigInteger phi = EM.generatePhi(p, q);
		if (e.gcd(phi).equals(BigInteger.ONE)) {
			return e.modInverse(phi);
		}
		return null;
	}

	public String encrypt(String text, BigInteger publicKey, BigInteger n) {
		int[] encodedArray = new int[text.length()];
		StringBuilder message = new StringBuilder(text);
		for (int i = 0; i < message.length(); i++) {
			encodedArray[i] = message.codePointAt(i);
		}

		return String.join(
			",",
			Arrays
				.stream(encodedArray)
				.map(i -> mpMod(i, publicKey.intValue(), n.intValue()))
				.mapToObj(String::valueOf)
				.toArray(String[]::new)
		);
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
