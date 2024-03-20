<template>

</template>
<script setup>
import {ref, watch} from "vue";

const props = defineProps({
  variables: Array
})

const sorted = ref([])
const values = ref({})

watch(() => props.variables, (value) => {
    sorted.value = value
      .filter(v => !["TRANCHES", "CALCUL"].includes(v.definitionReference))
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

const sort = (e) => {
  return e.sort((a, b) => a - b);
}

const emit = defineEmits(['values'])

const compute = () => {
  emit('values', values.value)
}
</script>