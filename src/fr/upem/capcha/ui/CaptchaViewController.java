package fr.upem.capcha.ui;

import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import fr.upem.capcha.images.ImagesProvider;

public class CaptchaViewController implements CaptchaViewDelegate {

  private ImagesProvider provider = new ImagesProvider();
  private CaptchaView captchaView = new CaptchaView(this);

  private Runnable successHandler;
  private Runnable cancelHandler;

  public CaptchaViewController() {
    captchaView.setDelegate(this);
    setPhotoKindIndicatorMessage();
    captchaView.updatePhotos();
    captchaView.setVisible(true);
  }

  @Override
  public void captchaUISubmitPhotosAction(List<URL> photos) {
    String message = "Au moins une photo était incorrecte."; 
    switch (provider.isSelectionCorrect(photos)) {
      case MISSING:
      message = "Vous avez oublié une photo";

      case INVALID:
      captchaView.setMessage("Cliquez sur les images contenant des " + provider.currentTargetName());
      setPhotoKindIndicatorMessage();
      captchaView.updatePhotos();
      break;

      case CORRECT:
      message = "Bravo!";
      captchaView.dispose();
      successHandler.run();
      break;
    }
    JOptionPane.showMessageDialog(null, message);
  }

  @Override
  public List<URL> capchaUIDisplayedPhotos() {
    return provider.getPhotoTestBatch(9, 4);
  }

  @Override
  public void captchaUIAbortAction() {
    captchaView.dispose();
    cancelHandler.run();
  }

  public CaptchaViewController then(Runnable onSuccess) {
    successHandler = onSuccess;
    return this;
  }

  public CaptchaViewController cancel(Runnable onCancel) {
    cancelHandler = onCancel;
    return this;
  }

  private void setPhotoKindIndicatorMessage() {
    captchaView.setMessage("Cliquez sur les images contenant des " + provider.currentTargetName());
  }
}
