package fr.upem.capcha.images;

import java.util.List;                    // data structure used for the properties
import java.util.stream.Stream;           // used for mapping, reducing and other functional programming techniques
import java.util.stream.Collectors;       // used to convert back Streams into Lists
import java.nio.file.Path;                // used to locate Category directories
import java.nio.file.Files;               // used to read the category Directory
import java.io.File;                      // used to filter subdirectories and files
import java.io.IOException;
import java.net.URL; // used to represent Photos
import java.util.Random; // used to generate random indexes for the random image getter
import java.util.Objects; // used to check for failed URL to URI conversions
import java.net.MalformedURLException; // thrown on failed URI to URL conversion
import java.net.URISyntaxException;

/**
 * @class Category
 * @brief Provides images sorted in a category tree
 */

// represents a more or less precise category of image
public class Category implements Images {

  private static final String[] SUPPORTED_FILE_TYPES = {"jpeg", "png", "jpg", "gif"};

  /// @brief Describes the photo category to the user
  private final String name;

  /// @brief Lists subcategories of the category
  private final List<Category> subcategories;

  /// @brief photos at the top of the category tree. excludes all sub categories
  /// photos.
  private final List<URL> photos;

  /**
   * @brief getter on a random first-level sub-category
   * @return returns a randomly choozen sub-category
   */
  public Category getRandomSubCategory() {
    if (subcategories.isEmpty()) return null;

    // initialize randomizer
    // set the seed to the UNIX timestamp
    var randomizer = new Random();
    randomizer.setSeed(System.currentTimeMillis());

    return subcategories.get(randomizer.nextInt(subcategories.size()));
  }

  /**
   * @brief recursively gathers all the photos in the category and its
   *        subcategories
   * @return collection of URLs
   */
  public List<URL> getPhotos() {
    return subcategories.stream()
      // gather all the subcategory photos in an list of lists (for each category).
      .map(subcategory -> subcategory.getPhotos().stream())
      // combine the lists into the root image list.
      .reduce(photos.stream(), Stream::concat)
      .collect(Collectors.toList());
  }

  /**
   * @brief selects a random sample of all the photos in the category.
   * @param count size of the returned sample
   * @return random subset of all the photos URLs.
   */
  public List<URL> getRandomPhotosURL(int count) {
    assert count > 0 ;
  
    // get all the category photos
    var allPhotos = getPhotos();

    // initialize randomizer
    // set the seed to the UNIX timestamp
    var randomizer = new Random();
    randomizer.setSeed(System.currentTimeMillis());

    return randomizer
      // Creates a stream of count random indexes of the allPhoto list
      .ints(count, 0, allPhotos.size()) // TODO risk of dupicates
      // maps each index to a photo, creating a stream of random photos
      .mapToObj(allPhotos::get)
      // collects the stream into a List type
      .collect(Collectors.toList());
  }

  /**
   * @brief alias of getRandomPhotosURL for a single photo
   * @return a single photo URL
   */
  public URL getRandomPhotoURL() {
    return getRandomPhotosURL(1).get(0);
  }

  /**
   * @brief checks recursively if URL points to a photo member of the category or
   *        its subcategories
   * @param photo needle URL searched in all photo
   * @return boolean, true if the URL is a member of the category
   */
  public boolean isPhotoCorrect(URL photo) {
    return getPhotos().contains(photo);
  }

  /**
   * @brief category name getter.
   * @return name property
   */
  public String name() {
    return name;
  }

  private boolean isEmpty() {
    return photos.isEmpty() && subcategories.isEmpty();
  }

  private boolean notEmpty() {
    return !isEmpty();
  }

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
   * @constructor Category
   * @brief scans through a directory on the disk to construct the photo
   *        categories tree
   * @param directoryPath target directory
   */
  public Category(Path directoryPath) throws IOException{
    // stores all the entries in the directory (subs and images)
    List<File> entries;

    // Tries to load the entries. If fails the directoryPath is probably invalid.
    entries = Files.list(directoryPath).map(Path::toFile).collect(Collectors.toList());

    // set class properties
    subcategories = createCategoriesFromDirectoryEntries(entries);
    photos = createImageListFromDirectoryEntries(entries);

    // sets the category name as the directory name.
    name = directoryPath.getFileName().toString();
  }  

  /**
   * @brief static private, constructor helper method extracting subcategories from a stream of files.
   * @param entries stream of Files object to parse
   * @return List of categoriess
   * 
   * the category constructor is called for each direcotry, building the tree recursively.
   * coded in declarative style programming
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
   * @brief static private, helper method extracting images in a stream of files
   * @param entries stream of Files object to parse
   * @return List of URL photos
   * 
   * Only files at the root of the target directory are scanned.
   * coded in declarative style programming
   */
  private static List<URL> createImageListFromDirectoryEntries(List<File> entries) {
    // filters the images out of the directories
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
  
  static final Path getClassDirectoryPath(){
	  	try { 
	      return Path.of(Category.class.getResource("Category.class").toURI()).getParent();
	    } catch(URISyntaxException error) {
	      return null;
	    }
  }

}