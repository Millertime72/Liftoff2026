package pkgETTranslator;

import java.util.ArrayList;

public class Word {
	private String wordName;
	private int wordType;
	private ArrayList<String> wordPhotos;
	private String chosenPhoto;

	// constructor
	public Word(String word, Integer wordType) {
		this.wordName = word;
		this.wordType = wordType;
		this.wordPhotos = new ArrayList<>();
	}

	public String getWord() {
		return wordName;
	}
	
	public void setChosenPhoto(String chosen) {
		chosenPhoto = chosen;
	}

	public void append(String newPhoto) {
		wordPhotos.add(newPhoto);
	}
	
	public void setWordType(int type) {
		this.wordType = type;
	}
	
	public void setTimeTensePhoto(String timeTense) {
		chosenPhoto = timeTense;
	}

	public String getPhoto(int index) {
		return wordPhotos.get(index);
	}

	public ArrayList<String> getAllPhotos() {
		return wordPhotos;
	}
	
	public String getChosenPhoto() {
		return chosenPhoto;
	}
	
	public int getWordType() {
		return this.wordType;
	}
}