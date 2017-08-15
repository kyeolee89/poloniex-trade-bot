# poloniex-trade-bot

It is a program to store Poloniex's coin price, based on **Selenium**.

Crawling the data with the API sometimes fails, so I made it more accurate to crawl on the screen with Selenium.

It contains logic to store every 30 seconds, and if the price changes greatly, a telegram message will be sent.

