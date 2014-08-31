import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ClickCookie {
	private static JavascriptExecutor jse;
	private static FirefoxDriver driver;
	private final static String loadfilepath = "./resources/loadfile.txt";
	private static Path loadfile;
	private static int highest = 0;
	private static int highestafford = 0;

	public static void main(String[] args) throws IOException {
		driver = new FirefoxDriver();
		jse = (JavascriptExecutor) driver;
		driver.get("http://orteil.dashnet.org/cookieclicker/");
		loadfile = Paths.get(loadfilepath).normalize();

		loadGame();

		while (true) {
			if (highest < 10) updateHighProducts();
			driver.findElement(By.id("product" + Dynamic.bestProdToBuy())).click();
			saveGame();

			long t = System.currentTimeMillis();
			long end = t + 15000;
			while (System.currentTimeMillis() < end) {
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

	private static void updateHighProducts() {
		String jsQuery = "return Game.ObjectsById[" + (highest + 1) + "].locked";
		if (Integer.parseInt(jse.executeScript(jsQuery).toString()) == 0) {
			highest++;
			if (getPrice(highest) < getCookiesInBank()) {
				highestafford++;
			}
		}
	}

	public class Dynamic {
		private final int SHRINK_THRES = 1000;
		private Double shrinkfactor;
		
		private int bestProdToBuy() {
			int n = 0;
			Double cookies = getCookiesInBank();
			shrinkfactor = (cookies > SHRINK_THRES) ? (cookies / SHRINK_THRES) : Double.valueOf(1);
			for (n = highestafford; n <= highest; n++) {
				knapSack(n, shrink(cookies));
				// TODO (2) keep list of objects with max value
			}
			return n;
		}
		
		private List<Integer> knapSack(int n, int capacity) {
			Double[][] opt = new Double[n][capacity + 1];
			for (int w = 0; w <= capacity; w++) { opt[0][capacity] = (double) 0; }
			for (int i = 1; i < n; i++) {
				for (int w = 0; w <= capacity; w++) {
					opt[i][w] = Double.max(shrink(getProdCookiePerSecond(i)) + opt[i-1][w - shrink(getPrice(i))] , opt[i-1][w]);
				}
			}
			// TODO (1) generate list of items from opt[];
		}
		
		// guarantees knapsack wont chew on a capacity larger than 1000
		private int shrink(Double largething) {
			return ((Double) (largething / shrinkfactor)).intValue();
		}
	}

	public static class Greedy {
		private static int bestProdToBuy() {
			int i = highest;
			while ((i > 0) && (getPrice(i) > getCookiesInBank())) { i--; }
			System.out.println("Can afford: " + i);
			while ((i > 0) && (!isHigherWorthIt(i))) { i--; }
			System.out.println("Will buy: " + i);
			return i;
		}
	
		private static boolean isHigherWorthIt(int high) {
			double hiprice = getPrice(high);
			double hicooksec = getProdCookiePerSecond(high);
			double loprice = getPrice(high - 1);
			double locooksec = getProdCookiePerSecond(high - 1);
			System.out.println(high + " Ratio: " + hiprice/hicooksec + " and " + (high - 1) + " Ratio: " + loprice/locooksec);
			return (hiprice/hicooksec) < (loprice/locooksec);
		}
	}

	private static Double getCookiesInBank() {
		String jsQuery = "return Game.cookies";
		Double cookrate = Double.parseDouble(jse.executeScript(jsQuery).toString());
		return cookrate;
	}

	private static Double getProdCookiePerSecond(int prodno) {
		String jsQuery="return Game.ObjectsById[" + prodno + "].storedTotalCps*Game.globalCpsMult";
		Double cookrate = Double.parseDouble(jse.executeScript(jsQuery).toString());
		return cookrate;
	}

	private static Double getPrice(int prodno) {
		String jsQuery = "return Game.ObjectsById[" + prodno + "].price";
		Double price = Double.parseDouble(jse.executeScript(jsQuery).toString());
		return price;
	}
}
