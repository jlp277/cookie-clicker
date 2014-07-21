import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
	private final static String loadfilepath = "/Users/James/Documents/workspace/cookie-clicker/resources/loadfile.txt";
	private static Path loadfile;

	public static void main(String[] args) throws IOException {
		driver = new FirefoxDriver();
		jse = (JavascriptExecutor) driver;
		driver.get("http://orteil.dashnet.org/cookieclicker/");
		prices = new HashMap<Integer,Double>();
		cookrates = new HashMap<Integer,Double>();
		loadfile = Paths.get(loadfilepath).normalize();
		
		loadGame();
		
		while (true) {
			// find highest item
			// get cookie per second
			// check if its worth it to buy next product
			updateHighProduct();
			driver.findElement(By.id("product"+ bestProdToBuy())).click();
			saveGame();
			// buy next product
			int i;
			for (i = 0; i <= 20; i++) {
				driver.findElement(By.id("bigCookie")).click();
			}
		}
	}
	
	private static void loadGame() throws IOException {
		File f = loadfile.toFile();
		if(!f.isFile()) {
			System.out.println("New game instance. Creating save file.");
		    f.createNewFile();
		}
		byte[] loadcodebytes = Files.readAllBytes(loadfile);
		if (loadcodebytes.length > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			System.out.println("Loading game from file last saved: " + sdf.format(f.lastModified()));
			String loadcode = new String(loadcodebytes).trim();
			String jsQuery = "Game.ImportSaveCode('" + loadcode + "')";
			jse.executeScript(jsQuery);
		}
	}
	
	private static void saveGame() throws IOException {
		String jsQuery = "return Game.WriteSave(1)";
		byte[] loadcode = (jse.executeScript(jsQuery).toString()).getBytes(Charset.forName("UTF-8"));;
		Files.write(loadfile, loadcode);
	}

	private static void updateHighProduct() {
		int i = highproduct + 1;
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
