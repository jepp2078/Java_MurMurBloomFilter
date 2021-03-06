package com.MurMurBloomFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloomfilter")
public class HashController {

    private static final int max = 200000;
    private static final int hashCount = 5;
    private static BitSet bit = new BitSet(max);
    private static ArrayList<String> wordsInBitSet = new ArrayList<String>();
    
    public class HashOut{
    	private String word;
    	private ArrayList<String> wordsInBitSet = new ArrayList<String>();
    	
    	public HashOut(ArrayList<String> listIn, String word){
    		this.setWordsInBitSet(listIn);
    		this.setWord(word);
    	}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public ArrayList<String> getWordsInBitSet() {
			return wordsInBitSet;
		}

		public void setWordsInBitSet(ArrayList<String> wordsInBitSet) {
			this.wordsInBitSet = wordsInBitSet;
		}
    }
    
    public class CheckOut{
    	private Boolean found;
    	private ArrayList<String> wordsInBitSet = new ArrayList<String>();
    	
    	public CheckOut(ArrayList<String> listIn, Boolean found){
    		this.setWordsInBitSet(listIn);
    		this.setFound(found);
    	}

		public ArrayList<String> getWordsInBitSet() {
			return wordsInBitSet;
		}

		public void setWordsInBitSet(ArrayList<String> wordsInBitSet) {
			this.wordsInBitSet = wordsInBitSet;
		}

		public Boolean getFound() {
			return found;
		}

		public void setFound(Boolean found) {
			this.found = found;
		}
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HashOut> hash(@RequestParam("value") String value) {
    	int[] hash = new MurMurHash().Hash(hashCount, max, value);
    	for (int l : hash) {
			bit.set(l);
		}
    	wordsInBitSet.add(value);
    	HashOut output = new HashOut(wordsInBitSet, value);
        return new ResponseEntity<HashOut>(output, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{value}", method = RequestMethod.GET)
    public ResponseEntity<CheckOut> checkHash(@PathVariable String value) {
    	for (int bitInHash : new MurMurHash().Hash(hashCount, max, value)) {
			if(!bit.get(bitInHash)){
				return new ResponseEntity<CheckOut>(new CheckOut(wordsInBitSet, false), HttpStatus.OK);
			}
		}
    	return new ResponseEntity<CheckOut>(new CheckOut(wordsInBitSet, true), HttpStatus.OK);
    }
    
    @RequestMapping("/testing/addWords")
    public void test() {
    	BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("/Users/Udvikler/Desktop/words.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				int[] hash = new MurMurHash().Hash(hashCount, max, sCurrentLine);
		    	for (int l : hash) {
					bit.set(l);
				}
				wordsInBitSet.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
}
