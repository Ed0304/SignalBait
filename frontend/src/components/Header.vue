
<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'

const menuOpen = ref(false)

function closeMenu() {
  menuOpen.value = false
}
</script>

<template>
  <header class="border-b border-white/10 bg-[#050505]">
    <nav
      class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4"
    >
      <!-- Logo -->
      <RouterLink
        to="/"
        @click="closeMenu"
        class="text-xl font-bold tracking-tight text-white transition-colors hover:text-[#00ff66]"
      >
        Signal<span class="text-[#00ff66]">Bait</span>
      </RouterLink>

      <!-- Desktop navigation -->
      <div class="hidden items-center gap-8 md:flex">
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

      <!-- Mobile hamburger button -->
      <button
        type="button"
        class="inline-flex items-center justify-center rounded-lg p-2 text-gray-300 transition-colors hover:text-[#00ff66] focus:outline-none focus:ring-2 focus:ring-[#00ff66] md:hidden"
        :aria-expanded="menuOpen"
        aria-controls="mobile-navigation"
        :aria-label="
          menuOpen ? 'Close navigation menu' : 'Open navigation menu'
        "
        @click="menuOpen = !menuOpen"
      >
        <!-- Hamburger icon -->
        <svg
          v-if="!menuOpen"
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M4 6h16M4 12h16M4 18h16"
          />
        </svg>

        <!-- Close icon -->
        <svg
          v-else
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M6 6l12 12M18 6L6 18"
          />
        </svg>
      </button>
    </nav>

    <!-- Mobile dropdown menu -->
    <div
      v-if="menuOpen"
      id="mobile-navigation"
      class="border-t border-white/10 bg-[#050505] px-6 py-4 md:hidden"
    >
      <div class="mx-auto flex max-w-6xl flex-col gap-1">
        <RouterLink
          to="/"
          @click="closeMenu"
          class="rounded-lg px-3 py-3 text-sm font-medium text-gray-400 transition-colors hover:bg-white/5 hover:text-[#00ff66]"
        >
          Home
        </RouterLink>

        <RouterLink
          to="/FAQ"
          @click="closeMenu"
          class="rounded-lg px-3 py-3 text-sm font-medium text-gray-400 transition-colors hover:bg-white/5 hover:text-[#00ff66]"
        >
          FAQ
        </RouterLink>

        <RouterLink
          to="/analyze"
          @click="closeMenu"
          class="rounded-lg px-3 py-3 text-sm font-medium text-gray-400 transition-colors hover:bg-white/5 hover:text-[#00ff66]"
        >
          Analyze
        </RouterLink>

        <RouterLink
          to="/trackticket"
          @click="closeMenu"
          class="rounded-lg px-3 py-3 text-sm font-medium text-gray-400 transition-colors hover:bg-white/5 hover:text-[#00ff66]"
        >
          Track Ticket
        </RouterLink>
      </div>
    </div>
  </header>
</template>
