import ws from 'k6/ws';
import { Counter } from 'k6/metrics';

const config = JSON.parse(open('./config.json'));

const successfulConnections = new Counter('successful_connections');
const bidsSent = new Counter('bids_sent');

export const options = {
    scenarios: {
        bidders: {
            executor: 'per-vu-iterations',
            vus: config.vus,
            iterations: 1,
            maxDuration: config.duration,
        },
    },
};

export default function () {
    ws.connect(
        config.websocket_url,
        {
            headers: {
                Authorization: `Bearer ${config.jwt_token}`,
            },
        },
        function (socket) {

            socket.on('open', () => {

                successfulConnections.add(1);
                console.log(`VU ${__VU} connected`);

                // STOMP CONNECT
                socket.send(
                    'CONNECT\n' +
                    'accept-version:1.2\n' +
                    'heart-beat:10000,10000\n' +
                    `Authorization: Bearer ${config.jwt_token}\n\n` +
                    '\0'
                );
            });

            socket.on('message', (msg) => {

                console.log(`VU ${__VU} received: ${msg}`);

                // Wait for STOMP CONNECTED frame
                if (msg.startsWith('CONNECTED')) {

                    // Subscribe to auction updates
                    socket.send(
                        'SUBSCRIBE\n' +
                        `id:sub-${__VU}\n` +
                        `destination:/topic/auction/${config.auction_id}\n\n` +
                        '\0'
                    );

                    const intervalId = socket.setInterval(() => {

                        const body = JSON.stringify({
                            userId: config.user.userId,
                            username: `${config.user.username}_${__VU}`,
                            amount: config.base_bid + Date.now()
                        });

                        socket.send(
                            'SEND\n' +
                            `destination:/app/place-bid/${config.auction_id}\n` +
                            'content-type:application/json\n' +
                            `content-length:${body.length}\n\n` +
                            body +
                            '\0'
                        );

                        bidsSent.add(1);

                    }, config.bid_interval_ms);

                    socket.setTimeout(() => {
                        socket.close();
                    }, parseDuration(config.duration));
                }
            });

            socket.on('error', (e) => {
                console.error(`VU ${__VU} ERROR: ${JSON.stringify(e)}`);
            });

            socket.on('close', () => {
                console.log(`VU ${__VU} closed`);
            });
        }
    );
}

function parseDuration(duration) {
    const value = parseInt(duration, 10);

    if (duration.endsWith('ms')) return value;
    if (duration.endsWith('s')) return value * 1000;
    if (duration.endsWith('m')) return value * 60 * 1000;
    if (duration.endsWith('h')) return value * 60 * 60 * 1000;

    throw new Error(`Unsupported duration: ${duration}`);
}