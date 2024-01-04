# Client-Server Web Scraper

## About
Client-Server Web scraper project written for the Network Programming Course at the Faculty of Mathematics and Informatics.
By: Dimitar Tagarev, 9MI0800164 KN2, group 6
system: Linux (Arch Linux)
language: Java

## Short Summary:
The Client-Server Web Scraper is a project designed to scrape data from websites using a client-server architecture. 
The system enables multiple clients to request and retrieve data concurrently through a central server. 
The project can scrape any random article form Wikipedia and fetch every hyperlink in any site, and also save the data to a file.

## How to Use:

### Using the Web Scraper:
Run the WebScraperServer class, then run one Client and start using the scraper through the menu on the screen. I open it trough IntelliJ.
Other commands you can type as a client not visible in the menu. 
- "quit" - quits the current client
- "terminate" - quits the current client and the server

If you want to save the scraped data to a file, when you scrape the data a menu will appear 
on the bottom and there you can select to save the scraped data to a file.

Example wiki pages that you can scrape:
- https://en.wikipedia.org/wiki/Horse
- https://en.wikipedia.org/wiki/Cat

## Hard Parts While Making:
Implementing a communication between the client and server to efficiently exchange scraping requests and data proved challenging. The system needed to handle concurrent requests, data synchronization, and error handling to ensure a reliable scraping process.


