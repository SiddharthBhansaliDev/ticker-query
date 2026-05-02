# TickerQuery

## Overview

TickerQuery is a Spring Boot REST API that allows you to pass in multiple tickers/date ranges simultaneously and quickly receive calculated statistics about each ticker's price over its corresponding date range. Currently, it will give you the start price, end price, and rate of return for each ticker.

## Demo

There is a Swagger page with default input here: [https://ticker-query-29369384786.us-central1.run.app](https://ticker-query-29369384786.us-central1.run.app)

**NOTE**: The demo is hosted on scale-to-zero infrastructure (GCP Cloud Run). This means that when you click the above link, there is a high chance that you will encounter a "cold start" (long loading time) while the instance initializes. Once the Swagger page loads, the API itself won't have this issue.

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
