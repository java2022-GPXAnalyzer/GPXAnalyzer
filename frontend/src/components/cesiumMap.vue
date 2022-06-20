<template>
  <div id="cesiumContainer"></div>
</template>

<script setup>
import * as Cesium from 'cesium';
import { onMounted, reactive, watch, ref } from 'vue';

import { eventManager } from '@/cesium/eventManager';

const emi = eventManager.getInstance();

var viewer;

// Cesium Setting
Cesium.Ion.defaultAccessToken =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJjNThmYjdjNi04MWU0LTQ3NDktOGVlMi05ZTVkNzExMWM0NGYiLCJpZCI6OTcxNDUsImlhdCI6MTY1NDkzMDE4NH0.1UyvbLY84Rmpj0IPPBHhyjEtqddTyz0Ovf0IhoKK0zA';

emi.showLoadingPercent(15, 'cesium loading...');

onMounted(() => {
  viewer = new Cesium.Viewer('cesiumContainer', {
    terrainProvider: Cesium.createWorldTerrain(),
    geocoder: false, //Hide search
    homeButton: false, //Hide back home
    sceneModePicker: false, //Hide view mode
    baseLayerPicker: false, //Hide layer section
    navigationHelpButton: false, //Hide help
    animation: false, //Hide animation controller
    timeline: false, //Hide timeline controller
    fullscreenButton: false, //Hide fullscreen button
    selectionIndicator: false, //Hide selection indicator
    infoBox: false, //Hide info box
  });

  // Cesium clock setting
  viewer.clock.clockRange = Cesium.ClockRange.LOOP_STOP;
  viewer.clock.multiplier = 1;
  viewer.clock.shouldAnimate = false;

  // Cesium version hidden
  viewer._cesiumWidget._creditContainer.style.display = 'none';
  viewer.forceResize();

  // Set Initial View in Taiwan
  viewer.camera.setView({
    destination: Cesium.Cartesian3.fromDegrees(121.0, 23.5, 550000.0),
  });

  emi.setViewer(viewer).buildGpxMaps().initializeHandler();
  emi.showLoadingPercent(60, 'cesium loading...');
});
</script>

<style>
#cesiumContainer {
  width: 70%;
  height: 100%;
  margin: auto;
  padding: 0;
  overflow: hidden;
}
</style>
