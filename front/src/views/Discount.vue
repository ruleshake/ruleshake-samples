<template>
    <SampleInfos title="Discount calculation">
        <div class="flex p-4 mb-4 text-sm rounded-lg bg-gray-800 text-yellow-300" role="alert">
            <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd"></path></svg>
            <span class="sr-only">Info</span>
            <div>This is only an example of RuleShake's services implementation</div>
        </div>
        <p>
            This example represents a mini e-commerce site with calculation of promotions.
        </p>
        <p>
            To do this, we created a collection in RuleShake Catalog containing a multiple variable representing the items in the cart. The items are defined in a dataset present in RuleShake Referential
        </p>
        <p>
            Select your products to compose your basket and view the promotions assigned. The table at the top right shows the available promotions.
        </p>
    </SampleInfos>

  <div class="mx-4 mt-4 lg:hidden px-4 bg-white rounded">
    <div class="divide-y divide-gray-100">
      <details class="group">
        <summary
          class="flex cursor-pointer list-none items-center justify-between py-4 text-lg font-medium text-secondary-900 group-open:text-primary-500">
          Available rewards
          <div>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="block h-5 w-5 group-open:hidden">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="hidden h-5 w-5 group-open:block">
              <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12h-15" />
            </svg>
          </div>
        </summary>
        <div class="pb-4 text-secondary-500">
          <table class="w-full text-sm text-left text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th scope="col" class="px-4 py-3">
                Label
              </th>
              <th scope="col" class="px-4 py-3">
                Description
              </th>
            </tr>
            </thead>
            <tbody>
            <tr class="bg-white border-b" v-for="reward in rewards">
              <th scope="row" class="px-4 py-2 font-medium text-gray-900 whitespace-nowrap">
                {{ reward.label }}
              </th>
              <td class="px-2 py-3">
                {{ reward.description }}
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </details>
    </div>
  </div>

    <div v-if="initialLoading"
         class="fixed top-0 left-0 right-0 bottom-0 w-full h-screen z-50 overflow-hidden bg-gray-700 opacity-75 flex flex-col items-center justify-center">
        <div role="status">
            <svg aria-hidden="true"
                 class="inline w-10 h-10 mr-2 text-gray-200 animate-spin fill-blue-600"
                 viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                      fill="currentColor"/>
                <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                      fill="currentFill"/>
            </svg>
            <span class="text-white text-2xl ml-2">Loading...</span>
        </div>
    </div>
    <div class="mx-auto flex lg:flex-row flex-col flex-wrap py-6">
        <div class="lg:w-2/3 w-full items-center px-3">
            <div class="grid grid-cols-2 lg:grid-cols-4 grid-flow-row gap-4 max-lg:mb-96">
                <div v-for="article in articles" class="m-1">
                    <div class="w-full h-full max-w-sm bg-white border border-gray-200 rounded-lg shadow flex flex-col justify-between">
                        <div>
                            <div class="flex justify-end mt-2">
                                <span class="text-right bg-blue-100 text-blue-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded border border-blue-400">
                                    {{ article.category }}
                                </span>
                            </div>
                            <img class="p-4 rounded-t-lg mx-auto h-36" :src="article.image" alt="product image"/>
                            <div class="px-4 pb-4 text-center">
                                <span class="text-xl font-normal font-semibold tracking-tight text-gray-900">
                                    {{ article.label }}
                                </span>
                            </div>
                        </div>
                        <div class="flex items-center justify-between mt-2 pl-4 px-2 pb-2">
                            <span class="text-lg lg:text-lg font-semibold text-gray-900">
                                {{ article.price }} €
                            </span>
                            <button
                               class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-3 lg:px-5 py-2.5 text-center"
                               @click="addToCart(article)"
                            >
                                Add to cart
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="lg:w-1/3 w-full flex flex-col items-center px-3">

            <div class="w-full shadow-md my-2 max-lg:hidden">
                <table class="w-full text-sm text-left text-gray-500">
                    <caption class="p-3 text-lg font-semibold text-left text-gray-900 bg-white">
                        Available rewards
                    </caption>
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                    <tr>
                        <th scope="col" class="px-4 py-3">
                            Label
                        </th>
                        <th scope="col" class="px-4 py-3">
                            Description
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="bg-white border-b" v-for="reward in rewards">
                        <th scope="row" class="px-4 py-2 font-medium text-gray-900 whitespace-nowrap">
                            {{ reward.label }}
                        </th>
                        <td class="px-2 py-3">
                            {{ reward.description }}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="max-lg:fixed max-lg:bottom-0 w-full bg-white shadow flex flex-col lg:my-2 p-6 font-mono">
                <div class="flex justify-between mb-4">
                    <span class="text-xl font-semibold pb-5">Receipt</span>
                    <button type="button" @click="resetCart" class="text-gray-900 bg-white hover:bg-gray-100 border border-gray-200 focus:ring-4 focus:outline-none focus:ring-gray-100 font-medium rounded text-sm px-5 py-2.5 text-center inline-flex items-center">
                        <svg fill="none" class="w-6 h-5 mr-2 -ml-1" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99"></path>
                        </svg>
                        Reset cart
                    </button>
                </div>
                <div v-for="item in cart" class="flex justify-between w-128 text-sm">
                    <div>
                        <span>{{ item.quantity }}</span>
                        <span class="ml-2">{{ item.label }}</span>
                    </div>
                    <div class="grid justify-items-end">
                        <span v-if="item.quantity > 1">{{ item.quantity }} x {{ item.price }} €</span>
                        <span>{{ parseFloat((item.price * item.quantity).toFixed(2)) }} €</span>
                    </div>
                </div>
                <div v-if="cart.length > 0">
                    <div v-if="totals.discount > 0">
                        <hr class="w-64 h-px my-4 bg-gray-200 border-0 mx-auto">
                        <p class="text-center">Rewards</p>
                        <div v-for="item in rewards.filter(item => item.amount > 0)" class="flex justify-between w-128 text-sm">
                            <span>{{ item.label }}</span>
                            <div v-if="loading" role="status">
                                <svg aria-hidden="true" class="inline w-4 h-4 mr-2 text-gray-200 animate-spin fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
                                    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
                                </svg>
                                <span class="sr-only">Loading...</span>
                            </div>
                            <span v-else class="ml-2">{{ item.amount }} €</span>
                        </div>
                    </div>
                    <hr class="w-64 h-px my-4 bg-gray-200 border-0 mx-auto">
                    <div class="grid justify-items-end">
                        <span class="text-sm">Initial : {{ totals.initial }} €</span>
                        <span class="text-sm">Rewards : {{ totals.discount }} €</span>
                        <span class="text-xl">Total : {{ parseFloat((totals.initial - totals.discount).toFixed(2)) }} €</span>
                    </div>
                </div>
                <span v-else>Please add articles to cart</span>
            </div>
        </div>
    </div>
