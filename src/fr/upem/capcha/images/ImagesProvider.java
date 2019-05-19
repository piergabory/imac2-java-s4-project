package fr.upem.capcha.images;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.upem.capcha.images.Category;

//model class: sends a batch of correct and incorrect images to the controller

public class ImagesProvider {

	public enum SelectionValidation {
		CORRECT, INVALID, MISSING
	}
	
	private final Category allImages; //all existing images
	private Category targetCategory; //the category the user has to select the images of
	private int capchaSize = 9;
	private int targetCount;
	

	public ImagesProvider() {
		allImages = Category.getAll();
		targetCategory = allImages.getRandomSubCategory();
	}
	
	//returns a list of correct and incorrect images
	public List<URL> getPhotoTestBatch() {

		var randomizer = new Random();
		randomizer.setSeed(System.currentTimeMillis());
		
		var batch = new ArrayList<URL>(capchaSize);
		targetCount = randomizer.nextInt(capchaSize/3 - 1) + 1;
		
		batch.addAll(targetCategory.getRandomPhotosURL(targetCount));	
		
		while(batch.size() < capchaSize) {
			var image = allImages.getRandomPhotoURL();
			if (targetCategory.isPhotoCorrect(image)) continue;
			batch.add(randomizer.nextInt(batch.size()), image);
		}
		
		return batch;
	}
	//checks if all the images in the selection are correct
	public SelectionValidation isSelectionCorrect(List<URL> selection) {
		SelectionValidation state;

		if (selection.stream().allMatch(targetCategory::isPhotoCorrect)) {
			if (selection.size() == targetCount) state = SelectionValidation.CORRECT;
			else if (selection.size() < targetCount) state = SelectionValidation.MISSING;
			else state = SelectionValidation.INVALID;
		} else {
			state = SelectionValidation.INVALID;
		}

		if (state != SelectionValidation.CORRECT) {
			increaseDifficulty();
		}
	
		return state;
	}
	
	public String currentTargetName() {
		return targetCategory.name();
	}

	private void increaseDifficulty() {
		if (targetCategory.hasSubcategories()) {
			targetCategory = targetCategory.getRandomSubCategory();
		} else {
			capchaSize += 3;
			targetCategory = allImages.getRandomSubCategory();
		} 

		if (capchaSize > 21) {
			capchaSize = 9;
		}
	}
}
