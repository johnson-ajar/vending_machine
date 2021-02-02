const path = require('path');
const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const baseConfig = require('./webpack.config.base');

const buildFolder = '../dist';
const basePath = __dirname;

function buildConfig(env) {
  console.log("Building for environment : "+env);
  return merge(baseConfig, {
  mode: env,
  output: {
    path: path.resolve(basePath, buildFolder),
    filename: '[name].bundle.js',
    publicPath: '/'
  },
  devtool: 'inline-source-map',
  devServer: {
    contentBase: path.resolve(basePath, buildFolder)
  },
 
  plugins: [
    new HtmlWebpackPlugin({
      title: env,
      filename: 'index.html',
      template: path.resolve(basePath, '../')+'/index.html',
      hash: true,
      inject: 'body'
    }),
    new CleanWebpackPlugin(['./dist'], {
    	root : path.resolve(basePath, '../')
    })
  ]
});
}

module.exports = buildConfig;
