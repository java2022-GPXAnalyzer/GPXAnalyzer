<template>
  <div>
    <h1>test nav</h1>
    <router-link to="/" class="ml-5">init</router-link>
    <router-link to="/index" class="ml-5">index</router-link>
  </div>
  <router-view></router-view>
</template>

<script setup>
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
