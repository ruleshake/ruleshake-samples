<template>
  <Loading v-if="initialLoading"/>
  <div class="container flex lg:flex-row flex-col py-6 text-white align-top">
    <div class="w-full flex flex-col items-center px-3 mt-6">
      <div class="flex flex-row justify-center">
        <span>Formules</span>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="f90" :checked="formules.includes('F90')"
                 @input="event => handleFormules('F90', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="f90">Formule 90%</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="f95" :checked="formules.includes('F95')"
                 @input="event => handleFormules('F95', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="f95">Formule 95%</label>
        </div>
      </div>
      <div class="flex flex-row justify-center mt-4">
        <span>Assiettes</span>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="tbi" :checked="assiettes.includes('TBI')"
                 @input="event => handleAssiettes('TBI', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="tbi">TBI</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="horsri" :checked="assiettes.includes('HorsRI')"
                 @input="event => handleAssiettes('HorsRI', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="horsri">Hors RI</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="horsri+ri" :checked="assiettes.includes('HorsRI+RI')"
                 @input="event => handleAssiettes('HorsRI+RI', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="horsri+ri">Hors RI + RI</label>
        </div>
      </div>
      <div class="flex flex-row justify-center mt-4">
        <span>Garanties</span>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="incap" :checked="garanties.includes('INCAP')"
                 @input="event => handleGaranties('INCAP', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="incap">Incapacité</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="inval" :checked="garanties.includes('INVAL')"
                 @input="event => handleGaranties('INVAL', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="inval">Invalidité</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="pr" :checked="garanties.includes('PR')"
                 @input="event => handleGaranties('PR', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="pr">Perte de retraite</label>
        </div>
        <div class="inline-block">
          <input class="ml-4" type="checkbox" id="dc" :checked="garanties.includes('DC')"
                 @input="event => handleGaranties('DC', event.target.checked)"/>
          <label class="inline-block ps-[0.15rem] hover:cursor-pointer" for="dc">Décès</label>
        </div>
      </div>

      <div class="flex flex-row justify-center mb-4">
        <div class="mt-8 flex justify-end">
          <a href="#" @click="compute()"
             class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
          >
            Calculer
          </a>
        </div>
        <div v-if="combinaisons" class="mt-8 flex justify-end ml-4">
          <a href="#" @click="downloadJsonFile()"
             class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-lg px-5 py-2.5 text-center"
          >
            Télécharger
          </a>
        </div>
      </div>
      <div v-for="(combinaison, index) in combinaisons" class="p-2 bg-gray-200 text-gray-900 w-full mt-2 rounded">
        <h2>
          <button
            id="faqs-title-01"
            type="button"
            class="flex items-center justify-between w-full text-left font-semibold py-2"
            @click="combinaison.expanded = !combinaison.expanded"
            :aria-expanded="combinaison.expanded"
            aria-controls="faqs-text-01"
          >
            <span>Modèle {{ index + 1 }} - <span class="font-normal">{{ combinaison.formule }}</span></span>
            <svg class="fill-indigo-500 shrink-0 ml-8" width="16" height="16" xmlns="http://www.w3.org/2000/svg">
              <rect y="7" width="16" height="2" rx="1" class="transform origin-center transition duration-200 ease-out"
                    :class="{'!rotate-180': combinaison.expanded}"/>
              <rect y="7" width="16" height="2" rx="1"
                    class="transform origin-center rotate-90 transition duration-200 ease-out"
                    :class="{'!rotate-180': combinaison.expanded}"/>
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
import {ref} from "vue";
import {useNotification} from "@kyvg/vue3-notification";
import Loading from "@/components/Loading.vue";

const initialLoading = ref(false)

const formules = ref(['F90', 'F95'])
const handleFormules = (code, state) => {
  if (state) {
      formules.value.push(code)
  } else {
      formules.value = formules.value.filter(v => v !== code)
  }
}

const assiettes = ref(['TBI', 'HorsRI', 'HorsRI+RI'])
const handleAssiettes = (code, state) => {
  if (state) {
      assiettes.value.push(code)
  } else {
      assiettes.value = assiettes.value.filter(v => v !== code)
  }
}

const garanties = ref(['INCAP', 'INVAL', 'DC', 'PR'])
const handleGaranties = (code, state) => {
  if (state) {
      garanties.value.push(code)
  } else {
      garanties.value = garanties.value.filter(v => v !== code)
  }
}

const compute = async () => {
  initialLoading.value = true
  console.log("garanties", garanties.value)
  const inputs = []
  formules.value.forEach((formule, index) => {
    inputs.push({
      reference: `FORMULES[${index}]`,
      value: formule,
      type: "string"
    })
  })
  assiettes.value.forEach((assiette, index) => {
    inputs.push({
      reference: `ASSIETTES[${index}]`,
      value: assiette,
      type: "string"
    })
  })
  garanties.value.forEach((garantie, index) => {
    inputs.push({
      reference: `GARANTIES[${index}]`,
      value: garantie,
      type: "string"
    })
  })
  const variables = await computeVariables(inputs);
  parseResult(variables)
  initialLoading.value = false
}

const API_URL = import.meta.env.VITE_RULESHAKE_SAMPLES_API_URL;
const computeVariables = async (inputs) => {
  let response = await fetch(`${API_URL}/collections/BAI/evaluations`, {
    method: 'POST',
    body: JSON.stringify(inputs),
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json"
    }
  });
  if (response.status >= 400) {
    throw response
  }
  return response.json();
}

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
  const blob = new Blob([JSON.stringify(combinaisons.value)], {type: 'application/json'});
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