# hackeat-server
A simple Spark REST API that serves as a relay server for TwiML. An alternate for ngrok tunnelling.
Configured to host on heroku.

## Motivation
TwiML reads XML data from a remote server to generate voice for a voice call. This is fine for static voice calls, but for
voice calls that react to the data from the client, we require a dynamic remote server in which TwiML should fetch from.
This REST API allows the client to post XML data before calling the TwiML API to fetch that same data, allowing for dynamic
voice controls.

## How it works
Accepts a http POST request to a specific path and saves it in a hashmap so when TwiML does a post request to that path,
it will be provided with that data, usually in XML format.

## Server side - deploying on heroku
1. `heroku login`

2. `heroku create`

3. `git push heroku master`

## Client side - Configuring for TwiML
Send a HTTP POST request to `{host url}/create/{id}` containing XML data. Then when creating call, using `Call.creator(...)`
specify the url to `{host url}/order/{id}`.
