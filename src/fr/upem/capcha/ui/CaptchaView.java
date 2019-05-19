package fr.upem.capcha.ui;

import java.net.URL;
import java.util.List;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


interface CaptchaViewDelegate {
  public void captchaUISubmitPhotosAction(List<URL> photos);
  public List<URL> capchaUIDisplayedPhotos();
}

public class CaptchaView {
  private JFrame frame;
  private JTextArea message;
  private CaptchaViewDelegate delegate;
  private SelectablePhotoGridView selectionGrid;

  public CaptchaView(CaptchaViewDelegate delegate) {
    frame = new JFrame("captcha");

    frame.setSize(1024, 768);
    frame.setResizable(false); 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(3, 1));

    message = new JTextArea("Cliquez n'importe où ... juste pour tester l'interface !");
    frame.add(message);
    frame.add(createOkButton());
    frame.setVisible(true);
  }

  public void updatePhotos() {
    if(selectionGrid != null) {
      frame.invalidate();
      frame.remove(selectionGrid);
    }
     
    var capchaPhotos = delegate.capchaUIDisplayedPhotos();
    selectionGrid = new SelectablePhotoGridView(capchaPhotos);

    frame.add(selectionGrid, 0);
    frame.revalidate();
    frame.repaint();
  }

  public void setMessage(String text) {
    frame.invalidate();
    frame.remove(message);

    message = new JTextArea(text);

    frame.add(message, 1);
    frame.revalidate();
    frame.repaint();
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
  
  /**
   * @param delegate the delegate to set
   */
  public void setDelegate(CaptchaViewDelegate delegate) {
    this.delegate = delegate;
  }

}