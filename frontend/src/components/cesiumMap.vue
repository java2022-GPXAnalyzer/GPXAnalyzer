<template>
  <button @click="track" class="m-3">track</button>
  <button @click="begin" class="m-3">begin</button>
  <button @click="stop" class="m-3">stop</button>
  <button @click="reset" class="m-3">reset</button>
  <button @click="add" class="m-3">add</button>
  <button @click="test" class="m-3">test</button>
  <p>{{ state.mode }}</p>
  <div class="container">
    <div id="cesiumContainer"></div>
  </div>
</template>

<script setup>
import * as Cesium from 'cesium';
import { onMounted, reactive, watch, ref } from 'vue';

import { eventManager } from '@/cesium/eventManager';

const state = reactive({
  name: '',
  mode: 0,
});

const emi = eventManager.getInstance();

var viewer;

// Cesium Setting
Cesium.Ion.defaultAccessToken =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJjNThmYjdjNi04MWU0LTQ3NDktOGVlMi05ZTVkNzExMWM0NGYiLCJpZCI6OTcxNDUsImlhdCI6MTY1NDkzMDE4NH0.1UyvbLY84Rmpj0IPPBHhyjEtqddTyz0Ovf0IhoKK0zA';

const states = ref({
  maps : null,
});

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

  emi.setViewer(viewer)
    .buildGpxMaps()
    .initializeHandler();
  emi.showLoadingPercent(60, 'cesium loading...');
});

// TEST
function track() {
  // Track
  viewer.trackedEntity = undefined;
  // addGPXPoint(0, {
  //   lng: '123.978',
  //   lat: '24.566',
  //   alt: '5550',
  // });
  // let tmp = mapsData[0];
  // tmp.polyline.material = Cesium.Color.RED;
}

function begin() {
  viewer.clock.shouldAnimate = true;
}

function stop() {
  viewer.clock.shouldAnimate = false;
}

function reset() {
  viewer.clock.currentTime = startTime.clone();
}

function add() {
  // console.log()
  // if (state.mode === 0) {
  //   state.mode = 1;
  //   return;
  // }
  // if (state.mode === 1) {
  //   state.mode = 2;
  //   return;
  // }
  // state.mode = 0;
  emi.toggleSpeedDistribution(0);
}

function test() {
  // emi.toggleHover(0);
  // emi.showalertMessage('test');
  emi.showAlertMessage(1, 'test');
  // emi.test();
}
</script>

<style>
.container {
  width: 100%;
  height: calc(100vh - 93px);
  margin: 20px auto 0;
}
#cesiumContainer {
  width: 80%;
  height: 100%;
  margin: auto;
  padding: 0;
  overflow: hidden;
}
</style>
