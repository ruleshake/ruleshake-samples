<template>
    <SampleInfos title="Assurance auto">
        <div class="flex p-4 mb-4 text-sm text-yellow-800 rounded-lg bg-yellow-50 dark:bg-gray-800 dark:text-yellow-300" role="alert">
            <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd"></path></svg>
            <span class="sr-only">Info</span>
            <div>Ceci est uniquement un exemple d'implémentation des services RuleShake</div>
        </div>
        <p>
            Cet exemple permet d'implémenter un formulaire de souscription à une assurance automobile.
            Pour ce faire, nous avons créé une collection dans RuleShake Catalog représentant un produit d'assurance automobile.
        </p>
        <p>
            Cette collection contient des variables représentants le risque auto dont 2 variables liés à des datasets
            (Infos du véhicule et Lieu de stationnement) définis dans RuleShake Referential.
        </p>
        <p>
            La matrice de tarif (qui est une variable calculée) affiche les garanties disponibles pour chaque formule ainsi que son tarif.
        </p>
        <p>
            Aucune modélisation intermédiaire n'a été réalisée, cet écran manipule directement les modèles d'objet de RuleShake.
        </p>
    </SampleInfos>
    <Loading v-if="initialLoading" />
    <div class="container flex flex-row py-6 text-white align-top">
        <section class="w-3/5 px-3 mx-auto">
            <InsuranceForm :variables="variables" @values="values => evaluate(values)"/>
        </section>
        <aside v-if="tarifs && tarifs.size > 0" class="w-2/5 flex flex-col items-center px-3">
            <div class="w-full bg-white shadow p-4 pt-6">
                <ul class="items-center w-full text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-lg sm:flex dark:bg-gray-700 dark:border-gray-600 dark:text-white">
                    <li class="w-full border-b border-gray-200 sm:border-b-0 sm:border-r dark:border-gray-600">
                        <div class="flex items-center pl-3">
                            <input id="horizontal-list-radio-license" type="radio" v-model="selectedFractionnement"
                                   value="Annuel" name="list-radio"
                                   class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700 focus:ring-2 dark:bg-gray-600 dark:border-gray-500">
                            <label for="horizontal-list-radio-license"
                                   class="w-full py-3 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">Annuel</label>
                        </div>
                    </li>
                    <li class="w-full border-b border-gray-200 sm:border-b-0 sm:border-r dark:border-gray-600">
                        <div class="flex items-center pl-3">
                            <input id="horizontal-list-radio-id" type="radio" v-model="selectedFractionnement"
                                   value="Mensuel" name="list-radio"
                                   class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700 focus:ring-2 dark:bg-gray-600 dark:border-gray-500">
                            <label for="horizontal-list-radio-id"
                                   class="w-full py-3 ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">Mensuel</label>
                        </div>
                    </li>
                </ul>
                <div class="grid grid-cols-5 text-gray-900 font-bold mt-6">
                    <div v-for="(cell, index) in getHeaderGrid()" :class="index === 0 ? 'col-span-2 py-2 border-b' : 'py-2 border-b text-center'">{{ cell }}</div>
                </div>
                <div v-for="row in getTarifGrid()" class="grid grid-cols-5 text-gray-900">
                    <div v-for="(cell, index) in row" :class="index === 0 ? 'col-span-2 py-2 border-b' : 'py-2 border-b flex justify-center items-center'">
                        <svg v-if="cell === true" class="stroke-green-700 w-12" fill="none" stroke="currentColor" stroke-width="1" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        <svg v-else-if="!cell" fill="none" class="stroke-red-700 w-12" stroke="currentColor" stroke-width="1" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9.75 9.75l4.5 4.5m0-4.5l-4.5 4.5M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        <span v-else class="font-medium">{{ cell }}</span>
                    </div>
                </div>
                <div class="grid grid-cols-5 text-gray-900">
                    <div v-for="(cell, index) in getTotalGrid()" :class="index === 0 ? 'col-span-2 py-2 border-b' : 'py-2 border-b text-center text-lg font-semibold'">{{ cell }}</div>
                </div>
            </div>
        </aside>
    </div>

