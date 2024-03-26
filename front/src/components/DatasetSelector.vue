<template>

  <!-- Modal toggle -->
    <div class="flex justify-between">
        <input :value="modelValue" class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mr-4">
        <button data-modal-target="defaultModal" data-modal-toggle="defaultModal"
                class="block text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-4 text-center"
                type="button" @click="toggle()">
            Choisir
        </button>
    </div>

  <!-- Main modal -->
    <transition name="fade">
        <div v-if="open"
             class="fixed top-0 left-0 right-0 z-50 w-full p-4 overflow-x-hidden overflow-y-auto inset-0 flex">
            <div class="absolute bg-black opacity-70 inset-0 z-0"></div>
            <div class="relative w-full max-w-6xl mx-auto my-auto">
                <!-- Modal content -->
                <div class="relative bg-white rounded-lg shadow">
                    <!-- Modal header -->
                    <div class="flex items-start justify-between p-4 border-b rounded-t">
                        <h3 class="text-xl font-semibold text-gray-900">
                            Sélectionner une valeur
                        </h3>
                        <button type="button"
                                class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
                                data-modal-hide="defaultModal" @click="toggle">
                            <svg aria-hidden="true" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
                                 xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd"
                                      d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                      clip-rule="evenodd"></path>
                            </svg>
                        </button>
                    </div>
                    <!-- Modal body -->

                    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
                        <table class="w-full text-sm text-left text-gray-500">
                            <thead class="text-xs text-gray-700 uppercase bg-gray-50 ">
                            <tr>
                                <th v-for="header in getHeaders()" scope="col" class="px-6 py-3">
                                    {{ header }}
                                </th>
                                <th scope="col" class="px-6 py-3">
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="record in records" class="bg-white border-b hover:bg-gray-50">
                                <th v-for="property in Object.values(record.properties)" scope="row"
                                    class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                                    {{ property.value }}
                                </th>
                                <td class="flex items-center px-6 py-4 space-x-3">
                                    <a href="#" class="font-medium text-blue-600 hover:underline" @click="select(record)">Sélectionner</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </transition>

</template>
<script setup>

import {onMounted, ref} from "vue";

const API_URL = import.meta.env.VITE_RULESHAKE_SAMPLES_API_URL;

const open = ref(false)
const records = ref([])
const selected = ref({})
const props = defineProps({
    variable: Object,
    modelValue: String
})

onMounted(async () => {
    await loadRecords()
})

const loadRecords = async () => {
    const response = fetch(`${API_URL}/datasets/${props.variable.datasetCode}/records`)
      .then(response => {
          if (response.status >= 400) {
              throw response
          }
          return response.json();
      })
    records.value = await response
}

const getHeaders = () => {
    return Object.keys(records.value.at(0).properties)
}
const toggle = () => {
    open.value = !open.value
}

const emit = defineEmits(['update:modelValue'])

const select = (record) => {
    selected.value = record
    emit('update:modelValue', record.code)
    open.value = false
}
</script>