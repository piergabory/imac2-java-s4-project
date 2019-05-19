package fr.upem.capcha.ui;

import java.net.URL;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


interface CaptchaViewDelegate {
  public void captchaUISubmitPhotosAction(List<URL> photos);
  public void captchaUIAbortAction();
  public List<URL> capchaUIDisplayedPhotos();
}

public class CaptchaView extends JFrame {
  private static final long serialVersionUID = 1L;
  private JTextArea message;
  private CaptchaViewDelegate delegate;
  private SelectablePhotoGridView selectionGrid;

  public CaptchaView(CaptchaViewDelegate delegate) {
    super("captcha");

    setSize(1024, 768);
    setResizable(false); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    add(createAbortButton(), BorderLayout.WEST);
    add(createOkButton(), BorderLayout.EAST);
  }

  public void updatePhotos() {
    if(selectionGrid != null) {
      invalidate();
      remove(selectionGrid);
    }
     
    var capchaPhotos = delegate.capchaUIDisplayedPhotos();
    selectionGrid = new SelectablePhotoGridView(capchaPhotos);

    add(selectionGrid, BorderLayout.CENTER);
    revalidate();
    repaint();
  }

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

  private JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			private static final long serialVersionUID = 1L;
      @Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(() -> delegate.captchaUISubmitPhotosAction(selectionGrid.selected()));
			}
		});
  }

  private JButton createAbortButton(){
		return new JButton(new AbstractAction("Annuler") { //ajouter l'action du bouton
			private static final long serialVersionUID = 1L;
      @Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(delegate::captchaUIAbortAction);
			}
		});
  }
  
  /**
   * @param delegate the delegate to set
   */
  public void setDelegate(CaptchaViewDelegate delegate) {
    this.delegate = delegate;
  }

}