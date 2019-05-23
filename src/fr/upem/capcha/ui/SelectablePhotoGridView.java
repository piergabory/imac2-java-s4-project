/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.ui;
import fr.upem.capcha.ui.SelectablePhotoLabelView;
import java.net.URL;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Selectable Photo Grid View.
 */
class SelectablePhotoGridView extends JPanel {
  private static final long serialVersionUID = 1L;

  static private final int COLUMNS_COUNT = 3;

  /**
   * Grid view Constructor
   * @param photos displayed in the grid
   */
  public SelectablePhotoGridView(List<URL> photos) {
    super();

    // create a layout to fit all the photos
    int rowsCount = photos.size() / COLUMNS_COUNT + 1;
    setLayout(new GridLayout(rowsCount, COLUMNS_COUNT));
    
    // try to add the photos or ignore the failed ones.
    for (URL location : photos) {
      try {
        add(new SelectablePhotoLabelView(location));
      } catch(IOException exception) {
        System.err.println(exception.getLocalizedMessage());
      }
    }

    // shows when ready
    setVisible(true);
  }

  /**
   * Selected photos getter.
   * @return all the selected photos URLs in the container
   */
  public List<URL> selected() {
    return 
    // get all the Selectable Photo Label Views in this container
    Stream.of(getComponents())
      .filter(SelectablePhotoLabelView.class::isInstance)
      .map(SelectablePhotoLabelView.class::cast)

      //selection filter, URL map and return 
      .filter(SelectablePhotoLabelView::isSelected)
      .map(SelectablePhotoLabelView::photo)
      .collect(Collectors.toList());
  }
}