package fr.upem.capcha.ui;

import java.net.URL;
import java.util.List;

import fr.upem.capcha.images.ImagesProvider;

public class CaptchaViewController implements CaptchaViewDelegate {

  private ImagesProvider provider = new ImagesProvider();
  private CaptchaView captchaView = new CaptchaView();

  public CaptchaViewController() {
    captchaView.setDelegate(this);
    captchaView.draw();
  }

  @Override
  public void captchaUISubmitPhotosAction(List<URL> photos) {
    provider.isSelectionCorrect(photos, captchaView::setMessage);
  }

  @Override
  public List<URL> capchaUIDisplayedPhotos() {
    return provider.getPhotoTestBatch(9, 4);
  }
}
