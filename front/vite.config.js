import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path';

export default ({ mode }) => {
  return defineConfig({
    server: {
      proxy: {
        '/api': {
          target: 'http://localhost:8084',
          changeOrigin: true,
          ws: true,
          secure: false
        }
      }
    },
    build: {
      emptyOutDir: true,
      manifest: true,
      minify: false,
      polyfillModulePreload: true,
      sourcemap: true,
      ssr: false,
      target: 'es2018'
    },
    plugins: [vue()],
    resolve: {
      alias: {
        '@': resolve(__dirname, './src')
      },
    },
  })
}
