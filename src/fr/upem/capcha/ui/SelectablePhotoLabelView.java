/**
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.ui;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Photo sélectionable.
 * <p>
 * JLabel displaying a picture. If the user clicks on it, a red border with show around the frame.
 */
class SelectablePhotoLabelView extends JLabel {
  private static final long serialVersionUID = 1L;
  
  final private URL photoLocation;
  private boolean selected = false;

  /**
   * Constructor creates the label from the photo URL.
   * <p>
   * Stretches/Crops the picture to fit in the label.
   * @param location of the photo ressource
   * @throws IOException if the url target fails.
   */
  public SelectablePhotoLabelView(URL location) throws IOException {
    super();
    this.photoLocation = location;
    BufferedImage imgBuffer;
    try {
      imgBuffer = ImageIO.read(photoLocation); //lire l'image
    } catch (IOException e) {
      System.err.println("Failed to access photo at: " + photoLocation);
      throw e;
    }
    Image image = imgBuffer.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
    setIcon(new ImageIcon(image));
    setMouseListeners();
  }

  // GETTERS
  public boolean isSelected() {
    return selected;
  }

  public URL photo() {
    return photoLocation;
  }


  // PRIVATE
  private void toggle() {
    selected = !selected;
  }

  /**
   * Constructor helper method setting the click listener.
   */
  private void setMouseListeners() {
    addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
      @Override
      public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
        EventQueue.invokeLater(new Runnable() { 
          @Override
          public void run() {
            toggle();
            setBorder(isSelected() ? BorderFactory.createLineBorder(Color.RED, 3) : BorderFactory.createEmptyBorder());
          }
        });
      }
			
      @Override
      public void mouseReleased(MouseEvent arg0) {}
      
      @Override
      public void mousePressed(MouseEvent arg0) {}
      
      @Override
      public void mouseExited(MouseEvent arg0) {}
      
      @Override
      public void mouseEntered(MouseEvent arg0) {}
    });
  }
}