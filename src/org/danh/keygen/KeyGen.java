package org.danh.keygen;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGen {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();
		System.out.println(new BigInteger(1, key.getEncoded()).toString(16));
	}
}
