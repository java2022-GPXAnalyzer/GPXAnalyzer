<template>
  <div>

    <!-- top bar -->
    <div class="border-2 rounded-lg border-blue-400 justify-center box-content w-2/3 min-w-fit mx-auto mt-1 flex">

      <button @click="save" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4" /></svg>
        <span>save</span>
      </button>

      <button @click="pause" class="button" v-if="state.start">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M9 10a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1v-4z" /></svg>
        <span>pause</span>
      </button>

      <button @click="start" class="button" v-else>
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <span>start</span>
      </button>

      <button @click="reduceVelocity" class="ml-2 my-auto py-0">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 rounded-lg hover:bg-red-400 hover:text-white transition duration-500 ease-in-out" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M18 12H6" /></svg>
      </button>
      <span class="my-3 py-2 mx-2 px-2 flex">
        <!-- {{ state.velocity }} -->
        <span>velocity</span>
        <input name="velocity" class="ml-1 w-5 flex text-center border-2 border-gray-500" type="text" v-model="state.velocity" v-on:keyup.enter="enterVelocity(velocity)">
      </span>
      <button @click="addVelocity" class="mr-2 my-auto py-0">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 rounded-lg hover:bg-red-400 hover:text-white transition duration-500 ease-in-out" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6m0 0v6m0-6h6m-6 0H6" /></svg>
      </button>

      <button @click="movementRateDistributionMap" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
        <span>velocity map</span>
      </button>

      <button @click="large" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4" /></svg>
        <span>large</span>
      </button>

      <button @click="reset" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" /></svg>
        <span>reset</span>
      </button>

      <button @click="zoomIn" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7" /></svg>
        <span>zoom in</span>
      </button>

      <button @click="zoomOut" class="button">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7" /></svg>
        <span>zoom out</span>
      </button>
    </div>

    <!-- container -->
    <div style="height: calc(100vh - 93px);" class="mt-5 flex justify-between">

      <!-- layer -->
      <div class="round-none border-2 border-black">
        <p class="title">Map</p>

        <button @click="addGPX" class="button border border-black">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 13h6m-3-3v6m5 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
          <span>add GPX</span>
        </button>
        
        <div v-for="(map, index) in tmpMap.maps" :key="index" style="cursor:pointer;" class="flex divide-y-2 divide-gray-500">
          <!-- open layer -->
          <button @click="closeMap(index)" class="eye" v-if="tmpMap.maps[index].showLayer">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
          </button>
          <!-- close layer -->
          <button @click="openMap(index)" class="eye" v-else>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" /></svg>
          </button>
          <ul>
            <li @click="map.show = !map.show" :class="{openLayer: tmpMap.maps[index].showLayer}">
              {{ map.name }}
            </li>

            <Transition>
            <ul v-if="map.show">
              <li v-for="(point, pointIndex) in map.points" :key="pointIndex" :class="{openSubLayer: tmpMap.maps[index].showLayer}" class="child" @click="test(index, pointIndex)">
                {{point.uuid}}
              </li>
            </ul>
            </Transition>
          </ul>
        </div>
      </div>

      <!-- map -->
      <div id="cesiumContainer"></div>
      
      <!-- map info -->
      <div class="round-none border-2 border-black">
        <div class="title flex justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 m-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
          MapInfo
        </div>

        <div class="flex-col divide-y-2">
          <div class="subtitle">
            <p>Map Name:</p>
            <span class="data">{{ state.mapName }}</span>
          </div>

          <div class="subtitle">
            <p>Map Start Time:</p>
            <span class="data">{{ state.mapST }}</span>
          </div>

          <div class="subtitle">
            <p>Map End Time:</p>
            <span class="data">{{ state.mapET }}</span>
          </div>

          <div class="subtitle">
            <p>Creator:</p>
            <span class="data">{{ state.mapCreator }}</span>
          </div>

        </div>
        <button @click="addWayPoint" class="flex rounded-lg mx-auto my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out border border-black m-x-auto">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
          <span>add way point</span>
        </button>

        <div class="title flex justify-center border-t-2 border-black">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 m-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
          PointInfo
        </div>
        <div class="flex-col divide-y-2">
          <div class="subtitle">
            <p>Point Uuid:</p>
            <span class="data">{{ state.pointUuid }}</span>
          </div>

          <div class="subtitle">
            <p>Point Lng:</p>
            <span class="data">{{ state.pointLng }}</span>
          </div>

          <div class="subtitle">
            <p>Point Lat:</p>
            <span class="data">{{ state.pointLat }}</span>
          </div>

          <div class="subtitle">
            <p>Point Ele:</p>
            <span class="data">{{ state.pointEle }}</span>
          </div>

          <div class="subtitle">
            <p>Point Time:</p>
            <span class="data">{{ state.pointTime }}</span>
          </div>
        </div>
        <button @click="addWayPoint" class="flex rounded-lg mx-auto my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out border border-black m-x-auto">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
          <span>remove</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as Cesium from 'cesium';
