/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.ui;
import java.net.URL;
import java.util.List;

/**
 * Capcha View Delegate Interface
 * 
 * Provides the Capcha View the list of images to be displayed.
 * Provides Submit and Cancel Action handlers.
 */
interface CapchaViewDelegate {
  // Action Handlers
  public void capchaViewSubmitPhotosAction(List<URL> photos);
  public void capchaViewAbortAction();

  // Photo Provider
  public List<URL> capchaViewDisplayedPhotosProvider();
}