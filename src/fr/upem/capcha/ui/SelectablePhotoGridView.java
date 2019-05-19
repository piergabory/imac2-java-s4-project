package fr.upem.capcha.ui;

import fr.upem.capcha.ui.SelectablePhotoLabelView;

import java.net.URL;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JPanel;
import java.awt.GridLayout;


class SelectablePhotoGridView extends JPanel {

  private static final long serialVersionUID = 1L;
  private final int COLUMNS_COUNT = 3;

  public SelectablePhotoGridView(List<URL> photos) {
    super();

    int rowsCount = photos.size() / COLUMNS_COUNT + 1;
    setLayout(new GridLayout(rowsCount, COLUMNS_COUNT));
    
    for (URL location : photos) {
      try {
        add(new SelectablePhotoLabelView(location));
      } catch(IOException exception) {}
    }

    setVisible(true);
  }

  public List<URL> selected() {
    return 
      Stream.of(getComponents())
      .filter(SelectablePhotoLabelView.class::isInstance)
      .map(SelectablePhotoLabelView.class::cast)

      .filter(SelectablePhotoLabelView::isSelected)
      .map(SelectablePhotoLabelView::photo)
      
      .collect(Collectors.toList());
  }
}