import { onMounted, reactive, watch, ref } from 'vue';

import { eventManager } from '@/cesium/eventManager';
// import { layer } from '@/cesium/layer';

const state = reactive({
  mode: 0,
  start: false,
  velocity: 5,
  mapName: '',
  mapST: '',
  mapET: '',
  mapCreator: '',
  pointUuid: '',
  pointLng: '',
  pointLat: '',
  pointEle: '',
  pointTime: '',
});

const tmpMap = reactive({
  maps: [
    {
      points: [
        {
          uuid: 'sdgs',
          lng: '120.178',
          lat: '23.566',
          ele: '5550',
          time: '2020-01-01T00:00:00Z',
        },
        {
          uuid: 'sdgs2',
          lng: '120.978',
          lat: '24.566',
          ele: '5550',
          time: '2020-01-01T00:00:05Z',
        },
        {
          uuid: 'sdgs3',
          lng: '121.578',
          lat: '24.5166',
          ele: '5550',
          time: '2020-01-01T00:00:10Z',
        },
      ],
      show: false,
      showLayer: false,
      uuid: 'testMap',
      name: 'testMap',
      startTime: '2020-01-01T00:00:00Z',
      endTime: '2020-01-01T00:00:10Z',
      creator: 'test',
    },
    {
      points: [
        {
          uuid: 'sdgs',
          lng: '120.178',
          lat: '23.566',
          ele: '5550',
          time: '2020-01-01T00:00:00Z',
        },
        {
          uuid: 'sdgs2',
          lng: '120.978',
          lat: '24.566',
          ele: '5550',
          time: '2020-01-01T00:00:05Z',
        },
      ],
      show: false,
      uuid: 'testMap2',
      name: 'testMap2',
      startTime: '2020-01-01T00:00:00Z',
      endTime: '2020-01-01T00:00:10Z',
      creator: 'test',
    },
  ] 
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

function start() {
  viewer.clock.shouldAnimate = true;
  state.start = true;
}

function pause() {
  viewer.clock.shouldAnimate = false;
  state.start = false;
}

function reduceVelocity() { 
  if(state.velocity)
    state.velocity -= 1;
  else
    state.velocity = 5;
}

function enterVelocity(velocity) {
  if(velocity) {
    state.velocity = velocity;
  }
}

function addVelocity() {
  if(state.velocity)
    state.velocity += 1;
  else
    state.velocity = 5;
}

function movementRateDistributionMap() {
}

function reset() {
}

function zoomIn() {
  viewer.camera.zoomIn();
}

function zoomOut() {
  viewer.camera.zoomOut();
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

function test(mapIndex, pointIndex) {
  let mapData = tmpMap.maps[mapIndex];
  let pointData = tmpMap.maps[mapIndex].points[pointIndex];
  state.mapName = mapData.name;
  state.mapST = mapData.startTime;
  state.mapET = mapData.endTime;
  state.mapCreator = mapData.creator;
  state.pointUuid = pointData.uuid;
  state.pointLng = pointData.lng;
  state.pointLat = pointData.lat;
  state.pointEle = pointData.ele;
  state.pointTime = pointData.time;
}

function openLayer() {}

function closeMap(index) {
  tmpMap.maps[index].showLayer = false;
}

function openMap(index) {
  tmpMap.maps[index].showLayer = true;
}
function addGPX() {
  emi.addGPX();
}
function addWayPoint() {
  emi.addWayPoint();
}
</script>

<style>
.container {
  width: 100%;
  height: calc(100vh - 93px);
  /* margin: 20px auto 0 */
  margin-top: 20px;
  margin-right: 0px;
}
#cesiumContainer {
  width: 70%;
  /* 80% */
  height: 100%;
  margin: auto;
  padding: 0;
  overflow: hidden;
}
.button {
  @apply rounded-lg mx-2 my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out;
  display: flex;
}
.title {
  @apply text-2xl font-sans border-b-2 border-black
}
.subtitle {
  @apply text-base font-sans flex
}
.data {
  @apply text-base font-sans my-auto bg-slate-500 bg-opacity-50
}
.openLayer {
  @apply hover:text-red-400
}
.openSubLayer {
  @apply hover:text-blue-400
}
.eye {
  @apply text-xl font-sans p-0 mt-1 ml-4 mr-2 items-start flex-col
}
.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
li {
  position: relative;
  list-style: none;
}

li.child::before {
  position: absolute;
  left: -1em;
  top: -0.5em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: 1px;
  border-bottom-color: black;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  content: "";
  width: 1em;
  height: 1.1em;
}

li.child2::before {
  position: absolute;
  left: -1em;
  top: -2em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: 1px;
  border-bottom-color: black;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  content: "";
  width: 1em;
  height: 2.5em;
}

li.child3::before {
  position: absolute;
  left: -1em;
  top: -3em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: 1px;
  border-bottom-color: black;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  content: "";
  width: 1em;
  height: 3.5em;
}

body {
  background: white;
}
</style>
