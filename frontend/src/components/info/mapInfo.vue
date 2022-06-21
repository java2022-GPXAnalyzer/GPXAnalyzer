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
    MapInfo
  </div>
  <div class="px-3 pt-3">
    <!-- <div class="relative">
      <input
        type="text"
        id="uuid_input"
        :value="state.mapData.uuid"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        readonly
      />
      <label
        for="uuid_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >UUID</label
      >
    </div> -->
    <div class="relative z-0 my-2">
      <input
        type="text"
        id="name_input"
        v-model="state.mapData.name"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
      />
      <label
        for="name_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >Name</label
      >
    </div>
    <div class="relative z-0 my-2">
      <input
        type="text"
        id="startTime_input"
        :value="formatData(state.mapData.startTime)"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        readonly
      />
      <label
        for="startTime_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >Start Time</label
      >
    </div>

    <div class="relative z-0 my-2">
      <input
        type="text"
        id="endTime_input"
        :value="formatData(state.mapData.endTime)"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        readonly
      />
      <label
        for="endTime_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >End Time</label
      >
    </div>
    <div class="relative z-0 my-2">
      <input
        type="text"
        id="creator_input"
        v-model="state.mapData.creator"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
      />
      <label
        for="creator_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >Creator</label
      >
    </div>
  </div>
  <button
    @click="addWayPoint"
    class="flex rounded-lg mx-auto my-2 py-2 px-4 hover:bg-red-400 hover:text-white transition duration-500 ease-in-out border border-black m-x-auto"
    :class="{'bg-red-400': state.addingPoint, 'text-white': state.addingPoint}"
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
    <span class="ml-2">add wayPoint</span>
  </button>
</template>
<script setup>
import { reactive, watch } from 'vue';

import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const props = defineProps(['mapData']);

const state = reactive({
  // mapData: computed(() => emi.gpxMaps[props.mapData]),
  mapData: emi.gpxMaps[props.mapData],
  addingPoint: false
});

function formatData(dateString) {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('default', { dateStyle: 'long', timeStyle: 'medium' }).format(date);
}

function addWayPoint(){
  // addingPoint = true;
  emi.toggleWayPointRequest();
}

watch(
  () => emi.state.nowToolMode,
  (val) => {
    state.addingPoint = val === 1;
  }
)

// console.log('!!!!!mapinfo', state.mapData.name);
</script>
<style></style>
