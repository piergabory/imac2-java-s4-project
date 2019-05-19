package fr.upem.capcha.images.vehicules;

import java.net.URISyntaxException;
import java.nio.file.Path;

import fr.upem.capcha.images.Category;

//represents a category of image
public class Vehicule extends Category {
  static final Path getClassDirectoryPath(){ //TODO: find less shlag way of doing it
    try { 
      return Path.of(Vehicule.class.getResource("Vehicule.class").toURI()).getParent();
    } catch(URISyntaxException error) {
      return null;
    }
  }
  
  Vehicule() {
     super(getClassDirectoryPath());
  }
}
