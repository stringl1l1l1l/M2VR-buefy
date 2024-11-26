const { defineConfig } = require('@vue/cli-service')

const port = import.meta.env.port || import.meta.env.npm_config_port || 5173 // dev port

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    //代理可以写多个，代理不同的后端地址
    proxy: {
      [import.meta.env.VUE_APP_BASE_API]: {
        target: import.meta.env.VUE_APP_BASE_API,
        changeOrigin: true,
        pathRewrite: {
          ['^' + import.meta.env.VUE_APP_BASE_API]: ''
        }
      }
    },
  }
});
