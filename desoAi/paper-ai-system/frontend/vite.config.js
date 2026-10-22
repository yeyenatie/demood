import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 监听所有网卡，允许局域网内其他设备通过本机 IP 访问
    host: true,
    port: 5173,
    // 开发环境代理：/api 开头的请求转发到 Spring Boot 后端
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
