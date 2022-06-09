import { defineConfig } from 'vite'
import cesium from 'vite-plugin-cesium'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  base: './',
  plugins: [vue(), cesium()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  }
})
