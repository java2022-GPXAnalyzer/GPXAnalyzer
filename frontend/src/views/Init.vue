<template>  
  <div class="h-screen w-screen flex">
    <div class="container my-auto mx-auto inline-block w-64">
      <img class="h-64 w-64" alt="Vue logo" src="@/assets/map.png" />
      <div class="text-3xl font-bold mt-3 inline-block">GPX Analyzer</div>
      <form class="flex items-center space-x-6">
        <label class="block">
          <span class="sr-only">Choose profile photo</span>
          <input
            type="file"
            class="block w-full text-sm text-slate-500 
            file:mr-4 file:py-2 file:px-4 file:rounded-full 
            file:border-0 file:text-sm file:font-semibold 
            file:bg-gray-200 file:text-blue-500 hover:file:bg-blue-200"
            style="text-align-last: center;"
          />
        </label>
      </form>
      <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="goToIndex">
        Go to the map
      </button>
    </div>
  </div>
</template>
<script setup>
  import { useRouter } from 'vue-router';
  import { eventManager } from '@/cesium/eventManager';
  const router = useRouter();
  const emi = eventManager.getInstance();
  
  function goToIndex() {
    emi.showLoading();
    setTimeout(() => {
      router.push('/index');
    }, 500);
  }
</script>
<style lang="">
  
</style>