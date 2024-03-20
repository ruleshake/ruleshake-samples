<template>

    <ol class="flex items-center font-medium text-center text-xl">
        <li v-for="(variable, index) in sorted" :class="getStepperItemClass(index)">
            <span class="flex items-center">
                <span :class="getNumberClass(index)">
                    {{ index + 1 }}
                </span>
                {{ variable.properties['ONGLET'] }}
            </span>
        </li>
    </ol>

    <div v-if="sorted.length > 0" class="mt-6">
        <div v-for="subVariable in getSubVariables()" class="mt-4">
            <label for="helper-text"
                   class="block mb-2 text-lg font-normal text-white">
                {{ subVariable.properties['LIBELLE'] }}
                <span v-if="subVariable.mandatory" class="text-red-700 text-xl">*</span>
            </label>
            <DatasetSelector v-if="subVariable.type === 'record'" v-model="values[subVariable.runtimeReference].value"
                             :variable="subVariable"/>
            <div v-else-if="subVariable.type === 'boolean'">
                <ul class="items-center mt-2 w-full text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-lg flex">
                    <li class="w-full border-gray-200 border-r">
                        <div class="flex items-center pl-3">
                            <input v-model="values[subVariable.runtimeReference].value" :value="true"
                                   :id="'radio-yes-' + subVariable.runtimeReference"
                                   :name="subVariable.runtimeReference" type="radio"
                                   class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
                            <label :for="'radio-yes-' + subVariable.runtimeReference"
                                   class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Oui</label>
                        </div>
                    </li>
                    <li class="w-full border-gray-200">
                        <div class="flex items-center pl-3">
                            <input v-model="values[subVariable.runtimeReference].value" :value="false"
                                   :id="'radio-no-' + subVariable.runtimeReference" :name="subVariable.runtimeReference"
                                   type="radio"
                                   class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 focus:ring-2">
                            <label :for="'radio-no-' + subVariable.runtimeReference"
                                   class="w-full py-3 ml-2 text-lg font-medium text-gray-900">Non</label>
                        </div>
                    </li>
                </ul>
            </div>
            <div v-else-if="subVariable.validValues && subVariable.validValues.length > 0">
                <select v-model="values[subVariable.runtimeReference].value"
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3">
                    <option selected></option>
                    <option v-for="value in sort(subVariable.validValues)" :value="value">{{ value }}</option>
                </select>
            </div>
            <input v-else v-model="values[subVariable.runtimeReference].value" :type="getInputType(subVariable)"
                   class="bg-gray-50 border border-gray-300 text-gray-900 text-lg rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
        </div>
    </div>
    <div :class="current === 0 ? 'mt-8 flex justify-end' : 'mt-8 flex justify-between'">
        <a href="#" v-if="current > 0"
           class="text-white border border-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
           @click="current--"
        >
            Précédent
        </a>
        <a href="#" v-if="current < sorted.length - 1"
           class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
           @click="current++"
        >
            Suivant
        </a>
        <a href="#" v-if="current === sorted.length - 1"
           class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
           @click="compute()"
        >
            Voir les tarifs
        </a>
    </div>

</template>
<script setup>

import {watch, ref} from "vue";
import DatasetSelector from "@/components/DatasetSelector.vue";

const current = ref(0)
const props = defineProps({
    variables: Array
})

const sorted = ref([])
const values = ref({})

watch(() => props.variables, (value) => {
        sorted.value = value
            .filter(v => !["TARIF", "FORMULES", "FRACTIONNEMENTS"].includes(v.definitionReference))
            .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
        initValues(sorted.value)
    }
)

const initValues = (vars) => {
    vars.forEach(variable => {
        if (variable.type === 'composite') {
            initValues(variable.subVariables)
        } else {
            values.value[variable.runtimeReference] = {type: variable.type}
        }
    })
}

const getSubVariables = () => {
    const variable = sorted.value.at(current.value)
    if (variable.type === 'composite') {
      return variable.subVariables.filter(v => v.properties && v.properties["LIBELLE"])
          .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
    }
    return [];
}
const getInputType = (variable) => {
    if (variable.type === "string") return 'text'
    return variable.type
}

const getStepperItemClass = (index) => {
    let clazz = ''
    if (index <= current.value) {
        clazz += 'flex text-blue-600 font-semibold'
        if (index < sorted.value.length - 1) {
            clazz += ' w-full after:w-full items-center after:border-b after:border-1 after:mx-6 after:border-gray-400'
        }
    } else {
        clazz = 'flex text-gray-400 font-normal'
        if (index < sorted.value.length - 1) {
            clazz += ' w-full after:w-full items-center after:border-b after:border-1 after:mx-6 after:border-gray-400'
        }
    }
    return clazz
}

const getNumberClass = (index) => {
    let clazz = 'flex items-center justify-center mr-2 w-7 h-7 border rounded-full shrink-0'
    if (index <= current.value) {
        clazz += ' bg-blue-600 border-blue-600 text-gray-800'
    } else {
        clazz += ' border-gray-400 text-gray-400'
    }
    return clazz
}

const sort = (e) => {
    return e.sort((a, b) => a - b);
}

const emit = defineEmits(['values'])

const compute = () => {
    emit('values', values.value)
}

</script>