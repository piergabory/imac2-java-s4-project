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
    try {
      new CapchaViewController(args.length == 1 ? args[0] : "./assets")
        .then(() -> { 
          System.out.println("SUCCESS");
          System.exit(0);
      })
        .cancel(() -> {
          System.out.println("CANCEL");
          System.exit(1);
        });
    } catch(Exception e) {
      System.out.println("FAILURE");
      System.err.println(e.getLocalizedMessage());
      System.exit(2);
    } 
  }
}