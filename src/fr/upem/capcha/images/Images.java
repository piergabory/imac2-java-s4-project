package fr.upem.capcha.images;

import java.util.List;

//interface : represents an image within the filesystem
import java.net.URL;

public interface Images {
  public List<URL> getPhotos();
  public List<URL> getRandomPhotosURL(int count);
  public URL getRandomPhotoURL();
  public boolean isPhotoCorrect(URL photo);
}