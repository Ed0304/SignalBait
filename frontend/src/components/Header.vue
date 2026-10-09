<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <header class="border-b border-white/10 bg-[#050505]">
    <nav class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">

      <!-- Logo -->
      <RouterLink
        to="/"
        class="text-xl font-bold tracking-tight text-white transition-colors hover:text-[#00ff66]"
      >
        Signal<span class="text-[#00ff66]">Bait</span>
      </RouterLink>

      <!-- Navigation -->
      <div class="flex items-center gap-8">

        <RouterLink
          to="/"
          class="text-sm font-medium text-gray-400 transition-colors hover:text-[#00ff66]"
        >
          Home
        </RouterLink>

        <RouterLink
          to="/FAQ"
          class="text-sm font-medium text-gray-400 transition-colors hover:text-[#00ff66]"
        >
          FAQ
        </RouterLink>

        <RouterLink
          to="/analyze"
          class="text-sm font-medium text-gray-400 transition-colors hover:text-[#00ff66]"
        >
          Analyze
        </RouterLink>

        <RouterLink
          to="/trackticket"
          class="text-sm font-medium text-gray-400 transition-colors hover:text-[#00ff66]"
        >
          Track Ticket
        </RouterLink>

      </div>

    </nav>
  </header>
</template>

<style scoped>
</style>