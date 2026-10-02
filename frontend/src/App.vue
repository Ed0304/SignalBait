<script setup lang="ts">
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import { RouterView } from 'vue-router';
</script>


<template>
  <main class="min-h-screen bg-[#050505] text-white">
    <Header />

    <div
      class="min-h-screen bg-[#050505] text-white
             selection:bg-[#00ff66] selection:text-black"
    >
      <RouterView />
    </div>

    <Footer />
  </main>
</template>