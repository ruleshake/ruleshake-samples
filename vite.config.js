import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path';

export default ({ mode }) => {
  return defineConfig({
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
