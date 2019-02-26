const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
  mode: 'development',
  devtool: 'source-map',
  entry: __dirname + "/src/index.js",
  output: {
    path: __dirname + "/dist",
    filename: "index_bundle.js"
  },
  devServer: {
    contentBase: path.join(__dirname, 'dist'),
    compress: true,
    port: 3000
  },
  resolve: { 
    extensions: ['.js', '.json'],
    alias: {
      '@configs': path.join(__dirname, '/src/configs'),
    }
  },
  module: {
    rules: [{
      test: /(\.jsx|\.js)$/,
      use: {
        loader: "babel-loader",
      },
      exclude: /node_modules/
    }, {
      test: /\.css$/,
      use: [{
        loader: "style-loader"
      }, {
        loader: "css-loader"
      }, {
        loader: "postcss-loader"
      }]
    }]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: __dirname + "/public/temp.html"
    })
  ]
}