</template>
<script setup>
import {onMounted, ref} from 'vue'
import InsuranceForm from "@/components/InsuranceForm.vue";
import {useNotification} from "@kyvg/vue3-notification";
import SampleInfos from "@/components/SampleInfos.vue";
import Loading from "@/components/Loading.vue";

const API_URL = import.meta.env.VITE_RULESHAKE_SAMPLES_API_URL;

const initialLoading = ref(true)
const loading = ref(false)
const variables = ref([])
const tarifs = ref({})
const selectedFractionnement = ref('Annuel')

onMounted(async () => {
    await fetchVariables()
    initialLoading.value = false
})

const computeVariables = (inputs) => {
    const response = fetch(`${API_URL}/collections/ASSURANCE_AUTO/evaluations`, {
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

const fetchVariables = async () => {
    const response = computeVariables([]);
    variables.value = await response;
}

const { notify }  = useNotification()

const evaluate = async (values) => {
    const inputs = Object.entries(values)
        .filter(([reference, value]) => value.value !== undefined)
        .map(([reference, value]) => {
            return {
                reference: reference,
                value: value.value,
                type: value.type
            }
        });
    ['Mini', 'Medium', 'Maxi'].forEach((formule, index) => inputs.push({
        type: 'string',
        reference: `FORMULES[${index}]`,
        value: formule
    }));
    ['Mensuel', 'Annuel'].forEach((fractionnement, index) => inputs.push({
        type: 'string',
        reference: `FRACTIONNEMENTS[${index}]`,
        value: fractionnement
    }));
    const response = computeVariables(inputs);
    try {
        initialLoading.value = true
        const result = await response;
        initialLoading.value = false
        let tmp = result
          .filter(variable => variable.definitionReference === 'TARIF')
          .reduce((acc, item) => {
              const {FORMULE, FRACTIONNEMENT} = item.loopStep;
              const formule = result.find(variable => variable.runtimeReference === FORMULE).value
              const fractionnement = result.find(variable => variable.runtimeReference === FRACTIONNEMENT).value
              if (!acc.has(fractionnement)) {
                  acc.set(fractionnement, new Map());
              }
              const sousMap = acc.get(fractionnement);
              const tarif = {
                  total: item.subVariables.find(variable => variable.definitionReference === 'TARIF/TOTAL').value + ' €',
                  garanties: item.subVariables.find(variable => variable.definitionReference === 'TARIF/GARANTIES')
                    .subVariables
                    .sort((a, b) => (a.orderIndex > b.orderIndex) ? 1 : -1)
                    .map(garantie => {
                        return {
                            code: garantie.definitionReference.split('/').filter(Boolean).pop(),
                            label: garantie.properties["LIBELLE"],
                            included: garantie.subVariables.find(variable => variable.definitionReference.endsWith('INCLUSE')).value,
                            amount: garantie.subVariables.find(variable => variable.definitionReference.endsWith('MONTANT')).value + ' €'
                        }
                    })

              }
              sousMap.set(formule, tarif);
              return acc;
          }, new Map());
        tarifs.value = tmp
    } catch (e) {
        notify({
            type: 'error',
            title: "Erreur",
            duration: 8000,
            text: "Une erreur est survenue lors de l'évaluation du tarif. Assurez-vous que les champs 'Véhicule' et 'Lieu de stationnement' sont renseignés",
        });
    }
}

const getTarifGrid = () => {
    const formules = tarifs.value.get(selectedFractionnement.value)
    let grid = [[]]
    const garanties = [...formules][0][1].garanties.map(garantie => {
        return {
            code: garantie.code,
            label: garantie.label
        }
    });
    garanties.forEach((garantie, index) => {
        let row = []
        formules.forEach((value, key) => {
            row.push(value.garanties.find(gar => gar.code === garantie.code).included)
        })
        grid[index] = [garantie.label, ...row]
    })
    return grid
}

const getHeaderGrid = () => {
    const formules = tarifs.value.get(selectedFractionnement.value)
    return ['', ...formules.keys()]
}

const getTotalGrid = () => {
    const formules = tarifs.value.get(selectedFractionnement.value)
    return ['', ...[...formules.values()].map(formule => formule.total)]
}

</script>