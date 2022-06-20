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
    <map-info></map-info>
  </div>
</template>
<script setup>
import cesiumMap from '@/components/cesiumMap.vue';
import toolBar from '@/components/toolBar.vue';
import mapInfo from '@/components/mapInfo.vue';
import layer from '@/components/layer.vue';
</script>
<style>
.button {
  @apply rounded-lg mx-2 my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out;
  display: flex;
}
.title {
  @apply text-2xl font-sans border-b-2 border-black;
}
.subtitle {
  @apply text-base font-sans flex;
}
.data {
  @apply text-base font-sans my-auto bg-slate-500 bg-opacity-50;
}
</style>
