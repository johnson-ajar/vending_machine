
function buildConfig(env) {
  if(env == 'development') {
	 console.log("Running the development webpack script");
    return require('./scripts/webpack.config.dev.js')(env);
  } else if (env == 'production') {
    return require('./scripts/webpack.config.prod.js')(env);
  }
}

module.exports = buildConfig;