<template>
<<<<<<< HEAD
  <div class="h-screen w-screen flex">
    <div class="container my-auto mx-auto inline-block w-128">
      <!-- <img class="h-64 w-64" alt="Vue logo" src="./assets/map.png" /> -->
      <div class="text-3xl font-bold mt-3 inline-block">GPX Analyzer <br>(用於測試後端，並非正式版前端)</div>
      <div class="my-3">
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="testAPI">testAPI Button</button>
      </div>
      <h2>{{message}}</h2>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      message: ''
    }
  },
  methods: {
    testAPI() {
      this.$http.get('/api/test').then(response => {
        console.log(response.data);
        this.message = response.data
      });
    },
  },
}
=======
  <loading></loading>
  <alert></alert>
  <router-view></router-view>
</template>

<script setup>
import alert from '@/components/alert.vue';
import loading from '@/components/loading.vue';
>>>>>>> jimmy_dev
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
