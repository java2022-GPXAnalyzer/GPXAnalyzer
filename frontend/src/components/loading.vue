<template>
  <Transition>
    <div class="loading" v-if="state.isLoading">
      <div class="loading-container">
        <div class="flex justify-between mb-1">
          <span class="text-xl font-medium text-white dark:text-white">{{
            state.loadingMessage
          }}</span>
          <span class="text-xl font-medium text-white dark:text-white"
            >{{ state.loadingPercent.toFixed(0) }}%</span
          >
        </div>
        <div
          class="w-full bg-gray-200 rounded-full h-2.5 dark:bg-gray-700 sexy"
        >
          <div class="border-overlay"></div>
          <div class="gradient-overlay"></div>
          <div
            class="h-2.5 rounded-full bar"
            :style="{width: state.loadingPercent + '%'}"
          ></div>
        </div>
      </div>
    </div>
  </Transition>
</template>
<script setup>
import { reactive, watch } from 'vue';
import { eventManager } from '@/cesium/eventManager';
import gsap from 'gsap';

const emi = eventManager.getInstance();

const state = reactive({
  loadingMessage: 'loading...',
  loadingPercent: 0,
  isLoading: false,
});

let loadingPercentMax = 0;

function percentMethod() {
  let percent = state.loadingPercent + Math.random() * 10;
  if (percent > 100) {
    percent = 100;
    gsap.to(state, { duration: 0.5, loadingPercent: percent });
    setTimeout(() => {
      state.isLoading = false;
    }, 500);
    return;
  }
  if (percent >= loadingPercentMax) {
    percent = loadingPercentMax;
  }
  gsap.to(state, { duration: 0.5, loadingPercent: percent });
  setTimeout(percentMethod, Math.random() * 100);
}

watch(
  () => emi.state.showLoading,
  (val) => {
    if (val === true) {
      // state.isLoading = true;
      // state.loadingPercent = 0;
      // loadingPercentMax = 0;
      // percentMethod();
    } else {
      state.isLoading = false;
    }
  }
);

watch(
  () => emi.state.loadingPercentMax,
  (val) => {
    if (val > 0) {
      loadingPercentMax = Math.max(val, loadingPercentMax);
      state.loadingMessage = emi.loadingMessage || 'loading...';
    }
  }
);
</script>
<style scoped>
.loading {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(61, 61, 61, 0.995);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-container {
  width: 600px;
  height: 34px;
  margin: auto;
}

@keyframes moving-gradient {
  0% {
    background-position: left bottom;
  }
  100% {
    background-position: right bottom;
  }
}

.bar {
  background: -webkit-linear-gradient(
      left,
      #0c6dff 30%,
      #3183ff 60%,
      #0c6dff 100%
    )
    repeat;
  background-size: 50% 100%;
  animation-name: moving-gradient;
  animation-duration: 0.8s;
  animation-iteration-count: infinite;
  animation-timing-function: linear;
}

.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
