/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.images;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import fr.upem.capcha.images.Category;

/**
 * Provides batches of correct and incorrect images to the controller
 */
public class ImagesProvider {

	/**
	 * Validation states of the user photo selection
	 * <p>
	 * CORRECT: The user has selectioned all the expected images.
	 * INVALID: The user has selectioned an image that wasn't a member of the target category.
	 * MISSING: The user forgot to select at least one correct image from the displayed photos.
	 */
	public enum SelectionValidation {
		CORRECT,
		INVALID, 
		MISSING
	}
	
	/**
	 * Image Provider Options
	 */
	private static final int MIN_TARGET_RESULTS = 2;					// Minimum amount of photo the user has to select, must be less than the max
	private static final int MAX_TARGET_RESULTS = 4;					// Maximum amount of photo the user has to select, must be more than the min
	private static final int DEFAULT_CAPCHA_SIZE = 9;					// Default number of photos in the capcha
	private static final int MAXIMUM_CAPCHA_SIZE = 21;				// Maximum number of photos allowed in the capchas
	private static final int DIFFICULTY_CAPCHA_SIZE_STEP = 9; // Number of photos added on difficulty increase

	private final Category allImages; 							// Global set of images
	private Category targetCategory; 								// Target set of images, the user selection should be included in this set for success.
	private int capchaSize = DEFAULT_CAPCHA_SIZE;		// Number of photos to be provided to the capcha
	private int targetCount;												// Expected number of images returned by the user.

	/**
	 * Image provider constructor
	 * Scans through the asset directory for all the photos.
	 * Selects a random subcategory for the target set.
	 * @param imageLibraryPathName The string path sent to the Category constructor
	 * @throws IOException from the category constructor if the path is unreachable
	 */
	public ImagesProvider(String imageLibraryPathName) throws IOException {
		allImages = new Category(imageLibraryPathName);
		targetCategory = allImages.getRandomSubCategory();
	}
	
	/**
	 * Get a random set of photos containing a some photos from the target Set.
	 * <p>
	 * Size of the target set defined by the class constants
	 * @return list of random photos
	 */
	public List<URL> getPhotoTestBatch() {
		var randomizer = new Random();
		randomizer.setSeed(System.currentTimeMillis());
		
		var batch = new ArrayList<URL>(capchaSize);

		// get X random photos from the target set
		targetCount = randomizer.nextInt(MAX_TARGET_RESULTS - MIN_TARGET_RESULTS) + MIN_TARGET_RESULTS;
		batch.addAll(targetCategory.getRandomPhotosURL(targetCount));	
		
		// fill the set with random photos not included in the target set.
		while(batch.size() < capchaSize) {
			var image = allImages.getRandomPhotoURL();
			if (targetCategory.isPhotoCorrect(image)) continue;
			batch.add(randomizer.nextInt(batch.size()), image);
		}
		
		return batch;
	}
	
	/**
	 * checks if all the images in the selection are correct
	 * @param selection send from the user
	 * @return selection state MISSING, INVALID or CORRECT
	 */
	public SelectionValidation isSelectionCorrect(List<URL> selection) {
		SelectionValidation state;

		// the selection is contained in the target set
		if (selection.stream().allMatch(targetCategory::isPhotoCorrect)) {
			// the set has missing cards
			 state = (selection.size() < targetCount) ? SelectionValidation.MISSING : SelectionValidation.CORRECT;
		} 
		
		// the selection contains cards that are not member of the target set.
		else {
			state = SelectionValidation.INVALID;
		}

		if (state != SelectionValidation.CORRECT) {
			increaseDifficulty();
		}
	
		return state;
	}

	/**
	 * Increase the Difficulty
	 * <p>
	 * Changes the target set to a more specialized topic.
	 * OR
	 * Increases the capcha size
	 */
	private void increaseDifficulty() {
		if (targetCategory.hasSubcategories()) {
			targetCategory = targetCategory.getRandomSubCategory();
		} else {
			capchaSize += DIFFICULTY_CAPCHA_SIZE_STEP;
			capchaSize %= MAXIMUM_CAPCHA_SIZE;
			targetCategory = allImages.getRandomSubCategory();
		} 
	}

	// getter
	public String currentTargetName() {
		return targetCategory.name();
	}
}
