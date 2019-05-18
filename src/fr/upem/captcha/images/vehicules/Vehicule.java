package fr.upem.captcha.images.vehicules;

import java.net.URISyntaxException;
import java.nio.file.Path;

import fr.upem.captcha.images.Category;

public class Vehicule extends Category {
  static final Path getClassDirectoryPath(){
    try { 
      return Path.of(Vehicule.class.getResource("Vehicule.class").toURI());
    } catch(URISyntaxException error) {
      return null;
    }
  }
  
  Vehicule() {
     super(getClassDirectoryPath());
  }
}
