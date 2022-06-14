import { createApp } from 'vue'
import { route }  from '@/router/route'
import App from './App.vue'
import './css/index.css'
import 'cesium/index.css'


createApp(App)
  .use(route)
  .mount('#app')