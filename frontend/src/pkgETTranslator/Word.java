package pkgETTranslator;

import java.util.ArrayList;

public class Word {
	private String wordName;
	private ArrayList<String> wordPhotos;
	private String chosenPhoto;

	// constructor
	public Word(String word) {
		this.wordName = word;
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

	public String getPhoto(int index) {
		return wordPhotos.get(index);
	}

	public ArrayList<String> getAllPhotos() {
		return wordPhotos;
	}
	
	public String getChosenPhoto() {
		return chosenPhoto;
	}
}