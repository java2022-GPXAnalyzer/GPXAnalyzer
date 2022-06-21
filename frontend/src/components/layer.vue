<template lang="">
  <div class="round-none border-2 border-black w-64 m-1 p-1">
    <p class="title">Map</p>
    <div
      class="m-auto justify-center flex-wrap flex overflow-y-scroll overflow-x-hidden"
      style="max-height: calc(100% - 33px)"
    >
      <button @click="addGPX" class="button border border-black">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M9 13h6m-3-3v6m5 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
          />
        </svg>
        <span>add GPX</span>
      </button>
      <div
        v-for="(map, index) in state.gpxMaps"
        :key="map.uuid"
        style="cursor: pointer"
        class="flex divide-y-2 divide-gray-500 mt-4 w-full"
      >
        <div
          class="display-button"
          @click="state.showLayer[index] = !state.showLayer[index]"
        >
          <button class="option" v-if="state.showLayer[index]">
            <svg
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
              ></path>
            </svg>
          </button>
          <button class="option" v-else>
            <svg
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M20 12H4"
              ></path>
            </svg>
          </button>
        </div>
        <ul>
          <li class="mapLayer" @click="selcetMap(index)">
            {{ map.name || `map${index + 1}` }}
          </li>
          <li>
            <Transition>
              <ul v-if="state.showLayer[index]">
                <li v-if="map.trkPoints.length > 0" class="child pointsLayer">
                  trkPoints
                </li>
                <li
                  v-for="(point, pointIndex) in map.trkPoints"
                  :key="point.uuid"
                  @click="selectPoint(index, point.uuid)"
                  class="child2 pointLayer"
                >
                  <span class="pointName">
                    {{ point.name || `trkPoint${pointIndex + 1}` }}
                  </span>
                </li>
                <li v-if="map.wayPoints.length > 0" class="child pointsLayer">
                  wayPoints
                </li>
                <li
                  v-for="(point, pointIndex) in map.wayPoints"
                  :key="state.updateKey + point.uuid"
                  @click="selectPoint(index, point.uuid)"
                  class="child2 pointLayer"
                >
                  {{ point.name || `wayPoint${pointIndex + 1}` }}
                </li>
              </ul>
            </Transition>
          </li>
        </ul>
        <div class="eye-button" @click="toggleMap(index)">
          <button class="option" v-if="state.showMap[index]">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
              />
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
              />
            </svg>
          </button>
          <button class="option" v-else>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, watch, ref } from 'vue';
import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const state = reactive({
  gpxMaps: [],
  showLayer: [],
  showMap: [],
  updateKey: 0,
});

let mapLength = 0;

watch(
  () => emi.state.gpxLength,
  (val) => {
    if (val === 0) return;
    if (mapLength < val) {
      let data = emi.gpxMaps;
      state.gpxMaps.push(data[val - 1]);
      state.showLayer.push(true);
      state.showMap.push(true);
      mapLength = val;
    }
  }
);

watch(
  () => emi.state.isPointChanged,
  (val) => {
    if (val === false) return;
    state.updateKey++;
    emi.state.isPointChanged = false;
    state.gpxMaps = emi.gpxMaps;
    console.log(state.gpxMaps.wayPoints);
  }
);

function toggleMap(index) {
  state.showMap[index] = !state.showMap[index];
  emi.toggleMapDisplay(index);
}

function selcetMap(index) {
  emi.selectMap(index, null);
}

function selectPoint(index, uuid) {
  emi.selectMap(index, uuid);
}
</script>
<style scoped>
.mapLayer {
  @apply hover:text-red-400 overflow-hidden;
  max-width: 120px;
  min-width: 100px;
}

.pointLayer {
  @apply hover:text-blue-400;
  max-width: 100px;
  min-width: 80px;
  margin-left: 30px;
  height: 24px;
}

.pointName {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.option {
  @apply text-xl font-sans p-0 ml-4 mr-1 items-start flex-col;
}

li {
  position: relative;
  list-style: none;
}

li.child::before {
  position: absolute;
  left: -1em;
  top: -0.2em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: 1px;
  border-bottom-color: black;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  content: '';
  width: 2.2em;
  height: 1.1em;
}

li.child2::before {
  position: absolute;
  left: -0.7em;
  top: -0.7em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: 1px;
  border-bottom-color: black;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  content: '';
  width: 1em;
  height: 1.5em;
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
  content: '';
  width: 1em;
  height: 3.5em;
}

.v-enter-active,
.v-leave-active {
  transition: opacity 0.2s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}

::-webkit-scrollbar-track {
  background-color: #ffffff;
}

::-webkit-scrollbar {
  width: 2px;
  background-color: black;
}

::-webkit-scrollbar-thumb {
  background-color: #000000;
}
</style>
