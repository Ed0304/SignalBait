<script setup lang="ts">
import BackButton from '@/components/BackButton.vue'
</script>

<template>
  <main class="bg-[#050505]">
    <!-- Hero -->
    <section
      class="mx-auto flex min-h-[calc(100vh-80px)] max-w-6xl flex-col items-center justify-center px-6 py-20 text-center"
    >
      <!-- Status badge -->
      <span
        class="mb-6 rounded-full border border-[#00ff66]/30 bg-[#00ff66]/5 px-4 py-2 text-sm font-medium text-[#00ff66]"
      >
        Stay alert. Think before you click.
      </span>

      <!-- Main heading -->
      <h1
        class="max-w-4xl text-5xl font-bold tracking-tight text-white sm:text-6xl lg:text-7xl"
      >
        Catch the
        <span class="text-[#00ff66]"> red flags</span>
        before proceeding.
      </h1>

      <!-- Description -->
      <p
        class="mt-6 max-w-2xl text-lg leading-8 text-gray-400 sm:text-xl"
      >
        Got a suspicious message or link?
        SignalBait helps you identify security red flags
        and understand why they matter.
      </p>

      <!-- Actions -->
      <div class="mt-10 flex flex-col items-center gap-4 sm:flex-row">
        <RouterLink
          to="/analyze"
          class="inline-flex items-center rounded-xl bg-[#00ff66] px-6 py-3 text-base font-semibold text-black shadow-[0_0_20px_rgba(0,255,102,0.08)] transition-all hover:bg-[#00e65c] hover:shadow-[0_0_25px_rgba(0,255,102,0.2)]"
        >
          Analyze a message
        </RouterLink>

        <RouterLink
          to="/FAQ"
          class="inline-flex items-center rounded-xl border border-white/10 bg-[#0D0D0D] px-6 py-3 text-base font-semibold text-gray-300 transition-all hover:border-[#00ff66]/40 hover:bg-[#111111] hover:text-[#00ff66]"
        >
          First time here? Read the FAQs
        </RouterLink>
      </div>

      <!-- Supporting text -->
      <p class="mt-6 text-sm text-gray-600">
        Transparent security analysis · AI-assisted explanations
      </p>
    </section>
  </main>
</template>