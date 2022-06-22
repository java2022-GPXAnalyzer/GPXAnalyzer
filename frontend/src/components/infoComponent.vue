<template lang="">
  <div class="round-none border-2 border-black w-64 p-1 m-1">
    <hot-spot v-if="state.showHotSpot"></hot-spot>
    <map-info v-if="state.showMapInfo" :map-data="state.mapDataIdx" :key="state.mapDataIdx"></map-info>
    <point-info v-if="state.showPointInfo" :point-data="state.pointDataId" :key="state.pointDataId"></point-info>
  </div>
</template>
<script setup>
import mapInfo from '@/components/info/mapInfo.vue';
import pointInfo from '@/components/info/pointInfo.vue';
import hotSpot from '@/components/info/hotSpot.vue';

import { reactive, watch } from 'vue';
import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

const state = reactive({
  pointData: {},
  mapDataIdx: null,
  pointDataId: null,
  showMapInfo: false,
  showPointInfo: false,
  showHotSpot: false,
});

watch(
  () => emi.state.nowSelectMap,
  (val) => {
    if (val >= 0) {
      state.mapDataIdx = val;
      state.showMapInfo = true;
    } else if (val === -1) {
      state.mapDataIdx = null;
      state.showMapInfo = false;
    }
  }
);

watch(
  () => emi.state.nowSelectPoint,
  (val) => {
    if (val !== null){
      state.pointData = emi.gpxMaps[emi.state.nowSelectMap].getPoint(val);
      state.pointDataId = val;
      state.showPointInfo = true;
    } else {
      state.pointData = {};
      state.showPointInfo = false;
    }
  }
)

watch(
  () => emi.state.hotSpot,
  (val) => {
    console.log(val);
    if(val){
      state.showHotSpot = true;
    } else {
      state.showHotSpot = false;
    }
  }
)

</script>
<style scoped>
</style>
