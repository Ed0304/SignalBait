<script setup lang="ts">
import BackButton from '@/components/BackButton.vue';
import { ref } from 'vue'

const inputMode = ref<'text' | 'image'>('text')
</script>

<template>
  <main class="mx-auto max-w-4xl px-6 py-12">
    <h1 class="text-4xl font-bold text-white">
      Analyze
    </h1>

    <h2 class="mt-4 text-xl font-bold text-white">
      If you are new here, please read the FAQ first:
      <RouterLink
        to="/faq"
        class="text-red-400 underline underline-offset-4 transition-colors hover:text-red-300"
      >
        FAQ
      </RouterLink>
    </h2>

    <div class="mt-8">
      <form class="flex flex-col gap-6">

        <!-- Input mode selector -->
        <div class="flex w-fit rounded-lg border border-white/10 bg-slate-900 p-1">
          <button
            type="button"
            @click="inputMode = 'text'"
            :class="[
              'rounded-md px-5 py-2 text-sm font-medium transition-colors',
              inputMode === 'text'
                ? 'bg-white text-slate-950'
                : 'text-slate-400 hover:text-white'
            ]"
          >
            Text
          </button>

          <button
            type="button"
            @click="inputMode = 'image'"
            :class="[
              'rounded-md px-5 py-2 text-sm font-medium transition-colors',
              inputMode === 'image'
                ? 'bg-white text-slate-950'
                : 'text-slate-400 hover:text-white'
            ]"
          >
            Image
          </button>
        </div>
        <div class="h-48">
        <!-- Text input -->
        <textarea
          v-if="inputMode === 'text'"
          placeholder="Paste suspicious message here..."
          class="min-h-48 w-full resize-y rounded-lg border border-white/10 bg-slate-900 px-4 py-3 text-white placeholder:text-slate-500 outline-none transition-colors focus:border-red-500 focus:ring-1 focus:ring-red-500"
        ></textarea>

        <!-- Image input -->
        <label
          v-else
          class="flex min-h-48 cursor-pointer flex-col items-center justify-center rounded-lg border-2 border-dashed border-white/10 bg-slate-900 px-6 py-10 text-center transition-colors hover:border-red-500/50 hover:bg-slate-900/80"
        >
          <span class="text-lg font-medium text-white">
            Upload an image
          </span>

          <span class="mt-2 text-sm text-slate-400">
            Upload a screenshot of the suspicious message
          </span>

          <span class="mt-4 rounded-md bg-slate-800 px-4 py-2 text-sm font-medium text-slate-300">
            Choose image
          </span>

          <input
            type="file"
            accept="image/*"
            class="hidden"
          />
        </label>
        </div>

        <!-- Submit -->
        <button
          type="submit"
          class="rounded-lg bg-red-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-red-500 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 focus:ring-offset-slate-950"
        >
          Analyze
        </button>
        <BackButton/>
      </form>
      <div class="mt-8">
        <h1 class="text-4xl font-bold text-white">
          ⚠️ Disclaimer
        </h1>
        <br/>
        <p>SignalBait's analysis results may contain mistakes and should not be treated as definitive. If you're unsure whether a message or link is legitimate, contact your bank, service provider, or relevant local authorities through their official channels to verify it.</p>

      </div>
      
    </div>
  </main>
</template>