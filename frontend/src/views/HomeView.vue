<script setup lang="ts">

</script>

<template>
    <main>
      <!-- Hero -->
      <section
        class="mx-auto flex min-h-[calc(100vh-80px)] max-w-6xl flex-col items-center justify-center px-6 py-20 text-center"
      >
        <span
          class="mb-6 rounded-full border border-red-400/30 bg-red-400/10 px-4 py-2 text-sm font-medium text-red-300"
        >
          Stay alert. Think before you click.
        </span>

        <h1 class="max-w-4xl text-5xl font-bold tracking-tight sm:text-6xl lg:text-7xl">
          Catch the
          <span class="text-red-400"> red flags</span>
          before proceeding.
        </h1>

        <p class="mt-6 max-w-2xl text-lg leading-8 text-slate-300 sm:text-xl">
          Got a suspicious message or link?
          SignalBait helps you identify security red flags
          and understand why they matter.
        </p>

        <div class="mt-10">
          <RouterLink
            to="/analyze"
            class="inline-flex items-center rounded-xl bg-red-500 px-6 py-3 text-base font-semibold text-white shadow-lg transition hover:bg-red-400 hover:shadow-red-500/20"
          >
            Analyze a message
          </RouterLink>
          <br/>
          <br/>
          <RouterLink
            to="/FAQ"
            class="inline-flex items-center rounded-xl bg-green-500 px-6 py-3 text-base font-semibold text-white shadow-lg transition hover:bg-green-400 hover:shadow-red-500/20"
          >
            First Time here? Read the FAQs first.
          </RouterLink>
        </div>

        <p class="mt-6 text-sm text-slate-500">
          Transparent security analysis · AI-assisted explanations
        </p>
      </section>
    </main>
</template>