<template>
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
    HotSpot
  </div>
  <div class="px-3 pt-3 overflow-x-hidden overflow-y-scroll" style="max-height: calc(100% - 30px);" v-if="state.show">
    <div class="relative z-0 my-2" v-for="(point, index) in state.points" :key="point.uuid">
      <input
        type="text"
        id="name_input"
        :value="`${point.lat}, ${point.lon}`"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
      />
      <label
        for="name_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >HotSpot{{index + 1}}</label
      >
    </div>    
  </div>
</template>
<script setup>
import { computed, reactive } from 'vue';

import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const state = reactive({
  show: false,
  points: null
});

emi.getHotSpot().then((res) => {
  state.show = true;
  state.points = res;
  emi.addHotSpotPoints(res);
})

</script>
<style scoped>
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