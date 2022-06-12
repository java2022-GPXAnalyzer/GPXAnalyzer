import { createApp } from 'vue'
import { route }  from '@/router/route'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'
import './css/index.css'
import 'cesium/index.css'


createApp(App)
  .use(route)
  .use(VueAxios, axios)
  .mount('#app')
