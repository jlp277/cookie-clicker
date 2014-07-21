cookie-clicker
==============

###Personal mission to create a fast cookie clicker

#####cookie-clicker is a bot that goes onto the <a href="http://orteil.dashnet.org/cookieclicker/">Cookie Clicker</a> game and clicks on stuff.

######Todo

<b>Features</b>
- Regulate how often products are bought. One rate of buying new products may to too slow when everything is cheap and also too fast when things get expensive.
	
	Another issue could be that depending on the current cookie per second, it may be clicking the cookie too many times. At high cps we may have enough cookies to buy again even if the cookie clicking interation is not finished.
- Make loop for clicking the cookie faster.
- Check if opportunity cost of waiting to buy next product is worth not buying current available.

	Currently, only algorithm in play is a greedy one. Incorporate dynamic programming - what linear combination of products will get me top product fastest?
- Check for bogus products. There may be a case where certain product is not valuable but the next locked one is. 
- Add clicker-upgrade capability.
- Add selling capability. Sell slow products now to get money for shiny expensive ones quicker.
- ~~Add persistence between selenium sessions. Perhaps use the user's default browser profile in webdriver?~~
	
	~~Or use ingame import export feature.~~
- ~~Save prices and cookie per second stats in a hashtable.~~

<b>Known Issues</b>
- ~~parseInt() cannot handle large integers. possibly use parseLong().~~

<b>Meta</b>
- Refactoring. Make bestProduct method extendable to classes that others can implement.
- Version roadmap