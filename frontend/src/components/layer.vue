<template lang="">
  <div class="round-none border-2 border-black">
    <p class="title">Map</p>

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
      v-for="(map, index) in tmpMap.maps"
      :key="index"
      style="cursor: pointer"
      class="flex divide-y-2 divide-gray-500"
    >
      <!-- open layer -->
      <button
        @click="closeMap(index)"
        class="eye"
        v-if="tmpMap.maps[index].showLayer"
      >
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
      <!-- close layer -->
      <button @click="openMap(index)" class="eye" v-else>
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
      <ul>
        <li
          @click="map.show = !map.show"
          :class="{ openLayer: tmpMap.maps[index].showLayer }"
        >
          {{ map.name }}
        </li>

        <Transition>
          <ul v-if="map.show">
            <li
              v-for="(point, pointIndex) in map.points"
              :key="pointIndex"
              :class="{ openSubLayer: tmpMap.maps[index].showLayer }"
              class="child"
              @click="test(index, pointIndex)"
            >
              {{ point.uuid }}
            </li>
          </ul>
        </Transition>
      </ul>
    </div>
  </div>
</template>
<script setup>
import { reactive } from 'vue';

const state = reactive({

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
  ],
});

function openLayer() {}

function closeMap(index) {
  tmpMap.maps[index].showLayer = false;
}

function openMap(index) {
  tmpMap.maps[index].showLayer = true;
}
</script>
<style>
.openLayer {
  @apply hover:text-red-400;
}
.openSubLayer {
  @apply hover:text-blue-400;
}
.eye {
  @apply text-xl font-sans p-0 mt-1 ml-4 mr-2 items-start flex-col;
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
  content: '';
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
  content: '';
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
  content: '';
  width: 1em;
  height: 3.5em;
}
</style>
