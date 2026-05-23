### Uses

- Create Auction
- Get auction by ID
- Get all live auctions
- Maintain Set of active auctions and add or remove according to status
- Close, Expire, Cancel auction
- Handle bid placement
- Publish and consume events
- Auction cleanup
- Get winner and all bussiness logic of auctions

## Note / TODO
- User TTL for auction expiry and once expired create an event before removal


Step 1: Receiving the Request
How it's done: Your controller accepts the incoming payload reactively as a Mono<AuctionCreationRequest>.
Under the hood: Because you are running WebFlux on Tomcat NIO, the engine thread reads the request body without blocking and immediately registers the downstream pipeline.

Step 2: Fetching Seller Details (Inter-Service Call)
How it's done: You use WebClient to make an outbound HTTP GET request to the User Service.
The Reactive Way: You extract the sellerId from the payload and use WebClient to retrieve the user profile, mapping the response to a Mono<SellerDto>.
Why it's efficient: The thread does not wait for the network response. It drops off a callback and handles other traffic until the User Service responds.

Step 3: Initializing the Hot Path Data in Redis
Once the seller details arrive, you combine them with the request body to initialize your auction state across three distinct Redis data structures using ReactiveRedisTemplate:
Auction Metadata (Hash): You use a Redis Hash (keyed by auction:{auctionId}) to store the core mutable state (e.g., title, sellerId, reservePrice, expiresAt, and setting the initial highestBid to 0).
Live Auction Tracker (Set): You issue an SADD command to add the auctionId into your auctions:active Set so users can instantly discover live listings.
The Bidding Leaderboard (Sorted Set): You initialize an empty Sorted Set keyed by bidlist:{auctionId}. To make it ready for future bids, you can optionally pre-populate it with a system/base score of 0 tied to a placeholder "initial" state.
MVP Optimization Note: You will chain these three operations together using a reactive operator (like .flatMap() or .zip()) so they execute sequentially or concurrently as a single non-blocking flow.

Step 4: Emitting the Asynchronous Persistence Event
How it's done: Once Redis confirms all data is written, the Auction Service constructs an AuctionCreatedEvent containing all the metadata.
The Messaging: You pass this event payload into your reactive Redis Pub/Sub publisher, targeting a dedicated channel like auction:creation:events.
The Fire-and-Forget Hand-off: As soon as Redis acknowledges the message was published, your HTTP controller instantly returns a 201 Created status back to the user. It does not wait for PostgreSQL.

What Happens in the Storage Service?
Completely decoupled from your API latency, the AuctionStorage Service runs a reactive listener on that same Redis channel. It safely picks up the event object, translates it into your relational entity mapping, and uses a reactive database driver to persist it to the BidInfo tables in PostgreSQL.
Whenever you are ready, let me know if you want to map out the exact reactive repository interfaces for Redis next, or dive straight into writing the service logic pipeline!
