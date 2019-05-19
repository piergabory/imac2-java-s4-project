package fr.upem.capcha.ui;

import java.net.URL;
import java.util.List;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


interface CaptchaViewDelegate {
  public void captchaUISubmitPhotosAction(List<URL> photos);
  public List<URL> capchaUIDisplayedPhotos();
}

public class CaptchaView extends JFrame {
  private static final long serialVersionUID = 1L;
  private CaptchaViewDelegate delegate;
  private String message = "Cliquez n'importe où ... juste pour tester l'interface !";
  private SelectablePhotoGridView selectionGrid;

  public CaptchaView() {
    super("captcha");

    setSize(1024, 768);
    setResizable(false); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // setLayout(new GridLayout(3, 1));
    setVisible(true);
  }

  public void draw() {
    removeAll();
    
    var capchaPhotos = delegate.capchaUIDisplayedPhotos();
    selectionGrid = new SelectablePhotoGridView(capchaPhotos);
    
    add(selectionGrid);
		add(new JTextArea(message));
    add(createOkButton());

    revalidate();
  }

  public void setMessage(String message) {
    this.message = message;
    draw();
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