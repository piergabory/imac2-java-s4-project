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
	private int targetCount;
	

	public ImagesProvider() {
		allImages = Category.getAll();
		targetCategory = allImages.getRandomSubCategory();
	}
	
	//returns a list of correct and incorrect images
	public List<URL> getPhotoTestBatch(int size, int maxTargetPhotosCount) {
		assert size < maxTargetPhotosCount;
		
		var randomizer = new Random();
		randomizer.setSeed(System.currentTimeMillis());
		
		var batch = new ArrayList<URL>(size);
		targetCount = randomizer.nextInt(maxTargetPhotosCount - 1) + 1;
		
		batch.addAll(targetCategory.getRandomPhotosURL(targetCount));	
		
		while(batch.size() <= size) {
			var image = allImages.getRandomPhotoURL();
			if (targetCategory.isPhotoCorrect(image)) continue;
			batch.add(image);
		}
		
		return batch;
	}
	//checks if all the images in the selection are correct
	public SelectionValidation isSelectionCorrect(List<URL> selection) {
		if (selection.size() < targetCount) return SelectionValidation.MISSING;
		if (selection.size() > targetCount) return SelectionValidation.INVALID;
		
		if (selection.stream().allMatch(targetCategory::isPhotoCorrect))
			return SelectionValidation.CORRECT;

		targetCategory = targetCategory.getRandomSubCategory();
		return SelectionValidation.INVALID;
	}
	
	public String currentTargetName() {
		return targetCategory.name();
	}
}
