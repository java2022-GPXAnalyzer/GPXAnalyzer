<template>
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
  <div class="px-3 pt-3">
    <!-- <div class="relative">
      <input
        type="text"
        id="uuid_input"
        :value="state.pointData.uuid"
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
        v-model="state.pointData.name"
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
        id="time_input"
        :value="formatData(state.pointData.time)"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        readonly
      />
      <label
        for="time_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >Point Time</label
      >
    </div>
    <div class="relative z-0 my-2">
      <input
        type="number"
        id="lat_input"
        v-model.number="state.pointData.lat"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        required
      />
      <label
        for="lat_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >latitude</label
      >
    </div>
    <div class="relative z-0 my-2">
      <input
        type="number"
        id="lng_input"
        v-model.number="state.pointData.lon"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        required
      />
      <label
        for="lng_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >longitude</label
      >
    </div>
    <div class="relative z-0 my-2">
      <input
        type="number"
        id="ele_input"
        v-model.number="state.pointData.ele"
        class="block rounded-t-lg px-2.5 pb-2.5 pt-5 w-full text-sm text-gray-900 bg-gray-50 dark:bg-gray-700 border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
        placeholder=" "
        required
      />
      <label
        for="ele_input"
        class="absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-4 scale-75 top-4 z-10 origin-[0] left-2.5 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-4"
        >elevation</label
      >
    </div>
  </div>
  <button
    @click="removePoint"
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
</template>
<script setup>
import { reactive, watch } from 'vue';

import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const props = defineProps(['pointData']);

const state = reactive({
  // pointData: computed(() => props.pointData),
  pointData: emi.gpxMaps[emi.state.nowSelectMap].findPoints(props.pointData),
});

function formatData(dateString) {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('default', {
    dateStyle: 'long',
    timeStyle: 'medium',
  }).format(date);
}

function _checkVaildLat(lat) {
  return lat >= -90 && lat <= 90;
}

function _checkVaildLng(lng) {
  return lng >= -180 && lng <= 180;
}

function _samePostion(a, b) {
  return a.lat === b.lat && a.lng === b.lng && a.ele === b.ele;
}

watch(
  () => {
    return {
      lat: state.pointData.lat,
      lng: state.pointData.lng,
      ele: state.pointData.ele,
    };
  },
  (newVal, oldVal) => {
    let isVaild = true;
    if(!_checkVaildLat(newVal.lat)){
      state.pointData.lat = oldVal.lat;
      isVaild = false;
    }
    if(!_checkVaildLng(newVal.lng)){
      state.pointData.lng = oldVal.lng;
      isVaild = false;
    }
    if(newVal.lat === '') {
      state.pointData.lat = 0;
      isVaild = false;
    }
    if(newVal.lng === '') {
      state.pointData.lng = 0;
      isVaild = false;
    }
    if(isVaild){
      console.log('update');
      emi.gpxMaps[emi.state.nowSelectMap].updatePoint(state.pointData.uuid, newVal);
      emi.gpxMaps[emi.state.nowSelectMap].reDrawLine();
    }
  }
);

function removePoint() {
  emi.removePoint(emi.state.nowSelectMap, state.pointData.uuid);
  console.log(emi.state.nowSelectPoint);
}
</script>
<style></style>
