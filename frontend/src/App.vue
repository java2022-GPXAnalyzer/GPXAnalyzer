<template>
  <loading></loading>
  <alert></alert>
  <router-view></router-view>
</template> 

<script setup>
import alert from '@/components/alert.vue';
import loading from '@/components/loading.vue';
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
