package fr.upem.capcha.images;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import fr.upem.capcha.images.Category;

//model class: sends a batch of correct and incorrect images to the controller

public class ImagesProvider {
	
	private final Category allImages; //all existing images
	private Category targetCategory; //the category the user has to select the images of
	

	public ImagesProvider() {
		allImages = Category.getAll();
		targetCategory = allImages.getRandomSubCategory();
	}
	
		//returns a list of correct and incorrect images
	public List<URL> getPhotoTestBatch(int size, int maxTargetPhotosCount) {
		
		// TODO throw err if size < maxTarget
		
		var randomizer = new Random();
		randomizer.setSeed(System.currentTimeMillis());
		
		var batch = new ArrayList<URL>(size);
		var targetCount = randomizer.nextInt(maxTargetPhotosCount - 1) + 1;
		
		batch.addAll(targetCategory.getRandomPhotosURL(targetCount));	
		
		while(batch.size() <= size) {
			var image = allImages.getRandomPhotoURL();
			if (targetCategory.isPhotoCorrect(image)) continue;
			batch.add(image);
		}
		
		return batch;
	}
		//checks if all the images in the selection are correct
	public boolean isSelectionCorrect(List<URL> selection, Consumer<String> ifFailed) {
		for (URL image: selection) {
			if(!targetCategory.isPhotoCorrect(image)) {
				targetCategory = targetCategory.getRandomSubCategory();
				ifFailed.accept("Au moins une photo était incorrecte.");
				return false;
			}
		}
		return true;
	}
	
	public String currentTargetName() {
		return targetCategory.name();
	}
}
