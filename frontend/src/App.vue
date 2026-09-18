<script setup lang="ts">
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import { RouterView } from 'vue-router';
</script>


<template>
  
  <main>
    <Header />
    <div class="min-h-screen bg-slate-950 text-white">
    <RouterView />
    </div>
    <Footer />
  </main>

  
</template>