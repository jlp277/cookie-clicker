import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ClickCookie {
	private static int highproduct = 0;
	private static JavascriptExecutor jse;
	private static FirefoxDriver driver;
	private static Map<Integer,Double> prices;
	private static Map<Integer,Double> cookrates;

	public static void main(String[] args) {
		driver = new FirefoxDriver();
		jse = (JavascriptExecutor) driver;
		driver.get("http://orteil.dashnet.org/cookieclicker/");
		prices = new HashMap<Integer,Double>();
		cookrates = new HashMap<Integer,Double>();
		while (true) {
			// find highest item
			// get cookie per second
			// check if its worth it to buy next product
			updateHighProduct();
			driver.findElement(By.id("product"+ bestProdToBuy())).click();
			// buy next product
			int i;
			for (i = 0; i <= 20; i++) {
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
		double hiprice = getPrice(i + 1);
		double hicooksec = getCookiePerSecond(i + 1);
		double loprice = getPrice(i);
		double locooksec = getCookiePerSecond(i);
		return (hiprice/hicooksec) < (loprice/locooksec);
	}

	private static Double getCookiePerSecond(int prodno) {
		if (cookrates.containsKey(prodno)) {
			return cookrates.get(prodno);
		} else {
			// the cookies per second of product numbered <prodno>
			String jsQuery="return Game.ObjectsById[" + prodno + "].storedTotalCps*Game.globalCpsMult";
			Double cookrate = Double.parseDouble(jse.executeScript(jsQuery).toString());
			cookrates.put(prodno, cookrate); 
			return cookrate;
		}
	}

	private static Double getPrice(int prodno) {
		if (prices.containsKey(prodno)) {
			return prices.get(prodno);
		} else {
			String jsQuery = "return Game.ObjectsById[" + prodno + "].price";
			Double price = Double.parseDouble(jse.executeScript(jsQuery).toString());
			prices.put(prodno, price);
			return price;
		}
	}
}
