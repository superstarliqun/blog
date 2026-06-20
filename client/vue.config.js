const path = require('path')
const fs = require('fs')

// 是否是线上环境
const PRO_MODE = process.env.NODE_ENV === 'production'

const CompressionPlugin = require('compression-webpack-plugin')

// 计算打包版本
const setPackageJsonVersion = () => {
  const pkgPath = path.join(__dirname, './package.json')
  let pkg = fs.readFileSync(pkgPath)
  pkg = JSON.parse(pkg)
  const arr = pkg.version.split('.')
  const str = Number(arr[2]) + 1
  if (str === 10) {
    arr[2] = '0'
    if (arr[1] === 9) {
      arr[1] = '0'
      arr[0] = Number(arr[0]) + 1
    } else {
      arr[1] = Number(arr[1]) + 1
    }
  } else {
    arr[2] = String(str)
  }
  pkg.version = arr.join('.')
  fs.writeFileSync(pkgPath, JSON.stringify(pkg, null, 2))
}

module.exports = {
  runtimeCompiler: true, // 开启运行时编译器，支持template选项
  // 服务器路径
  publicPath: PRO_MODE ? '/' : '/',
  // 打包文件名
  outputDir: process.env.outPutDir,
  // 打包时是否添加 .map文件
  productionSourceMap: false,
  // 静态资源输出目录
  // assetsDir: 'assets',
  /* css预处理 */
  css: {
    loaderOptions: {
      sass: {
        additionalData: "@use '~@/assets/css/variables' as *;"
      },
      scss: {
        additionalData: "@use '~@/assets/css/variables' as *;"
      }
    }
  },
  configureWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {
      // 为生产环境修改配置...
      config.mode = 'production'
      config['performance'] = {
        // 打包文件大小配置
        maxEntrypointSize: 10000000,
        maxAssetSize: 30000000
      }
    }
    config.module.rules.push({
      test: /\.js$/,
      exclude: /node_modules(?!\/marked)/,
      use: {
        loader: 'babel-loader',
        options: {
          presets: ['@babel/preset-env']
        }
      }
    })
  },
  /* webpack配置 */
  chainWebpack: (config) => {
    config.plugin('html').tap((options) => {
      options[0].title = '灵光一刻'
      return options
    })
    config.plugin('define').tap((options) => {
      if (process.env.NODE_ENV === 'production') setPackageJsonVersion()
      return options
    })
    // 生产环境，开启js\css压缩
    config.plugin('compressionPlugin').use(
      new CompressionPlugin({
        test: /\.(js|css|less|map)$/, // 匹配文件名
        threshold: 1024, // 对超过10k的数据压缩
        minRatio: 0.8
      })
    )
  },
  devServer: {
    host: '0.0.0.0',
    client: {
      overlay: false
    },
    hot: true,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BASE_API,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
