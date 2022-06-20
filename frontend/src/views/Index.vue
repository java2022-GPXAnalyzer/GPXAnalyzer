<template>
  <!-- top bar -->
  <tool-bar></tool-bar>
  <!-- container -->
  <div style="height: calc(100vh - 98px)" class="mt-5 flex justify-between">
    <!-- layer -->
    <layer></layer>
    <!-- map -->
    <cesium-map />
    <!-- map info -->
    <info-component></info-component>
  </div>
</template>
<script setup>
import cesiumMap from '@/components/cesiumMap.vue';
import toolBar from '@/components/toolBar.vue';
import infoComponent from '@/components/infoComponent.vue';
import layer from '@/components/layer.vue';
</script>
<style>
.button {
  @apply flex rounded-lg mx-2 my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out;
}
.title {
  @apply text-2xl font-sans border-b-2 border-black;
}
</style>
