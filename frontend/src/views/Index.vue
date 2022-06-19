<template>
  <div>
    <button 
      @click="backToInit" 
      class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">back</button>
    <cesium-map />
  </div>
</template>
<script setup>
  import { useRouter, useRoute } from 'vue-router'
  import cesiumMap from '@/components/cesiumMap.vue'

  const router = useRouter();
  
  function backToInit() {
    router.push('/');
  }
</script>
<style lang="">
  
</style>