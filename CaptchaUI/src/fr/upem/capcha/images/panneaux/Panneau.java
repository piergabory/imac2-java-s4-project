package fr.upem.capcha.images.panneaux;

import fr.upem.capcha.images.Category;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class Panneau extends Category {
  static final Path getClassDirectoryPath(){
    try { 
      return Path.of(Panneau.class.getResource("Panneau.class").toURI()).getParent();
    } catch(URISyntaxException error) {
      return null;
    }
  }
  
  Panneau() {
     super(getClassDirectoryPath());
  }
}
