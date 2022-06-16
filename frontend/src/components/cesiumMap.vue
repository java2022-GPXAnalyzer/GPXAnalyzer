<template>
  <div>
    <div class="border-2 rounded-lg border-blue-400 justify-center box-content w-2/3 mx-auto mt-1">
      <button @click="track" class="button">track</button>
      <button @click="begin" class="button">begin</button>
      <button @click="stop" class="button">stop</button>
      <button @click="reset" class="button">reset</button>
      <button @click="addGPXFromJson" class="button">add</button>
      <button @click="test" class="button">test</button>
      <button><img src="../assets/logo.png" class="mt-auto py-auto px-auto w-10 rounded border-2 border-blue-400"></button>
    </div>
    <p>{{ state.mode }}</p>
    <div class="container flex justify-evenly">
      <div class="w-28 h-full round-none border-2 border-black">
        <p class="title">Map</p>
        <div>
          <button @click="openLayer" class="layer">Main layer</button>
        </div>
        <!-- <layer></layer> -->
      </div>

      <div id="cesiumContainer"></div>
      
      <div class="w-28 h-full round-none border-2 border-black">
        <p class="title">MapInfo</p>
        <div class="flex-col divide-y-2">
          <div class="subtitle">
            <p>Name</p>
            <span class="data">{{ state.mode }}</span>
          </div>
          <div class="subtitle">
            <p>StartTime</p>
            <span class="data">{{ state.mode }}</span>
          </div>
          <div class="subtitle">
            <p>EndTime</p>
            <span class="data">{{ state.mode }}</span>
          </div>
          <div class="subtitle">
            <p>Creator</p>
            <span class="data">{{ state.mode }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as Cesium from 'cesium';
import { onMounted, reactive } from 'vue';

import { eventManager } from '@/cesium/eventManager';
// import { layer } from '@/cesium/layer';

const state = reactive({
  name: '',
  mode: 0,
});

const emi = eventManager.getInstance();

var viewer;

// Cesium Setting
Cesium.Ion.defaultAccessToken =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJjNThmYjdjNi04MWU0LTQ3NDktOGVlMi05ZTVkNzExMWM0NGYiLCJpZCI6OTcxNDUsImlhdCI6MTY1NDkzMDE4NH0.1UyvbLY84Rmpj0IPPBHhyjEtqddTyz0Ovf0IhoKK0zA';

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
}

function test() {
}

function openLayer() {
  
}
</script>

<style>
.container {
  width: 100%;
  height: calc(100vh - 60px);
  margin: 20px auto 0;
}
#cesiumContainer {
  width: 80%;
  height: 100%;
  margin: auto;
  padding: 0;
  overflow: hidden;
}
.button {
  @apply m-3 py-2 px-4 hover:bg-red-400 hover:text-white
}
.title {
  @apply text-2xl font-sans border-b-2 border-black
}
.subtitle {
  @apply text-xl font-sans flex-col
}
.data {
  @apply text-xl font-sans my-auto
}
.layer {
  @apply font-sans rounded-md border-2 border-black m-4 hover:bg-black hover:text-white
}
</style>
