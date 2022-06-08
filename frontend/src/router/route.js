import { createRouter, createWebHashHistory } from 'vue-router';

import Init from '@/views/Init.vue';
import Index from '@/views/Index.vue';

const routes =  [
  {
    path: '/',
    name: 'init',
    component: Init,
  },
  {
    path: '/index',
    name: 'index',
    component: Index,
  }
]

export const route = createRouter({
  history: createWebHashHistory(),
  routes
});