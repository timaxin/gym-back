const middleware = () => {
    return {
        name: 'middleware',
        apply: 'serve',
        configureServer(viteDevServer) {
            return () => {
                viteDevServer.middlewares.use(async (req, res, next) => {
                    if (req.originalUrl.endsWith(".html") && req.originalUrl !== "/") {
                        req.url = `/src/html` + req.originalUrl;
                    }
                    // else if (req.url === "/index.html") {
                    //     req.url = `/src` + req.url;
                    // }

                    next();
                });
            };
        }
    }
}



//todo nginx same
//todo error handling / events
export default {
    plugins:[middleware()]
}
