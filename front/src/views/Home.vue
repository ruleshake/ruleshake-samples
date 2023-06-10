<template>
    <section>
        <div class="px-6 pt-10">
            <h1 class="text-3xl text-center text-gray-100 dark:text-white lg:text-4xl mb-12">
                RuleShake usage samples :
            </h1>

            <div class="mt-8 grid grid-cols-2">
                <router-link to="/car-insurance-quote">
                    <div class="group flex transform cursor-pointer flex-col items-center border border-gray-500 rounded-xl p-8 transition-colors duration-300 hover:bg-blue-600 mr-2">
                        <span class="bg-blue-200 text-sm font-medium text-blue-800 text-center p-1 leading-none rounded-full px-2 dark:bg-blue-900 dark:text-blue-200 absolute left-auto top-2 right-2">Français</span>
                        <img class="h-64 w-64  object-cover "
                             src="@/assets/car.png"
                             alt=""/>

                        <h1 class="mt-4 text-3xl font-semibold group-hover:text-white text-white">
                            Calculer un tarif assurance auto
                        </h1>
                    </div>
                </router-link>
                <router-link to="/discount">
                    <div class="group flex transform cursor-pointer flex-col items-center border border-gray-500 rounded-xl p-8 transition-colors duration-300 hover:bg-blue-600 ml-2">
                        <span class="bg-blue-200 text-sm font-medium text-blue-800 text-center p-1 leading-none rounded-full px-2 dark:bg-blue-900 dark:text-blue-200 absolute left-auto top-2 right-2">English</span>
                        <img class="h-64 w-64  object-cover "
                             src="@/assets/promo.png"
                             alt=""/>

                        <h1 class="mt-4 text-3xl font-semibold group-hover:text-white text-white">
                            Calculate the amount of a discount
                        </h1>
                    </div>
                </router-link>
            </div>
        </div>
    </section>
</template>
<script setup>
</script>