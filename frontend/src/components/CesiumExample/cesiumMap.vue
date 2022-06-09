<template>
  <div class="map-box">
    <div id="cesiumContainer"></div>
  </div>
</template>

<script setup> 
import { Viewer } from 'cesium'
var viewer = new Viewer("cesiumContainer");

// eslint-disable-next-line no-console
console.log(viewer)
</script> 
<style scoped>
  .map-box{
    width: 100%;
    height: 100%;
  }
  #cesiumContainer{
    width: 100%;
    height: 100%;
  }
</style>