</template>
<style>
</style>
<script setup>
import {onMounted, ref} from 'vue'
import SampleInfos from "@/components/SampleInfos.vue";

const API_URL = import.meta.env.VITE_RULESHAKE_SAMPLES_API_URL;

const initialLoading = ref(true)
const loading = ref(false)
const articles = ref([])
const cart = ref([])
const rewards = ref([])
const totals = ref({
    initial: 0,
    discount: 0
})

const fetchArticles = async () => {
    const response = fetch(`${API_URL}/discount/articles`)
        .then(response => {
            if (response.status >= 400) {
                throw response
            }
            return response.json();
        })
    let list = await response;
    articles.value = list.map(obj => ({...obj, quantity: 1}));
};

onMounted(async () => {
    await fetchArticles()
    await computeRewards()
    initialLoading.value = false
});

const addToCart = async (article) => {
    loading.value = true
    const i = cart.value.findIndex(e => e.barcode === article.barcode);
    if (i > -1) {
        article.quantity = article.quantity + 1
    } else {
        cart.value.push(article)
    }
    await computeTotals()
    loading.value = false
}

const computeRewards = async () => {
    const inputs = cart.value.flatMap(item => Array(item.quantity).fill({barcode: item.barcode}));
    const response = fetch(`${API_URL}/discount/apply`, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(inputs)
    })
        .then(response => {
            if (response.status >= 400) {
                throw response
            }
            return response.json();
        })
    rewards.value = await response
}

const computeTotals = async () => {
    await computeRewards()
    const initial = cart.value.reduce((acc, item) => acc + parseFloat((item.price * item.quantity).toFixed(2)), 0)
    const discount = rewards.value.reduce((acc, item) => acc + parseFloat(item.amount.toFixed(2)), 0)
    totals.value = {
        initial: parseFloat(initial.toFixed(2)),
        discount: parseFloat(discount.toFixed(2))
    }
}

const resetCart = () => {
    cart.value = []
    articles.value.forEach(article => article.quantity = 1)
}
</script>