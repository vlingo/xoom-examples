import sirv from 'sirv';
import polka from 'polka';
import compression from 'compression';
import * as sapper from '@sapper/server';
const { json } = require('body-parser');
import { createProxyMiddleware } from 'http-proxy-middleware';

const { PORT, NODE_ENV } = process.env;
const dev = NODE_ENV === 'development';

polka() // You can also use Express
	.use("/aircrafts/", createProxyMiddleware({ target: "http://localhost:18080", changeOrigin: true }))
	.use("/flight-plannings/", createProxyMiddleware({ target: "http://localhost:18081", changeOrigin: true }))
	.use("/fleetcrew/aircrafts/", createProxyMiddleware({ target: "http://localhost:18082", changeOrigin: true }))
	.use("/flights/", createProxyMiddleware({ target: "http://localhost:18083", changeOrigin: true }))
	.use(json())
	.use(
		compression({ threshold: 0 }),
		sirv('static', { dev }),
		sapper.middleware()
	)
	.listen(PORT, err => {
		if (err) console.log('error', err);
	});
