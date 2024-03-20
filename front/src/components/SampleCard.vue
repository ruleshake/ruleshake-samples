<template>
  <router-link :to="link">
    <div class="group flex transform cursor-pointer flex-col items-center border border-gray-500 rounded-xl p-8 transition-colors duration-300 hover:bg-blue-600 m-2">
      <span class="bg-blue-200 text-sm font-medium text-blue-800 text-center p-1 leading-none rounded-full px-2 dark:bg-blue-900 dark:text-blue-200 absolute left-auto top-2 right-2">
        {{ lang }}
      </span>
      <img class="h-64 w-64 object-cover" :src="`/src/assets/${img}.png`" alt=""/>
      <h1 class="mt-4 text-3xl font-semibold group-hover:text-white text-white">
        {{ title }}
      </h1>
    </div>
  </router-link>
</template>
<script setup>
const props = defineProps(['link', 'lang', 'img', 'title'])
</script>