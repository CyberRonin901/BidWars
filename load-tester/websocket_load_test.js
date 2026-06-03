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

            successfulConnections.add(1);

            socket.on('open', () => {

                console.log(`VU ${__VU} connected`);

                // STOMP CONNECT
                socket.send(
                    'CONNECT\n' +
                    'accept-version:1.2\n' +
                    'heart-beat:10000,10000\n' +
                    `Authorization:Bearer ${config.jwt_token}\n\n` +
                    '\0'
                );
            });

            socket.on('message', (msg) => {

                // Wait for STOMP CONNECTED before subscribing/sending bids
                if (msg.includes('CONNECTED')) {

                    socket.send(
                        `SUBSCRIBE
id:sub-${__VU}
destination:/topic/auction/${config.auction_id}

\0`
                    );

                    const intervalId = socket.setInterval(() => {

                        const amount = config.base_bid + Date.now();;

                        socket.send(
                            `SEND
destination:/app/place-bid/${config.auction_id}
content-type:application/json

${JSON.stringify({
                                userId: config.user.userId,
                                username: `${config.user.username}_${__VU}`,
                                amount: amount
                            })}
\0`
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