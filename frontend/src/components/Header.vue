<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<template>
  <header class="border-b border-white/10 bg-slate-950">
    <nav class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
      
      <!-- Logo -->
      <RouterLink
        to="/"
        class="text-xl font-bold tracking-tight text-white"
        >
        SignalBait
        </RouterLink>

      <!-- Navigation -->
      <div class="flex items-center gap-8">
        <RouterLink
          to="/"
          class="text-sm font-medium text-slate-400 transition-colors hover:text-white"
        >
          Home
        </RouterLink>

        <RouterLink
          to="/FAQ"
          class="text-sm font-medium text-slate-400 transition-colors hover:text-white"
        >
          FAQ
        </RouterLink>

        <RouterLink
          to="/analyze"
          class="text-sm font-medium text-slate-400 transition-colors hover:text-white"
        >
          Analyze
        </RouterLink>
      </div>

    </nav>
  </header>
</template>

<style scoped>
</style>