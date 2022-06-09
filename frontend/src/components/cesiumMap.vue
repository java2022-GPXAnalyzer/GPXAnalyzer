<template>
  <div id="cesiumContainer" />
</template>
<script setup>
  // import * as Cesium from 'cesium';
  import { Viewer } from 'cesium'

  const viewer = new Viewer('cesiumContainer');

</script>
<style lang="">
  #cesiumContainer {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }
</style>