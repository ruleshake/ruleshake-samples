<template>
  <ol class="flex items-center font-medium text-center text-xl">
    <li v-for="(variable, index) in sorted" :class="getStepperItemClass(index)">
            <span class="flex items-center">
                <span  :class="getNumberClass(index)">
                    {{ index + 1 }}
                </span>
                {{ variable.properties['ONGLET'] }}
            </span>
    </li>
  </ol>

  <div v-if="sorted.length > 0" class="mt-6">
    <VariableInput v-for="subVariable in getSubVariables()" v-model="values[subVariable.runtimeReference].value" :variable="subVariable" />
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
import VariableInput from "@/components/VariableInput.vue";
import {ref, watch} from "vue";

const current = ref(0)
const props = defineProps(['variables', 'ignoredVariables'])

const sorted = ref([])
const values = ref({})

watch(() => props.variables, (value) => {
  sorted.value = value
    .filter(v => !props.ignoredVariables.includes(v.definitionReference))
    .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
  initValues(sorted.value)
})

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
    return variable.subVariables
      .filter(v => !props.ignoredVariables.includes(v.definitionReference))
      .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
  }
  return [];
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

const emit = defineEmits(['values'])

const compute = () => {
  emit('values', values.value)
}
</script>