cookie-clicker
==============

###Personal mission to create a darn fast cookie clicker

#####cookie-clicker is a bot that goes onto the <a href="http://orteil.dashnet.org/cookieclicker/">Cookie Clicker</a> game and clicks on stuff.

<b>Todo</b>
- Regulate how often products are bought. One rate of buying new products may to too slow when everything is cheap and also too fast when things get expensive.
	
	Another issue could be that depending on the current cookie per second, it may be clicking the cookie too many times. At high cps we may have enough cookies to buy again even if the cookie clicking interation is not finished.
- Check if opportunity cost of waiting to buy next product is worth not buying current available.
- Check for bogus products. There may be a case where certain product is not valuable but the next locked one is. 
- Add clicker-upgrade capability
- Add selling capability. Sell slow products now to get money for shiny expensive ones quicker.
- Add persistence between selenium sessions. Perhaps use the user's default browser profile in webdriver?
- Save prices and cookie per second stats in a hashtable.

