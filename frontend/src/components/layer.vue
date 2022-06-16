<template>
  <div>
    <button>
      <router-link to="/">back</router-link>
    </button>
  </div>
</template>

<script setup>
  import { useRouter, useRoute } from 'vue-router'
  import cesiumMap from '@/components/cesiumMap.vue'

  const router = useRouter();

  function backToInit() {
    router.push('/');
  }

  // export default {
  //   name: 'backToInit',
  //   components: {
  //     cesiumMap,
  //   },
  //   methods: {
  //     backToInit,
  //   },
  // };

  const backToInit = {
    name: 'backToInit',
    components: {
      cesiumMap,
    },
    methods: {
      backToInit,
    },
  };
</script>

<style lang="">
</style>