package fr.upem.capcha;

import fr.upem.capcha.ui.CaptchaViewController;

public class Main {
  public static void main(String[] args) {
    (new CaptchaViewController())
      .then(() -> System.out.println("The user is probably human"))
      .cancel(() -> System.out.println("The user aborted the action"));
  }
}