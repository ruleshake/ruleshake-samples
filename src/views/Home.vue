<template>
    <section>
        <div class="px-6 pt-10">
            <h1 class="text-3xl capitalize text-center text-gray-800 dark:text-white lg:text-4xl mb-12">
                RuleShake usage samples :
            </h1>

            <div class="mt-8 grid grid-cols-2">
                <router-link to="/car-insurance-quote">
                    <div class="group flex transform cursor-pointer flex-col items-center rounded-xl p-8 transition-colors duration-300 hover:bg-blue-600">
                        <img class="h-64 w-64  object-cover "
                             src="@/assets/car.png"
                             alt=""/>

                        <h1 class="mt-4 text-3xl font-semibold group-hover:text-white text-white">
                            Create a car insurance quote
                        </h1>
                    </div>
                </router-link>
                <router-link to="/discount">
                    <div class="group flex transform cursor-pointer flex-col items-center rounded-xl p-8 transition-colors duration-300 hover:bg-blue-600">
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