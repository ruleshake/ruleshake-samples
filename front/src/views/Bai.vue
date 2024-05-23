<template>
  <div class="container flex lg:flex-row flex-col py-6 text-white align-top">
    <div class="lg:w-1/2 w-full px-3 mx-auto">
      <Form type="column" collection-code="BAI"
            :ignored-variables="['FORMULES', 'GARANTIES', 'ASSIETTES']"
            :initial-variables="inputs" @values="parseResult" @error="onError"/>
      <div v-if="combinaisons" class="mt-8 flex justify-end">
        <a href="#" @click="downloadJsonFile()"
           class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
        >
          Télécharger
        </a>
      </div>
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
            <span>{{ combinaison.formule }}</span>
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
                  <th>Taux couv</th>
                  <th>Taux coti</th>
                  <th>TBI</th>
                  <th>NBI</th>
                  <th>RI</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="garantie in combinaison.garanties">
                  <td class="px-6">{{ garantie.code }}</td>
                  <td class="px-6">{{ garantie.couv }}</td>
                  <td class="px-6">{{ garantie.coti }}</td>
                  <td class="px-6">{{ garantie.tbi }}</td>
                  <td class="px-6">{{ garantie.nbi }}</td>
                  <td class="px-6">{{ garantie.ri }}</td>
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
const combinaisons = ref(null)
const parseResult = (values) => {
  combinaisons.value = values
    .filter(variable => variable.definitionReference === 'MODELES')
    .map(item => {
      const libelle = item.subVariables.find(variable => variable.definitionReference === "MODELES/LIBELLE").value
      const total = item.subVariables.find(variable => variable.definitionReference === "MODELES/TAUX").value
      const garanties = item.subVariables.filter(variable => variable.definitionReference === "MODELES/GARANTIES")
        .map(garantie => {
          const code = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/CODE")
            .value
          const taux = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/TAUX_COTI")
            .value
          const tauxCouv = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/TAUX_COUV")
            .value
          const tbi = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE")
            .subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE/TBI")
            .value
          const nbi = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE")
            .subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE/NBI")
            .value
          const ri = garantie.subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE")
            .subVariables
            .find(variable => variable.definitionReference === "MODELES/GARANTIES/COUVERTURE/RI")
            .value
          return {
            code: code,
            coti: taux,
            couv: tauxCouv,
            tbi: tbi,
            nbi: nbi,
            ri: ri
          }
        })
      return {
        formule: libelle,
        total: total,
        garanties: garanties,
        expanded: false
      }
    });
}

function downloadJsonFile() {
  // Creating a blob object from non-blob data using the Blob constructor
  const blob = new Blob([JSON.stringify(combinaisons.value)], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  // Create a new anchor element
  const a = document.createElement('a');
  a.href = url;
  a.download = 'bai.json';
  a.click();
  a.remove();
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