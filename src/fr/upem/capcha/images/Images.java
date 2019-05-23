/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.images;
import java.util.List;
import java.net.URL;

/**
 * Data structure providing random photos from a set.
 * <p>
 * Known implementation: Category
 */
public interface Images {
  public List<URL> getPhotos();
  public List<URL> getRandomPhotosURL(int count);
  public URL getRandomPhotoURL();
  public boolean isPhotoCorrect(URL photo);
}