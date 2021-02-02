var path = require('path');


// let ExtractTextPlugin = require('extract-text-webpack-plugin');
// let CopyWebpackPlugin = require('copy-webpack-plugin');

const basePath = __dirname;
const srcFolder = '../src';
console.log(basePath)
module.exports = {
  
  context: path.resolve(basePath, srcFolder),
  entry: {
    app: './index.tsx',
    //appStyles: ['./styles/app.css'],
    vendor: [
      'react',
      'react-dom',
      //'react-router-dom',
      //'react-router',
      //'toastr',
      //'lc-form-validation'
    ]
   // vendorStyles: []
  },
  //If you want a static file name for output, then replace HtmlWebpackPlugin with a CopyWebpackPlugin
  //to copy the index.html into the dist folder.
  
  output: {
   //This property is used to set the location of webpack bundled js in index.html. 
   //This location should contain the same name as the context path in application.properties.
   publicPath: '/vending'
  },
  
  //Avoid inline-*** and eval-*** use in production as they can increase bundle size and reduce
  //the overall performance.
  //source-map creates *.map file in the build folder.
  //inline-source-map doesn't create *.map in the build folder.
  // devtool: 'inline-source-map',
 //This is used by webpack-dev-server, which is a simple web server and provides the ability to use live reloading.
 devServer: {
  //This tells the webpack-dev-server to serve the files from the dist directory on localhost:8082
  port: 8081,
  noInfo: true,
  proxy: {
    /*'/': {
      target: 'http://localhost:3001',
      secure: false
    }*/
  }
},
  resolve: {
    //Add '.ts' and '.tsx' as resolvable extensions.
    extensions: ['.ts', '.jsx','.tsx','.js','.json','.ejs']
  },
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        exclude: /(node_modules|bower_components)/,
        loader: 'ts-loader'
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.scss$/,
        use: ['style-loader','css-loader','sass-loader']
      },
      {
        test: /\.exec\.js$/,
        loader: 'script-loader'
      },
      //Json file doesn't need loader for webpack > 2.9 its inbuilt.
      //Svg files are loaded using svg-react-loader don't need to declare
      //loader.
      {
        test: /\.(png|jpe?g|gif)$/i,
        use: [
          {loader: 'url-loader?name=assets/icons/**/[name].[ext]'}
        ]
        //TODO: Include images in a folder and add this to the path.
        // include: imagePath
      }
    ]
  }
 
}