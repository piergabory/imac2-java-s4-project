package fr.upem.capcha.ui;

import java.awt.GridLayout;
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
  private String message;
  private SelectablePhotoGridView selectionGrid;

  public CaptchaView() {
    super("captcha");

    setSize(1024, 768);
    setResizable(false); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    setLayout(new GridLayout(3, 1));
    update();
    
    setVisible(true);
  }

  public void update() {
    removeAll();
    var capchaPhotos = delegate.capchaUIDisplayedPhotos();
    selectionGrid = new SelectablePhotoGridView(capchaPhotos);
    add(selectionGrid);
		add(new JTextArea(message));
		add(createOkButton());
  }

  public void setMessage(String message) {
    this.message = message;
    update();
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
}