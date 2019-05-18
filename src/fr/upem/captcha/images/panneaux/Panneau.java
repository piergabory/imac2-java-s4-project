package fr.upem.captcha.images.panneaux;

import fr.upem.captcha.images.Category;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class Panneau extends Category {
  static final Path getClassDirectoryPath(){
    try { 
      return Path.of(Panneau.class.getResource("Panneau.class").toURI());
    } catch(URISyntaxException error) {
      return null;
    }
  }
  
  Panneau() {
     super(getClassDirectoryPath());
  }
}
