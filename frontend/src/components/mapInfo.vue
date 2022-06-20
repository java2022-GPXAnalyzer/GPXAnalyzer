<template lang="">
  <div class="round-none border-2 border-black">
    <div class="title flex justify-center">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-6 w-6 m-1"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
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
    <button
      @click="addWayPoint"
      class="flex rounded-lg mx-auto my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out border border-black m-x-auto"
    >
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
          d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
      <span>add way point</span>
    </button>

    <div class="title flex justify-center border-t-2 border-black">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-6 w-6 m-1"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
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
    <button
      @click="addWayPoint"
      class="flex rounded-lg mx-auto my-3 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out border border-black m-x-auto"
    >
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
          d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
      <span>remove</span>
    </button>
  </div>
</template>
<script setup>
import { reactive } from 'vue';

const state = reactive({
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

</script>
<style></style>
