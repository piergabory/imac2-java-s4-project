/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.ui;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import fr.upem.capcha.images.ImagesProvider;

/**
 * Capcha View Controller, controls and delegates to the Capcha view. Handles communications between the Model and the view
 */
public class CapchaViewController implements CapchaViewDelegate {

  // View User Messages 
  static private final String PHOTO_TYPE_INDICATOR_MESSAGE = "Cliquez sur les images contenant des ";
  static private final String BAD_SELECTION_DIALOG_MESSAGE = "Au moins une photo était incorrecte.";
  static private final String PHOTO_IS_MISSING_DIALOG_MESSAGE = "Vous avez oublié une photo";
  static private final String SUCCESS_DIALOG_MESSAGE = "Bravo!";

  // model view
  private final ImagesProvider provider;
  private final CapchaView capchaView = new CapchaView(this);

  // capcha completion callbacks
  private Runnable successCallback;
  private Runnable cancelCallback;

  /**
   * Controller constructor.
   * Sets parameters on the view.
   * Displays the view when ready
   * @param imageLibraryPathName. Path the the photo library: source for the capcha. 
   * @throws IOException when libraryPathName is unreachable
   */
  public CapchaViewController(String imageLibraryPathName) throws IOException {
    provider = new ImagesProvider(imageLibraryPathName);
    
    setPhotoKindIndicatorMessage();
    capchaView.updatePhotos();
    capchaView.setVisible(true);
  }

  /**
   * Requests the model for a new photo batch to challenge the user.
   * <p> 
   * Required implementation from the Capcha View Delegate
   * @return List of random photos
   */
  @Override
  public List<URL> capchaViewDisplayedPhotosProvider() {
    return provider.getPhotoTestBatch();
  }

  /**
   * Handles the submit action of capcha view
   * <p> 
   * Required implementation from the Capcha View Delegate
   * Displays an alert dialog showing the appropriate message
   * Destroys the view and calls the success callback
   * @param photos list of the selected photos in the capcha view.
   */
  @Override
  public void capchaViewSubmitPhotosAction(List<URL> photos) {
    String message = BAD_SELECTION_DIALOG_MESSAGE;

    switch (provider.isSelectionCorrect(photos)) {
      case MISSING:
      message = PHOTO_IS_MISSING_DIALOG_MESSAGE;

      case INVALID:
      setPhotoKindIndicatorMessage();
      capchaView.updatePhotos();
      break;

      case CORRECT:
      message = SUCCESS_DIALOG_MESSAGE;
      capchaView.dispose();
      successCallback.run();
      break;
    }
    JOptionPane.showMessageDialog(null, message);
  }

   /**
   * Handles the cancel button action of capcha view
   * <p> 
   * Required implementation from the Capcha View Delegate
   * Destroys the view and calls the cancel callback.
   */
  @Override
  public void capchaViewAbortAction() {
    capchaView.dispose();
    cancelCallback.run();
  }

  /**
   * Success Callback Setter (Builder)
   * @param onSuccess success callback called on the user completion of the capcha
   * @return the viewController as Builder functions do.
   */
  public CapchaViewController then(Runnable onSuccess) {
    successCallback = onSuccess;
    return this;
  }

  /**
   * Cancel Callback Setter (Builder)
   * @param onCancel cancel callback called on the user completion of the capcha
   * @return the viewController as Builder functions do.
   */
  public CapchaViewController cancel(Runnable onCancel) {
    cancelCallback = onCancel;
    return this;
  }

  /**
   * Updates the user message label to show the current expected photo type.
   */
  private void setPhotoKindIndicatorMessage() {
    capchaView.setMessage(PHOTO_TYPE_INDICATOR_MESSAGE + provider.currentTargetName());
  }
}
