<template>
  <notifications/>
  <main class="bg-gray-700 min-h-screen flex flex-col">
    <Toolbar class="max-lg:hidden"/>
    <Sidebar class="lg:hidden"/>
    <div class="container mx-auto grow">
      <router-view :key="$route.fullPath"/>
    </div>
    <footer>
      <div class="container mx-auto px-6 pb-12">
        <hr class="border-gray-300 md:my-8"/>
        <div class="text-white text-center">
          <span>{{ new Date().getFullYear() }} — <strong>RuleShake</strong></span>
        </div>
      </div>
    </footer>
  </main>
</template>

<script setup>
//<a href="https://www.flaticon.com/fr/icones-gratuites/voiture" title="voiture icônes">Voiture icônes créées par Freepik - Flaticon</a>
import {Notifications} from "@kyvg/vue3-notification";
import Toolbar from "@/components/Toolbar.vue";
import Sidebar from "@/components/Sidebar.vue";
</script>