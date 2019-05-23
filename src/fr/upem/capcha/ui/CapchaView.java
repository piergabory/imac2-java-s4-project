/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha.ui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *  Capcha View Class provides a user interface for a capcha program with a 3 column image grid, a submit button and a cancel button.
 */
public class CapchaView extends JFrame {
  private static final long serialVersionUID = 1L;

  // window parameters
  private static final String WINDOW_TITLE = "Capcha";
  private static final int WINDOW_PIXEL_WIDTH = 1024;
  private static final int WINDOW_PIXEL_HEIGHT = 768;

  // View Delegate
  private CapchaViewDelegate delegate;

  // references to the mutable components to be removed when updated.
  private JTextArea message;
  private SelectablePhotoGridView selectionGrid;

  /**
   * Capcha View Constructor. Creates and setup the window
   * @param delegate
   */
  public CapchaView(CapchaViewDelegate delegate) {
    super(WINDOW_TITLE);

    this.delegate = delegate;
    setSize(WINDOW_PIXEL_WIDTH, WINDOW_PIXEL_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // define the red traffic light action
    
    // Border layout looks like this:
    //-------------------------------
    // NORTH  NORTH   NORTH
    // WEST   CENTER  EAST
    // WEST   CENTER  EAST
    // SOUTH  SOUTH   SOUTH
    //-------------------------------
    setLayout(new BorderLayout());                    

    add(createAbortButton(), BorderLayout.WEST);
    add(createOkButton(), BorderLayout.EAST);
  }

  /**
   * Replaces the selectable photo grid with photos provided by the delegate
   * <p>
   * Requires the delegate to be set.
   */
  public void updatePhotos() {
    assert delegate != null;

    if(selectionGrid != null) {
      invalidate();
      remove(selectionGrid);
    }
     
    var capchaPhotos = delegate.capchaViewDisplayedPhotosProvider();
    selectionGrid = new SelectablePhotoGridView(capchaPhotos);
    add(selectionGrid, BorderLayout.CENTER);

    revalidate();
    repaint();
  }

  /**
   * Replaces the message label with a new on with the parameter String
   * @param text written on the label
   */
  public void setMessage(String text) {
    if(message != null) {
      invalidate();
      remove(message);
    }

    message = new JTextArea(text);
    add(message, BorderLayout.NORTH);

    revalidate();
    repaint();
  }

  /**
   * Submit Button Factory
   * @return button with the delegate's submit handler called on click
   */
  private JButton createOkButton() {
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			private static final long serialVersionUID = 1L;
      @Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(() -> delegate.capchaViewSubmitPhotosAction(selectionGrid.selected()));
			}
		});
  }

  /**
   * Abort Button Factory
   * @return button with the delegate's cancel handler called on click
   */
  private JButton createAbortButton(){
		return new JButton(new AbstractAction("Annuler") { //ajouter l'action du bouton
			private static final long serialVersionUID = 1L;
      @Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(delegate::capchaViewAbortAction);
			}
		});
  }
}