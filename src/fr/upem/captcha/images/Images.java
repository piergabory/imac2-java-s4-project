package fr.upem.captcha.images;

import java.util.List;
import java.net.URL;

public interface Images {
  public List<URL> getPhotos();
  public List<URL> getRandomPhotosURL(int count);
  public URL getRandomPhotoURL();
  public boolean isPhotoCorrect(URL photo);
}