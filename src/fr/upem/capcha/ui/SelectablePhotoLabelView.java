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

class SelectablePhotoLabelView extends JLabel {
  private static final long serialVersionUID = 1L;
  private boolean selected = false;
  final private URL photoLocation;

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

  public boolean isSelected() {
    return selected;
  }

  private void toggle() {
    selected = !selected;
  }

  public URL photo() {
    return photoLocation;
  }

  private void setMouseListeners() {
    addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean selected = false;
			
      @Override
      public void mouseReleased(MouseEvent arg0) {}
      
      @Override
      public void mousePressed(MouseEvent arg0) {}
      
      @Override
      public void mouseExited(MouseEvent arg0) {}
      
      @Override
      public void mouseEntered(MouseEvent arg0) {}
    
      @Override
      public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
        EventQueue.invokeLater(new Runnable() { 
          
          @Override
          public void run() {
            toggle();
            System.out.println(isSelected());
            if(isSelected()){
              setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
            else {
              setBorder(BorderFactory.createEmptyBorder());
            }
          }
        });
      }
    });
  }
}