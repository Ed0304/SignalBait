<script setup lang="ts">
import DropdownBox from '@/components/DropdownBox.vue'
import { FAQ } from '@/data/faq'
import BackButton from '@/components/BackButton.vue'
</script>

<template>
  <main class="mx-auto max-w-4xl px-6 py-14">
    <!-- Page heading -->
    <div>
      <p
        class="text-sm font-semibold uppercase tracking-[0.25em] text-[#00ff66]"
      >
        SignalBait
      </p>

      <h1 class="mt-3 text-4xl font-bold tracking-tight text-white sm:text-5xl">
        Frequently Asked Questions
      </h1>

      <p class="mt-4 max-w-2xl text-gray-400">
        Learn how SignalBait works, what its analysis means, and how to use it
        responsibly.
      </p>
    </div>

    <!-- FAQ list -->
    <div class="mt-10 flex flex-col gap-4">
      <DropdownBox
        v-for="item in FAQ"
        :key="item.question"
        :qna="item"
      />
    </div>

    <!-- Back button -->
    <div class="mt-10">
      <BackButton />
    </div>
  </main>
</template>