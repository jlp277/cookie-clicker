cookie-clicker
==============

###Mission to create a fast cookie clicker

#####cookie-clicker is a bot that goes onto the <a href="http://orteil.dashnet.org/cookieclicker/">Cookie Clicker</a> game and clicks on stuff.

The only rule is no cheating - only can automate actions that involve clicking. Exceptions are getting information not easily retrieved from the DOM via javascript.

######Todo

<b>Features</b>
- Regulate how often products are bought. One rate of buying new products may to too slow when everything is cheap and also too fast when things get expensive.
	
	Another issue could be that depending on the current cookie per second, it may be clicking the cookie too many times. At high cps we may have enough cookies to buy again even if the cookie clicking interation is not finished.
- Check if opportunity cost of waiting to buy next product is worth not buying current available.

	Currently, only algorithm in play is a greedy one. Incorporate dynamic programming - what linear combination of products will get me top product fastest?
- Check for bogus products. There may be a case where certain product is not valuable but the next locked one is. 
- Add clicker-upgrade capability.
- Add selling capability. Sell slow products now to get money for shiny expensive ones quicker.
- Click lucky rabbits when they appear.
- Benchmarking.

<b>Known Issues</b>
- ~~parseInt() cannot handle large integers. possibly use parseLong().~~

<b>Meta</b>
- Make loop for clicking the cookie faster. Make it run for time intervals instead of number of instructions. Tiered time intervals - some operations should happen less often, like saving.
- Have it run on faceless browser. Would probably use Phantomjs.
- Refactoring. Make extendable PurchasingManager class that handles buying products. Others can implement this class to make faster purchasers.
- GUI
- Version roadmap
- ~~Add persistence between selenium sessions. Perhaps use the user's default browser profile in webdriver?~~
	
	~~Or use ingame import export feature.~~
- ~~Save prices and cookie per second stats in a hashtable.~~