<template>
  <div id="cesiumContainer"></div>
</template>
<script setup>
  import { Viewer } from 'cesium';
  import { onMounted } from 'vue';

  onMounted(() => {
    const viewer = new Viewer('cesiumContainer');
    console.log(viewer)
  })

</script>
<style>
  #cesiumContainer {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }
</style>