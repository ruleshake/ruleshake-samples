<template>
    <div v-if="open"
         class="fixed top-0 left-0 right-0 z-50 w-full p-4 overflow-x-hidden overflow-y-auto inset-0 flex">
        <div class="absolute bg-black opacity-70 inset-0 z-0"></div>
        <div class="relative w-full max-w-2xl  mx-auto my-auto">
            <!-- Modal content -->
            <div class="relative bg-white rounded-lg shadow">
                <!-- Modal header -->
                <div class="flex items-start justify-between p-4 border-b rounded-t">
                    <h3 class="text-xl font-semibold text-gray-900">
                        {{ title }}
                    </h3>
                    <button type="button"
                            class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center "
                            data-modal-hide="defaultModal" @click="open = false">
                        <svg aria-hidden="true" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
                             xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd"
                                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                  clip-rule="evenodd"></path>
                        </svg>
                    </button>
                </div>
                <!-- Modal body -->
                <div class="p-6 space-y-6">
                    <slot></slot>
                </div>
                <!-- Modal footer -->
                <div class="flex justify-end p-6 space-x-2 border-t border-gray-200 rounded-b">
                    <button data-modal-hide="defaultModal" type="button" @click="open = false"
                            class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                        OK
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import {ref} from "vue";

const open = ref(true)
const props = defineProps({
    title: String,
    content: String
})
</script>