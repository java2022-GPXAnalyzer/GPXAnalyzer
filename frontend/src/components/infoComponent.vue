<template lang="">
  <div class="round-none border-2 border-black w-64 p-1 m-1">
    <map-info v-if="state.showMapInfo" :map-data="state.mapData"></map-info>
    <point-info v-if="state.showPointInfo" :point-data="state.pointData"></point-info>
  </div>
</template>
<script setup>
import mapInfo from '@/components/info/mapInfo.vue';
import pointInfo from '@/components/info/pointInfo.vue';

import { reactive, watch } from 'vue';
import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const state = reactive({
  mapData: {},
  pointData: {},
  showMapInfo: false,
  showPointInfo: false
});

watch(
  () => emi.state.nowSelectMap,
  (val) => {
    if (val >= 0) {
      state.mapData = emi.gpxMaps[val];
      state.showMapInfo = true;
    } else if (val === -1) {
      state.mapData = {};
      state.showMapInfo = false;
    }
  }
);

watch(
  () => emi.state.nowSelectPoint,
  (val) => {
    console.log(val);
    if (val !== ''){
      console.log('??', emi.state.nowSelectMap);
      state.pointData = emi.gpxMaps[emi.state.nowSelectMap].getPoint(val);
      state.showPointInfo = true;
    } else {
      state.pointData = {};
      state.showPointInfo = false;
    }
  }
)
</script>
<style scoped>
</style>
