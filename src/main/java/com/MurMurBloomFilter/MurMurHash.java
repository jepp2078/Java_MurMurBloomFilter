package com.MurMurBloomFilter;

import com.sangupta.murmur.*;

public class MurMurHash {

	int[] Hash(int hashCount, int max, String value){
		int[] output = new int[max];
		int hash1 = (int) Murmur1.hash(value.getBytes(), value.length(), 0);
		int hash2 = (int) Murmur1.hash(value.getBytes(), value.length(), hash1);
		for (int i = 0; i < hashCount; i++) {
			output[i] = Math.abs((hash1 + i * hash2) % max);
		}
		return output;
	}
}

