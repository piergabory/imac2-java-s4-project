/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.images;
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
import java.io.IOException;

/**
 * Provides images sorted in a category tree
 */
public class Category implements Images {

  private static final String PHOTOS_ROOT_DIRECTORY = "assets";
  private static final String[] SUPPORTED_FILE_TYPES = {"jpeg", "png", "jpg", "gif"};

  private static final Random randomizer = new Random(System.currentTimeMillis());

  private final String name;
  private final List<Category> subcategories;   // Describes the photo category to the user
  private final List<URL> photos;               // photos at the top of the category tree. excludes all sub categories

  /**
   * getter on a random first-level sub-category
   * @return returns a randomly choozen sub-category
   */
  public Category getRandomSubCategory() {
    if (subcategories.isEmpty()) return null;
    return subcategories.get(randomizer.nextInt(subcategories.size()));
  }

  /**
   * recursively gathers all the photos in the category and its subcategories
   * @return collection of URLs
   */
  public List<URL> getPhotos() {
    return subcategories.stream()
      .map(subcategory -> subcategory.getPhotos().stream())
      .reduce(photos.stream(), Stream::concat)
      .collect(Collectors.toList());
  }

  /**
   * selects a random sample of all the photos in the category.
   * @param count size of the returned sample
   * @return random subset of all the photos URLs.
   */
  public List<URL> getRandomPhotosURL(int count) {
    assert count > 0 ;
  
    var allPhotos = getPhotos();

    return randomizer
      .ints(count, 0, allPhotos.size()) // TODO risk of dupicates
      .mapToObj(allPhotos::get)
      .collect(Collectors.toList());
  }

  /**
   * alias of getRandomPhotosURL for a single photo
   * @return a single photo URL
   */
  public URL getRandomPhotoURL() {
    return getRandomPhotosURL(1).get(0);
  }

  /**
   * checks recursively if URL points to a photo member of the category or its subcategories
   * @param photo needle URL searched in all photo
   * @return boolean, true if the URL is a member of the category
   */
  public boolean isPhotoCorrect(URL photo) {
    return getPhotos().contains(photo);
  }

  /**
   * Creates a tree containing all the photos available.
   * @return Global category getter
   */
  public static Category getAll() {
    try {
      var path = getClassDirectoryPath();
      return new Category(path);
    } catch(IOException exception) {
      System.err.println("Failed to access picture directory");
      return null;
    }
  }

  /**
   * Scans through a directory on the disk to construct the photo  categories tree
   * @param directoryPath target directory
   */
  public Category(Path directoryPath) throws IOException{
    List<File> entries;

    // Tries to load the entries. If fails the directoryPath is probably invalid.
    entries = Files.list(directoryPath).map(Path::toFile).collect(Collectors.toList());

    subcategories = createCategoriesFromDirectoryEntries(entries);
    photos = createImageListFromDirectoryEntries(entries);

    name = directoryPath.getFileName().toString();
  }  

  /**
   * Helper method extracting subcategories from a stream of files.
   * <p>
   * the category constructor is called for each direcotry, building the tree recursively.
   * coded in declarative style programming
   * 
   * @param entries stream of Files object to parse
   * @return List of categoriess
   */
  private static List<Category> createCategoriesFromDirectoryEntries(List<File> entries) {
    return entries.stream()
      .filter(File::isDirectory)
      .map(File::toPath)
      .map(path -> {
        try {
          return new Category(path);
        } catch(IOException exception) {
          return null;
        }
      })
      .filter(Objects::nonNull)
      .filter(Category::notEmpty)
      .collect(Collectors.toList());
  }

  /**
   * Helper method extracting images in a stream of files
   * <p>
   * Only files at the root of the target directory are scanned.
   * coded in declarative style programming
   *
   * @param entries stream of Files object to parse
   * @return List of URL photos
   */
  private static List<URL> createImageListFromDirectoryEntries(List<File> entries) {
    return entries.stream()
      .filter(File::isFile)
      .filter(
        file -> Stream.of(SUPPORTED_FILE_TYPES).anyMatch(
          suffix -> file.getName().toLowerCase().endsWith(suffix)
        )
      )
      .map(File::toURI) 
      .map(URI -> {
        try { 
          return URI.toURL();
        } catch(MalformedURLException exception) {
          System.err.println(exception.getLocalizedMessage());
          return null;
        }
      })
      
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
  
  /**
   * Helper method providing the path to the global root photo directory
   * @return photo directory path
   */
  private static final Path getClassDirectoryPath(){
	  	var directoryURL = Category.class.getResource(PHOTOS_ROOT_DIRECTORY);
	  	try { 
	      return Path.of(directoryURL.toURI());
	    } catch(Exception error) {
	    	System.out.println(error.getLocalizedMessage());
	      return null;
	    }
  }

  // Getters

  public String name() {
    return name;
  }

  public boolean hasSubcategories() {
    return !subcategories.isEmpty();
  }

  private boolean isEmpty() {
    return photos.isEmpty() && subcategories.isEmpty();
  }

  private boolean notEmpty() {
    return !isEmpty();
  }

}