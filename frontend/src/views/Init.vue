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
            @input="onFileInput"
            multiple
            class="block w-full text-sm text-slate-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-gray-200 file:text-blue-500 hover:file:bg-blue-200"
            style="text-align-last: center"
          />
        </label>
      </form>
      <!-- <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" @click="goToIndex">
        Go to the map
      </button> -->
    </div>
  </div>
</template>
<script setup>
import { reactive } from 'vue';
import { postGpxFilePathAPI } from '@/api/index';

import { eventManager } from '@/cesium/eventManager';
const emi = eventManager.getInstance();

import { useRouter } from 'vue-router';
const router = useRouter();

function onFileInput(e) {
  const files = e.target.files;
  let postDataFilePath = [];
  for (let i = 0; i < files.length; i++) {
    postDataFilePath.push(files[i].path);
  }
  emi
    .postGPXFile(postDataFilePath)
    .then((res) => {
      window.$electron.ipcRenderer.send('gotoCesium');
      emi.showLoadingPercent(5, 'gpx loaded...');
      setTimeout(() => {
        router.push('/index');
      }, 500);
    })
    .catch(() => {
      emi.showAlertMessage(2, 'Error while uploading file');
    });
}
</script>
<style lang=""></style>
