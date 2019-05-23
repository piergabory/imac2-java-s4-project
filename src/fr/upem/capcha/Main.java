/** 
 * @author Pierre Gabory
 * @author Solane Genevaux
 */
package fr.upem.capcha;
import fr.upem.capcha.ui.CapchaViewController;

/**
 * Demo class
 */
public class Main {
  public static void main(String[] args) {
    (new CapchaViewController())
      .then(() -> System.out.println("The user is probably human"))
      .cancel(() -> System.out.println("The user aborted the action"));
  }
}