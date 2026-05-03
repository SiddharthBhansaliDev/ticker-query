# TickerQuery

## Overview

TickerQuery is a Spring Boot REST API that takes in multiple tickers/date ranges simultaneously and outputs calculated statistics about each ticker's price over its corresponding date range. Currently, it will return the start price, end price, and rate of return for each ticker.

<img width="346" height="364" alt="TickerQuery Screenshot" src="https://github.com/user-attachments/assets/a498c488-f4f3-4611-8d82-24acff5bece5" />

## Demo

There is a Swagger page with default input here: [https://ticker-query-29369384786.us-central1.run.app](https://ticker-query-29369384786.us-central1.run.app)

**NOTE:** This demo runs on GCP Cloud Run with scale-to-zero enabled to minimize cost. As a result, you may experience a cold start while the container initializes.

This can appear in two stages:

- Initial page load (Swagger UI): slower load time as the service starts
- First API request after load: additional latency while the application fully initializes

Once warmed, both the UI and API responses become consistently fast. After a period of inactivity, the demo will reset back to a "cold" state.

## Deploying Locally

You need Spring Boot 4.0.6 and Java 25. Additionally, currently the only price caller type supported is Tiingo, so you will need a Tiingo API key as well. As of May 2026, Tiingo has a very generous free tier, and all you need to sign up is an email address.

You will need to configure the following environment variables:

- PORT = {whatever port you want the API to run on} (MANDATORY; it does NOT default to 8080)
- TIINGO_API_KEY = {your API key, as described above}
- PRICE_CACHE = {leave it blank for in-memory or set it to GCS for a GCS cache; you will need your own GCS config for this}

Note that if you choose to use GCS, there is also a GCS_BUCKET_NAME environment variable, otherwise it will just default to "ticker-query".

Once you configure your environment variables, you can just run it like any other Spring Boot application.

## Architecture

The API has two data sources: a price caller (currenly only supports Tiingo) and a price cache (currently supports in-memory + GCS). These are configurable via the PRICE_CALLER and PRICE_CACHE environment variables, although the PRICE_CALLER variable is currently a bit useless since it defaults to the only option, Tiingo.

The way data retrieval works is this: the application fires off parallel virtual threads for each ticker, and each thread first checks the cache, and falls back to the price caller (makes a REST API call) if necessary. If the price caller was successfully used, it then caches the new data. It is all-or-nothing, meaning a single thread failing will make the entire response fail.

The price caller + price cache are both implemented through abstraction layers that make it trivial to add more caller types + cache types in the future. For example we would be able to add support for Alpha Vantage (price caller) and Redis (price cache) pretty easily.

Once the data is retrieved, it runs calculations and returns a response.

## Roadmap

There are 3 things that I want to continue to add to this project over time:

- New calculations (i.e. Volume adjusted rate of return)
- New price caller types (i.e. Alpha Vantage)
- New price cache types (i.e. Redis)
