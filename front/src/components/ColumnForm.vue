<template>
  <div v-for="(composite, index) in sorted">
    <div class="mt-6">
      <span class="flex items-center w-full after:w-full before:w-full before:border-b before:border-1 after:border-b after:border-1 before:mx-6 after:mx-6 before:border-gray-400 after:border-gray-400">
        <span class="flex items-center justify-center mr-2 w-7 h-7 border rounded-full shrink-0 border-gray-400 text-gray-400">
          {{ index + 1 }}
        </span>
        {{ composite.properties['ONGLET'] }}
      </span>
      <VariableInput v-for="subVariable in getSubVariables(index)" v-model="values[subVariable.runtimeReference].value" :variable="subVariable" />
    </div>
  </div>
  <div class="mt-8 flex justify-end">
    <a href="#" @click="compute()"
       class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
    >
      Calculer
    </a>
  </div>

</template>
<script setup>
import {ref, watch} from "vue";
import VariableInput from "@/components/VariableInput.vue";

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

const getSubVariables = (index) => {
  const variable = sorted.value.at(index)
  if (variable.type === 'composite') {
    return variable.subVariables
      .filter(v => !props.ignoredVariables.includes(v.definitionReference))
      .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
  }
  return [];
}

const emit = defineEmits(['values'])

const compute = () => {
  emit('values', values.value)
}
</script>