<template>
  <div for="helper-text" class="my-2 text-lg font-normal text-white flex">
    {{ variable.properties['LIBELLE'] }}
    <span v-if="variable.mandatory" class="text-red-700 text-xl ml-1">*</span>
    <div v-if="variable.properties['INFOBULLE']" class="tooltip ml-1">
        <svg aria-haspopup="true" xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-info-circle"
             width="25" height="25" viewBox="0 0 24 24" stroke-width="1.5" stroke="#A0AEC0" fill="none"
             stroke-linecap="round" stroke-linejoin="round">
          <path stroke="none" d="M0 0h24v24H0z"/>
          <circle cx="12" cy="12" r="9"/>
          <line x1="12" y1="8" x2="12.01" y2="8"/>
          <polyline points="11 12 12 12 12 16 13 16"/>
        </svg>
      <div class="flex flex-col p-4 bg-white w-96 rounded-md absolute invisible tooltip-item text-black">
        {{ variable.properties['INFOBULLE']}}
      </div>
    </div>
  </div>
  <DatasetSelector v-if="variable.type === 'record'" v-model="model" :variable="variable"/>
  <div v-else-if="variable.type === 'boolean'">
    <ul
      class="items-center mt-2 w-full text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-lg flex grow">
      <li class="w-full border-gray-200 border-r">
        <div class="flex items-center pl-3">
          <input v-model="model" :value="true" :id="'radio-yes-' + variable.runtimeReference"
                 :name="variable.runtimeReference" type="radio"
                 class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
          <label :for="'radio-yes-' + variable.runtimeReference"
                 class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Oui</label>
        </div>
      </li>
      <li class="w-full border-gray-200">
        <div class="flex items-center pl-3">
          <input v-model="model" :value="false" :id="'radio-no-' + variable.runtimeReference"
                 :name="variable.runtimeReference" type="radio"
                 class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
          <label :for="'radio-no-' + variable.runtimeReference"
                 class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Non</label>
        </div>
      </li>
    </ul>
  </div>
  <div v-else-if="variable.validValues && variable.validValues.length > 0">
    <select v-model="model"
            class="bg-gray-50 border w-full border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 p-3">
      <option selected></option>
      <option v-for="value in sort(variable.validValues)" :value="value">{{ value }}</option>
    </select>
  </div>
  <input v-else v-model="model" :type="getInputType(variable)"
         class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
</template>
<script setup>
import DatasetSelector from "@/components/DatasetSelector.vue";

const props = defineProps(['variable'])
const model = defineModel()

const getInputType = (variable) => {
  if (variable.type === "string") return 'text'
  return variable.type
}

const sort = (e) => {
  return e.sort((a, b) => a - b);
}
</script>
<style>
.tooltip:hover .tooltip-item {
  visibility: visible;
}
</style>