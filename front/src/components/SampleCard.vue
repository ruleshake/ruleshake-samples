<template>
  <router-link :to="link">
    <div class="flex cursor-pointer flex-col border border-gray-500 rounded-xl p-6 transition-colors duration-300 hover:bg-blue-600 m-2">
      <div class="flex flex-row justify-end">
        <span class="bg-blue-200 text-sm font-medium text-blue-800 p-1.5 leading-none rounded-full px-2">
          {{ lang }}
        </span>
      </div>
      <div class="flex flex-col items-center">
        <img class="w-36 lg:w-64 object-cover" :src="`/src/assets/${img}.png`" alt=""/>
        <h1 class="mt-4 text-2xl lg:text-3xl font-semibold group-hover:text-white text-white">
          {{ title }}
        </h1>
      </div>
    </div>
  </router-link>
</template>
<script setup>
const props = defineProps(['link', 'lang', 'img', 'title'])
</script>