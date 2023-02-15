Wrote a caching DNS resolver. The program will listen for incoming DNS requests. When it receives one, it will check its local cache (a hash table) and, if it has a valid response in its cache for the query, it will send a result back right away. Otherwise, it will forward the request to Google's public DNS server (at 8.8.8.8), store Google's response in the local cache, and then send back the response.