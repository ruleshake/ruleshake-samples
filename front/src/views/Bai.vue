<template>
  <div class="container flex lg:flex-row flex-col py-6 text-white align-top">
    <div class="lg:w-1/2 w-full px-3 mx-auto">
      <Form type="column" collection-code="BAI"
            :ignored-variables="['FORMULES', 'GARANTIES', 'RESULT']"
            :initial-variables="inputs" @values="parseResult" @error="onError"/>
    </div>
    <div class="lg:w-1/2 w-full flex flex-col items-center px-3 mt-6">
      <div v-for="combinaison in combinaisons" class="p-2 bg-gray-200 text-gray-900 w-full">
        <h2>
          <button
            id="faqs-title-01"
            type="button"
            class="flex items-center justify-between w-full text-left font-semibold py-2"
            @click="combinaison.expanded = !combinaison.expanded"
            :aria-expanded="combinaison.expanded"
            aria-controls="faqs-text-01"
          >
            <span>{{ combinaison.formule }} - {{ combinaison.total }} %</span>
            <svg class="fill-indigo-500 shrink-0 ml-8" width="16" height="16" xmlns="http://www.w3.org/2000/svg">
              <rect y="7" width="16" height="2" rx="1" class="transform origin-center transition duration-200 ease-out" :class="{'!rotate-180': combinaison.expanded}" />
              <rect y="7" width="16" height="2" rx="1" class="transform origin-center rotate-90 transition duration-200 ease-out" :class="{'!rotate-180': combinaison.expanded}" />
            </svg>
          </button>
        </h2>
        <div class="grid text-sm text-slate-600 overflow-hidden transition-all duration-300 ease-in-out"
          :class="combinaison.expanded ? 'grid-rows-[1fr] opacity-100' : 'grid-rows-[0fr] opacity-0'"
        >
          <div class="overflow-hidden">
            <p class="pb-3">
              <table class="table-auto mx-auto">
                <thead>
                <tr>
                  <th>Garantie</th>
                  <th>Taux</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="garantie in combinaison.garanties">
                  <td class="px-6">{{ garantie.label }}</td>
                  <td class="px-6">{{ garantie.rate }}</td>
                </tr>
                <tr>
                  <td class="px-6"></td>
                  <td class="px-6">{{ combinaison.total }}</td>
                </tr>
                </tbody>
              </table>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Form from "@/components/Form.vue";
import {ref} from "vue";
import {useNotification} from "@kyvg/vue3-notification";

const inputs = [];
['F90', 'F95'].forEach((formule, index) => inputs.push({
  type: 'string',
  reference: `FORMULES[${index}]`,
  value: formule
}));
[["INC", "INV"], ["INC", "INV", "DECES"], ["INC", "INV", "DECES", "PATCA", "MATAD"], ["INC", "INV", "PATCA", "MATAD"]].forEach((combinaison, indexC) => {
  combinaison.forEach((garantie, indexG) => {
    inputs.push({
      type: 'string',
      reference: `COMBINAISONS[${indexC}]/GARANTIES[${indexG}]`,
      value: garantie
    })
  })
})

const combinaisons = ref(null)
const parseResult = (values) => {
  combinaisons.value = values
    .filter(variable => variable.definitionReference === 'RESULT')
    .map(item => {
      const {FORMULE, COMBINAISON} = item.loopStep;
      const formule = values.find(variable => variable.runtimeReference === FORMULE).value
      const codes = values.find(variable => variable.runtimeReference === COMBINAISON).subVariables
        .map(g => g.value)
        .join(" / ")
      const total = item.subVariables.find(variable => variable.definitionReference === "RESULT/TOTAL").value
      const garanties = item.subVariables.filter(variable => variable.definitionReference === "RESULT/GARANTIES")
        .map(garantie => {
          const libelle = garantie.subVariables
            .find(variable => variable.definitionReference === "RESULT/GARANTIES/INFOS")
            .subVariables
            .find(variable => variable.definitionReference === "RESULT/GARANTIES/INFOS/LIBELLE")
            .value
          const taux = garantie.subVariables
            .find(variable => variable.definitionReference === "RESULT/GARANTIES/TAUX")
            .value
          return {
            label: libelle,
            rate: taux
          }
        })
      return {
        formule: formule + " - " + codes,
        total: total,
        garanties: garanties,
        expanded: false
      }
    });
  console.log(combinaisons.value)
}

const {notify} = useNotification()
const onError = (e) => {
  console.log(e)
  notify({
    type: 'error',
    title: "Erreur",
    duration: 8000,
    text: "Une erreur est survenue lors de la simulation du calcul de BAI",
  });
}
</script>