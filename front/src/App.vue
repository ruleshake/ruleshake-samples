<template>
    <main class="bg-gray-800 min-h-screen flex flex-col justify-between">
        <header class="bg-gray-900">
            <nav class="container bg-gray-900 mx-auto p-3 lg:flex lg:items-center lg:justify-between">
                <div class="flex items-center justify-between">
                    <img alt="" src="./assets/ruleshake-logo-white.png" width="48">
                    <router-link class="text-3xl text-white hover:text-gray-300" to="/">
                        <span>RuleShake</span>
                        <span class="font-thin ml-2">Samples</span>
                    </router-link>
                </div>

                <div class="px-6 py-4  relative flex w-auto items-center p-0 opacity-100">
                    <div class="flex lg:mt-0 flex-row space-y-0">
                        <router-link class="text-gray-200 hover:text-blue-400 mx-6" to="/car-insurance-quote">Car
                            insurance
                        </router-link>
                        <router-link class="text-gray-200 hover:text-blue-400 mx-6" to="/discount">Discount
                            calculation
                        </router-link>
                    </div>

                    <a class="ml-6 block h-10 transform rounded-md border px-5 py-2 text-center text-sm capitalize
                    transition-colors duration-300 text-white hover:bg-gray-700 mt-0 w-auto"
                       href="https://ruleshake.com">About RuleShake</a>
                </div>
            </nav>

        </header>
        <div class="container mx-auto">
            <router-view :key="$route.fullPath"/>
        </div>
        <footer>
            <div class="container mx-auto px-6 py-20">

                <hr class="border-gray-700 md:my-8"/>

                <div class="text-white text-center">
                    <span>{{ new Date().getFullYear() }} — <strong>RuleShake</strong></span>
                </div>
            </div>
        </footer>
    </main>
</template>

<script setup>
//<a href="https://www.flaticon.com/fr/icones-gratuites/voiture" title="voiture icônes">Voiture icônes créées par Freepik - Flaticon</a>
</script>