<<<<<<< HEAD
import { createApp } from 'vue'
import App from './App.vue'
import './css/index.css'
import axios from 'axios'
import VueAxios from 'vue-axios'

const app = createApp(App)
app.use(VueAxios, axios)

app.mount('#app')
=======
import { createApp } from 'vue';
import { route } from '@/router/route';
import App from './App.vue';
import './css/index.css';
import 'cesium/index.css';

createApp(App).use(route).mount('#app');
>>>>>>> jimmy_dev
