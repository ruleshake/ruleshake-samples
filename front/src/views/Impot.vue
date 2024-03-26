<template>
  <SampleInfos title="Simulation impôt sur le revenu">
    <div class="flex p-4 mb-4 text-sm rounded-lg bg-gray-800 text-yellow-300" role="alert">
      <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20"
           xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd"
              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
              clip-rule="evenodd"></path>
      </svg>
      <span class="sr-only">Info</span>
      <div>Ceci est uniquement un exemple d'implémentation des services RuleShake</div>
    </div>
    <p>
      Cet exemple permet une simulation express de l'impôt sur le revenu à payer en renseignant sa situation,
      ses revenus ainsi que ses réductions/crédit d'impôt.
    </p>
    <p>
      Le calcul est réalisé dans une collection de variables dans RuleShake en suivant les règles définies ici
      <a href="https://www.economie.gouv.fr/particuliers/tranches-imposition-impot-revenu">
        https://www.economie.gouv.fr/particuliers/tranches-imposition-impot-revenu
      </a>
    </p>
  </SampleInfos>
  <div class="container flex lg:flex-row flex-col py-6 text-white align-top">
    <div class="lg:w-1/2 w-full px-3 mx-auto">
      <Form type="column" collection-code="SIMU_IR"
            :ignored-variables="['TRANCHES', 'CALCUL', 'FOYER/NB_PARTS', 'FOYER/NB_PARTS_QF', 'ASSIETTE/BASE_CALCUL', 'ASSIETTE/BASE_CALCUL_QF']"
            :initial-variables="inputs" @values="parseResult" @error="onError"/>
    </div>
    <div ref="results"></div>
    <div v-if="data" class="lg:w-1/2 w-full flex flex-col items-center px-3 mt-6">

      <div class="w-full p-4 pt-6 rounded-xl bg-white bg-clip-border text-gray-700">

        <div class="flex flex-col bg-white border shadow-sm">
          <div class="bg-gray-700 border-gray-900 border-b py-2 px-4">
            <p class="mt-1 text-gray-200">
              Éléments imposables
            </p>
          </div>
          <div class="p-4 flex flex-row w-full overflow-hidden py-3">
            <span class="w-2/3">
              Revenu net imposable :
              <span class="font-bold">
                {{ formatPrice(data.revenus) }}
              </span>
            </span>
              <span>
              Nb parts :
              <span class="font-bold">
                {{ data.nbParts }}
              </span>
            </span>
          </div>
        </div>

        <div class="flex flex-col bg-white border shadow-sm mt-2">
          <div class="bg-gray-700 border-gray-900 border-b py-2 px-4">
            <p class="mt-1 text-gray-200">
              Répartition par tranche
            </p>
          </div>
          <div class="p-4 flex flex-row w-full overflow-hidden py-3">
            <table class="w-full">
              <thead>
                <tr class="text-left">
                  <th>Tranches</th>
                  <th>Montant</th>
                  <th>Taux</th>
                  <th>Impôt</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(tranche, index) in data.tranches" :class="index === 0 ? '': 'border-t'">
                  <td v-if="tranche.min === 0">
                    Jusqu'à {{ formatPrice(tranche.max) }}
                  </td>
                  <td v-else-if="tranche.max === 999999999999">
                    À partir de {{ formatPrice(tranche.min) }}
                  </td>
                  <td v-else>
                    De {{ formatPrice(tranche.min) }} à {{ formatPrice(tranche.max) }}
                  </td>
                  <td>
                    {{ formatPrice(tranche.montant) }}
                  </td>
                  <td>
                    {{ tranche.taux * 100 }} %
                  </td>
                  <td class="py-1">
                    {{ formatPrice(tranche.impot) }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="flex flex-col bg-white border shadow-sm mt-2">
          <div class="bg-gray-700 border-gray-900 border-b py-2 px-4">
            <p class="mt-1 text-gray-200">
              Résultat détaillé
            </p>
          </div>
          <div class="p-4 flex flex-col w-full py-3">
            <div v-if="data.impotCalcule !== data.impotRetenu"
                 class="flex flex-row w-full overflow-hidden py-1 justify-between border-b">
              <span>
                Impôt brut
                <span v-if="data.impotCalcule !== data.impotPlafonne">
                  avant plafonnement QF
                </span>
              </span>
              <div>
                <span v-if="data.impotParPart > 0" class="italic font-light mr-1">
                  ({{ formatPrice(data.impotParPart) }} x {{ data.nbParts }})
                </span>
                <span class="font-bold">
                  {{ formatPrice(data.impotCalcule) }}
                </span>
              </div>
            </div>
            <div v-if="data.impotCalcule !== data.impotPlafonne"
                 class="flex flex-row w-full py-1 justify-between border-b">
              <div>
                Impôt après plafonnement QF
                <div class="inline-block tooltip ml-1">
                  <svg aria-haspopup="true" xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-info-circle"
                       width="25" height="25" viewBox="0 0 24 24" stroke-width="1.5" stroke="#A0AEC0" fill="none"
                       stroke-linecap="round" stroke-linejoin="round">
                    <path stroke="none" d="M0 0h24v24H0z"/>
                    <circle cx="12" cy="12" r="9"/>
                    <line x1="12" y1="8" x2="12.01" y2="8"/>
                    <polyline points="11 12 12 12 12 16 13 16"/>
                  </svg>
                  <div class="flex flex-col p-4 bg-white w-96 rounded-md z-20 absolute right-0 invisible tooltip-item text-black">
                    Vous bénéficiez de demi-parts supplémentaires qui ont notamment pour effet de réduire votre impôt.
                    Cette réduction est toutefois limitée à un montant en fonction de votre situation de famille et du
                    nombre de personnes à votre charge, c'est le principe du plafonnement du quotient familial.
                  </div>
                </div>
              </div>
              <span class="font-bold">
                {{ formatPrice(data.impotPlafonne) }}
              </span>
            </div>
            <div v-if="data.decote > 0" class="flex flex-row w-full overflow-hidden py-1 justify-between border-b">
          <span>
            Décote
          </span>
              <span class="font-bold">
            {{ formatPrice(data.decote) }}
          </span>
            </div>
            <div v-if="data.reductionsRetenues > 0"
                 class="flex flex-row w-full overflow-hidden py-1 justify-between border-b">
          <span>
            Réductions d'impôts retenues
          </span>
              <div>
            <span v-if="data.reductions !== data.reductionsRetenues" class="font-bold italic line-through mr-2">
              {{ formatPrice(data.reductions) }}
            </span>
                <span class="font-bold">
              {{ formatPrice(data.reductionsRetenues) }}
            </span>
              </div>
            </div>
            <div v-if="data.creditRetenus > 0"
                 class="flex flex-row w-full overflow-hidden py-1 justify-between border-b">
          <span>
            Impôt avant crédit d'impôt
          </span>
              <span class="font-bold">
            {{ formatPrice(data.impotAvantCredit) }}
          </span>
            </div>
            <div v-if="data.creditRetenus > 0"
                 class="flex flex-row w-full overflow-hidden py-1 justify-between border-b">
              <span>
                Crédit d'impôt retenu
              </span>
              <div>
                <span v-if="data.credit !== data.creditRetenus" class="font-bold italic line-through mr-2">
                  {{ formatPrice(data.credit) }}
                </span>
                <span class="font-bold">
                  {{ formatPrice(data.creditRetenus) }}
                </span>
              </div>
            </div>
            <div class="flex flex-row w-full overflow-hidden py-1 justify-between font-bold text-lg">
              <span>
                Impôt retenu
              </span>
              <span class="font-bold">
                {{ formatPrice(data.impotRetenu) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import {ref} from 'vue'
import {useNotification} from "@kyvg/vue3-notification";
import SampleInfos from "@/components/SampleInfos.vue";
import Form from "@/components/Form.vue";

const inputs = [];
['TR0', 'TR11', 'TR30', 'TR41', 'TR45'].forEach((tranche, index) => inputs.push({
  type: 'record',
  reference: `TRANCHES[${index}]`,
  value: tranche
}));

const data = ref(null)

function formatPrice(value) {
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " €"
}

const results = ref()
const {notify} = useNotification()

const parseResult = (values) => {
  data.value = {}
  const assiette = values.find(variable => variable.definitionReference === 'ASSIETTE')
  data.value.revenus = assiette.subVariables.find(variable => variable.definitionReference === 'ASSIETTE/REVENUS_NET').value
  data.value.reductions = assiette.subVariables.find(variable => variable.definitionReference === 'ASSIETTE/REDUCTIONS_IMPOTS').value
  data.value.credit = assiette.subVariables.find(variable => variable.definitionReference === 'ASSIETTE/CREDIT_IMPOT').value
  const foyer = values.find(variable => variable.definitionReference === 'FOYER')
  data.value.nbParts = foyer.subVariables.find(variable => variable.definitionReference === 'FOYER/NB_PARTS_QF').value
  const calcul = values.find(variable => variable.definitionReference === 'CALCUL')
  data.value.tranches = calcul.subVariables.filter(variable => variable.definitionReference === 'CALCUL/IMPOT_PAR_TRANCHE')
    .map(tranche => {
      return {
        taux: tranche.loopStep["TRANCHE"]["TAUX"].value,
        min: tranche.loopStep["TRANCHE"]["MIN"].value,
        max: tranche.loopStep["TRANCHE"]["MAX"].value,
        montant: tranche.subVariables.find(variable => variable.definitionReference === 'CALCUL/IMPOT_PAR_TRANCHE/MONTANT').value,
        impot: tranche.subVariables.find(variable => variable.definitionReference === 'CALCUL/IMPOT_PAR_TRANCHE/AVEC_QF').value
      }
    })
  data.value.impotParPart = Number.parseFloat(data.value.tranches.reduce((acc, next) => next.impot + acc, 0)).toFixed(2)
  data.value.impotCalcule = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/IMPOT_AVEC_QF').value
  data.value.impotPlafonne = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/DROIT_SIMPLE').value
  data.value.decote = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/DECOTE').value
  data.value.reductionsRetenues = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/REDUCTIONS_RETENUES').value
  data.value.impotAvantCredit = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/IMPOT_AVANT_CREDIT').value
  data.value.creditRetenus = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/CREDIT_RETENU').value
  data.value.impotRetenu = calcul.subVariables.find(variable => variable.definitionReference === 'CALCUL/IMPOT_RETENU').value

  setTimeout(() => results.value.scrollIntoView({ behavior: "smooth" }), 150);
}

const onError = (e) => {
  console.log(e)
  notify({
    type: 'error',
    title: "Erreur",
    duration: 8000,
    text: "Une erreur est survenue lors de la simulation de l'impôt sur le revenu",
  });
}

</script>
<style>
.tooltip:hover .tooltip-item {
  visibility: visible;
}
</style>