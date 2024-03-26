<template>
  <section>
    <div class="px-6 pt-6">
      <div class="mt-4 grid grid-cols-1 lg:grid-cols-2">
        <SampleCard link="/car-insurance-quote" lang="Français" img="car" title="Calculer un tarif assurance auto" />
        <SampleCard link="/discount" lang="English" img="promo" title="Calculate the amount of a discount" />
        <SampleCard link="/impot" lang="Français" img="impot" title="Simuler l'impôt sur le revenu" />
      </div>
    </div>
  </section>
</template>
<script setup>
import SampleCard from "@/components/SampleCard.vue";
</script>