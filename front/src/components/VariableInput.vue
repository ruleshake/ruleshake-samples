<template>
  <label for="helper-text" class="block mb-2 text-lg font-normal text-white">
    {{ variable.properties['LIBELLE'] }}
    <span v-if="variable.mandatory" class="text-red-700 text-xl">*</span>
  </label>
  <DatasetSelector v-if="variable.type === 'record'" v-model="values[variable.runtimeReference].value" :variable="variable"/>
  <div v-else-if="variable.type === 'boolean'">
    <ul class="items-center mt-2 w-full text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-lg flex">
      <li class="w-full border-gray-200 border-r">
        <div class="flex items-center pl-3">
          <input v-model="values[variable.runtimeReference].value" :value="true"
                 :id="'radio-yes-' + variable.runtimeReference"
                 :name="variable.runtimeReference" type="radio"
                 class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
          <label :for="'radio-yes-' + variable.runtimeReference"
                 class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Oui</label>
        </div>
      </li>
      <li class="w-full border-gray-200">
        <div class="flex items-center pl-3">
          <input v-model="values[variable.runtimeReference].value" :value="false"
                 :id="'radio-no-' + variable.runtimeReference" :name="variable.runtimeReference"
                 type="radio"
                 class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
          <label :for="'radio-no-' + variable.runtimeReference"
                 class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Non</label>
        </div>
      </li>
    </ul>
  </div>
  <div v-else-if="variable.validValues && variable.validValues.length > 0">
    <select v-model="values[variable.runtimeReference].value"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3">
      <option selected></option>
      <option v-for="value in sort(variable.validValues)" :value="value">{{ value }}</option>
    </select>
  </div>
  <input v-else v-model="values[variable.runtimeReference].value" :type="getInputType(variable)"
         class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
</template>
<script setup>
import DatasetSelector from "@/components/DatasetSelector.vue";
const props = defineProps(['variable'])

const getInputType = (variable) => {
  if (variable.type === "string") return 'text'
  return variable.type
}

const sort = (e) => {
  return e.sort((a, b) => a - b);
}
</script>