<template>
  <button @click="test">{{ value }} + 1000</button>
  <cesiumMap />
</template>
<script setup>
  import cesiumMap from '@/components/CesiumExample/cesiumMap.vue'
  import { ref } from 'vue' 

  const value = ref(2000)
  const test = () => {
    value.value += 1000
  }
</script>
<style lang="">
  
</style>