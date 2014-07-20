import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;


public class ClickCookie {
	private static int highproduct = 0;
	private static JavascriptExecutor jse;
	private static FirefoxDriver driver;

	public static void main(String[] args) {
		driver = new FirefoxDriver();
		jse = (JavascriptExecutor) driver;
		driver.get("http://orteil.dashnet.org/cookieclicker/");
		while (true) {
			// find highest item
			// get cookie per second
			// check if its worth it to buy next product
			updateHighProduct();
			driver.findElement(By.id("product"+ bestProdToBuy())).click();
			// buy next product
			int i;
			for (i = 0; i <= 200; i++) {
				driver.findElement(By.id("bigCookie")).click();
			}
		}
	}

	private static void updateHighProduct() {
		int i = highproduct + 1;
		System.out.println("i " + i);
		String jsQuery = "return Game.ObjectsById[" + (highproduct + 1) + "].locked";
		int islocked = Integer.parseInt(jse.executeScript(jsQuery).toString());
		if (islocked == 0) {
			System.out.println("highproduct: " + highproduct);
			highproduct++;
		}
	}

	private static int bestProdToBuy() {
		int i = highproduct;
		while (isHigherWorthIt(i)) { i--; }
		return i;
	}

	private static boolean isHigherWorthIt(int i) {
		float hiprice = getPrice(i + 1);
		float hicooksec = getCookiePerSecond(i + 1);
		float loprice = getPrice(i);
		float locooksec = getCookiePerSecond(i);
		return (hiprice/hicooksec) < (loprice/locooksec);
	}

	private static float getCookiePerSecond(int prodno) {
		// the cookies per second of product numbered <prodno>
		String jsQuery="return Game.ObjectsById[" + prodno + "].storedTotalCps*Game.globalCpsMult";
		return Float.parseFloat(jse.executeScript(jsQuery).toString());
	}

	private static int getPrice(int prodno) {
		String jsQuery = "return Game.ObjectsById[" + prodno + "].name";
		jsQuery = "return Game.ObjectsById[" + prodno + "].price";
		return Integer.parseInt(jse.executeScript(jsQuery).toString());
	}
}
