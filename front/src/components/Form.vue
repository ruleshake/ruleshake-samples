<template>
  <Loading v-if="initialLoading"/>
  <StepperForm v-if="type === 'steps'" :variables="displayableVariables" :ignored-variables="ignoredVariables" @values="evaluate" />
  <ColumnForm v-if="type === 'column'" :variables="displayableVariables" :ignored-variables="ignoredVariables" @values="evaluate" />
</template>
<script setup>
import {onMounted, ref, watch} from "vue";
import StepperForm from "@/components/StepperForm.vue";
import ColumnForm from "@/components/ColumnForm.vue";
import Loading from "@/components/Loading.vue";

const props = defineProps(["type", "collectionCode", "ignoredVariables", "initialVariables"])

const API_URL = import.meta.env.VITE_RULESHAKE_SAMPLES_API_URL;

const initialLoading = ref(true)
const loading = ref(false)
const variables = ref([])
const displayableVariables = ref([])

onMounted(async () => {
  await fetchVariables()
  initialLoading.value = false
})

const fetchVariables = async () => {
  const response = computeVariables([]);
  variables.value = await response;
}

const computeVariables = (inputs) => {
  const response = fetch(`${API_URL}/collections/${props.collectionCode}/evaluations`, {
    method: 'POST',
    body: JSON.stringify(inputs),
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (response.status >= 400) {
        throw response
      }
      return response.json();
    })
  return response;
}

watch(() => variables.value, (value) => {
    displayableVariables.value = value
      .filter(v => !props.ignoredVariables.includes(v.definitionReference))
      .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
  }
)

const emit = defineEmits(['values', 'error'])

const evaluate = (values) => {
  const inputs = Object.entries(values)
    .filter(([reference, value]) => value.value !== undefined)
    .map(([reference, value]) => {
      return {
        reference: reference,
        value: value.value,
        type: value.type
      }
    });
  inputs.push(...props.initialVariables)

  initialLoading.value = true
  computeVariables(inputs)
    .then(result => {
      emit('values', result)
    })
    .catch(e => emit('error', e))
    .finally(() => initialLoading.value = false);
}
</script>