<template lang="">
  <div
    class="border-2 rounded-lg border-blue-400 justify-center box-content w-2/3 min-w-fit mx-auto mt-1 flex"
  >
    <button @click="save" class="button">
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
          d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4"
        />
      </svg>
      <span>save</span>
    </button>

    <button @click="pause" class="button" v-if="state.start">
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
          d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
        />
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M9 10a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1v-4z"
        />
      </svg>
      <span>pause</span>
    </button>

    <button @click="start" class="button" v-else>
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
          d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"
        ></path>
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
        ></path>
      </svg>
      <span>start</span>
    </button>

    <button @click="reduceVelocity" class="ml-2 my-auto py-0">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-6 rounded-lg hover:bg-red-400 hover:text-white transition duration-500 ease-in-out"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M18 12H6" />
      </svg>
    </button>
    <span class="my-3 py-2 mx-2 px-2 flex">
      <!-- {{ state.velocity }} -->
      <span>velocity</span>
      <input
        name="velocity"
        class="ml-1 w-10 flex text-center border-2 border-gray-500"
        type="text"
        v-model.number="state.velocity"
      />
    </span>
    <button @click="addVelocity" class="mr-2 my-auto py-0">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-6 rounded-lg hover:bg-red-400 hover:text-white transition duration-500 ease-in-out"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M12 6v6m0 0v6m0-6h6m-6 0H6"
        />
      </svg>
    </button>

    <button @click="movementRateDistributionMap" class="button">
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
          d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
        />
      </svg>
      <span>velocity map</span>
    </button>

    <button @click="large" class="button">
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
          d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4"
        />
      </svg>
      <span>large</span>
    </button>

    <button @click="reset" class="button">
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
          d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
        />
      </svg>
      <span>reset</span>
    </button>

    <button @click="zoomIn" class="button">
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
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7"
        />
      </svg>
      <span>zoom in</span>
    </button>

    <button @click="zoomOut" class="button">
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
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7"
        />
      </svg>
      <span>zoom out</span>
    </button>
    <button @click="changeMap" class="button">
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
          d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"
        ></path>
      </svg>
      <span>change Map</span>
    </button>
    <button @click="HotSpot" class="button">
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
          d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
        ></path>
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
        ></path>
      </svg>
      <span>HotSpot</span>
    </button>
  </div>
</template>
<script setup>
import { reactive, watch } from 'vue';
import { eventManager } from '@/cesium/eventManager';

const emi = eventManager.getInstance();

const state = reactive({
  velocity: 1,
  start: false,
  large: false,
  reset: false,
});

function _isNotANumber(inputData) {
  if (parseFloat(inputData).toString() === 'NaN') {
    return false;
  } else {
    return true;
  }
}

watch(
  () => state.velocity,
  (val) => {
    if (val > 9999) {
      state.velocity = 9999;
    } else if (val < -999) {
      state.velocity = -999;
    }
    if (_isNotANumber(val)) {
      emi.setTrakingSpeed(val);
    }
  }
);

function save() {
  emi.saveGpxMaps();
}

function start() {
  let res = emi.beginTracking();
  state.start = res === true;
}

function pause() {
  emi.stopTracking();
  state.start = false;
}

function reduceVelocity() {
  state.velocity -= 1;
  if (state.velocity == 0) state.velocity = -1;
}

function addVelocity() {
  state.velocity = parseInt(state.velocity) + 1;
  if (state.velocity == 0) state.velocity = 1;
}

function movementRateDistributionMap() {
  emi.toggleSpeedDistribution();
}

function large() {
  emi.fullScreen();
}

function reset() {
  emi.backToHomeView();
}

function zoomIn() {
  emi.zoomIn();
}

function zoomOut() {
  emi.zoomOut();
}

let map = 0;

function changeMap() {
  map = (map + 1) % 2;
  emi.changeMap(map);
}

function HotSpot() {
  emi.toggleHotSpot();
}
</script>
<style></style>
