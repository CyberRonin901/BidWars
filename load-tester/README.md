# WebSocket Load Tester (k6)

Load testing script for BidWars auction service WebSocket endpoints using k6.

## Features

- Configurable auction ID and request template
- Adjustable bids per second rate
- Incrementing bid amounts (starts at base amount, increments by 1 for each request)
- Real-time progress tracking
- Detailed results reporting with k6 metrics

## Installation

1. Install k6:
```bash
# On macOS
brew install k6

# On Linux
sudo gpg -k
sudo gpg --keyserver hkp://keyserver.ubuntu.com --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6

# Or download from https://k6.io/docs/getting-started/installation/
```

## Configuration

Edit `config.json` to customize the load test:

```json
{
  "websocket_url": "ws://localhost:8082/ws",
  "auction_id": "00000000-0000-0000-0000-000000000001",
  "bids_per_second": 10,
  "duration_seconds": 60,
  "base_request": {
    "userId": "00000000-0000-0000-0000-000000000001",
    "username": "load_tester",
    "amount": 100
  }
}
```

### Configuration Options

- **websocket_url**: WebSocket endpoint URL (default: `ws://localhost:8082/ws`)
- **auction_id**: UUID of the auction to bid on
- **bids_per_second**: Number of bids to send per second
- **duration_seconds**: How long the load test should run
- **base_request**: Template for bid requests
  - **userId**: UUID of the user placing bids
  - **username**: Username for the bidder
  - **amount**: Starting bid amount (increments by 1 for each subsequent bid)

## Usage

Run the load test:
```bash
k6 run websocket_load_test.js
```

Run with output to file:
```bash
k6 run --summary-export=result.json websocket_load_test.js
```

## Example Output

```
WebSocketBid..............: ✓ 100% 600/600
  status..................: 101

checks.....................: ✓ 100% 600/600
data_received..............: 0 B 0 B/s
data_sent..................: 0 B 0 B/s
ws_connecting..............: 0 B 0 B/s
ws_messages_received.......: 0 B 0 B/s
ws_messages_sent...........: 0 B 0 B/s
ws_ping...................: 0 B 0 B/s
ws_pong...................: 0 B 0 B/s
```

## How It Works

1. Loads configuration from `config.json`
2. Creates virtual users based on bids per second
3. Each virtual user connects to the WebSocket endpoint
4. Sends bid requests to the auction with incrementing amounts
5. Tracks connection status and errors
6. Reports detailed metrics including success rate, latency, and throughput
