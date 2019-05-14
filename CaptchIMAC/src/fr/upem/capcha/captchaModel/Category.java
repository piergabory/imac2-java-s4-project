package fr.upem.capcha.captchaModel;

import fr.upem.capcha.ImagesPublicInterface.Images; // Implemented by the class

// Imported Utilities
import java.util.List;                    // data structure used for the properties
import java.util.stream.Stream;           // used for mapping, reducing and other functional programming techniques
import java.util.stream.Collectors;       // used to convert back Streams into Lists
import java.nio.file.Path;                // used to locate Category directories
import java.nio.file.Files;               // used to read the category Directory
import java.io.File;                      // used to filter subdirectories and files
import java.net.URL;                      // used to represent Photos
import java.util.Random;                  // used to generate random indexes for the random image getter
import java.util.Objects;                 // used to check for failed URL to URI conversions
import java.net.MalformedURLException;    // thrown on failed URI to URL conversion


class Category implements Images {

  // Describes the photo category to the user
  private final String name;

  // Lists subcategories of the category
  private final List<Category> subcategories;

  // photos at the top of the category tree. excludes all sub categories photos.
  private final List<URL> photos;


  // returns a randomly choozen sub-category
  public Category getRandomSubCategory() {
    if(subcategories.isEmpty()) return null;

    // initialize randomizer
    var randomizer = new Random();
    // set the seed to the UNIX timestamp
    randomizer.setSeed(System.currentTimeMillis());

    return subcategories.get(randomizer.nextInt(subcategories.size()));
  }

  // recursively gathers all the photos in the category and subcategories
  public List<URL> getPhotos() {
    return subcategories.stream()

      // gather all the subcategory photos in an list of lists (for each category).
      .map(subcategory -> subcategory.getPhotos())

      // combine the lists into the root image list.
      .reduce(photos, (accumulator, subcategoryPhotos) -> {
        accumulator.addAll(subcategoryPhotos);
        return accumulator;
      });
  }

  // get {count} random photos from the category
  public List<URL> getRandomPhotosURL(int count) {
    // get all the category photos
    var allPhotos = getPhotos();

    // initialize randomizer
    var randomizer = new Random();
    // set the seed to the UNIX timestamp
    randomizer.setSeed(System.currentTimeMillis());

    return randomizer
      // Creates a stream of count random indexes of the allPhoto list
      .ints(count, 0, allPhotos.size())
      // maps each index to a photo, creating a stream of random photos
      .mapToObj(allPhotos::get)
      // collects the stream into a List type
      .collect(Collectors.toList());
  }

  // returns a random photo from the category
  public URL getRandomPhotoURL() {
    return getRandomPhotosURL(1).get(0);
  }

  // checks if URL points to a photo member of the category
  public boolean isPhotoCorrect(URL photo) {
    return getPhotos().contains(photo);
  }

  // category description name getter
  public String name() {
    return name;
  }

  // Remaps recursively the directory tree in memory
  public Category(Path directoryPath) {
    // stores all the entries in the directory (subs and images)
    Stream<File> entries;

    // Tries to load the entries. If fails the directoryPath is probably invalid.
    try {
      entries = Files.list(directoryPath).map(Path::toFile);
    } catch (Exception exception) {
      exception.printStackTrace();
      entries = Stream.empty();
    }

    // sets the category name as the directory name.
    name = directoryPath.getFileName().toString();

    
    // filters all the directory entries and recursively calls new Category constructor for the path
    subcategories = entries
      // filter the subdirectories out of the image files
      .filter(File::isDirectory)
      // convert each file into a path object
      .map(File::toPath)
      // convert each path into a new Category 
      .map(Category::new)
      // recollect the category stream into a list.
      .collect(Collectors.toList());


    // filters all the photos entries and converts them into an URL array
    photos = entries
      // filters the images out of the directories
      .filter(File::isFile)
      // convert the file object into an Universal Ressource Idebntifier
      .map(File::toURI)
      // convert the URI into an URL, if the file name is invalid:
      // - an error is thrown in the console, 
      // - the file is ignored and the collection continues
      .map(URI -> {
        URL returned;
        try {
          returned = URI.toURL();
        } catch(MalformedURLException exception) {
          System.err.println(exception.getLocalizedMessage());
          exception.printStackTrace();
          returned = null;
        }
        return returned;
      })
      // filter out failed URI to URL conversions
      .filter(Objects::nonNull)
      // collect the URL stream into a List
      .collect(Collectors.toList());
  }  
}