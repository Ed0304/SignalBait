<script setup lang="ts">
import { ref } from 'vue'
import type { QNA } from '../data/faq'

defineProps<{
  qna: QNA // var_name: interface
}>()

const extended = ref(false)
</script>

<template>
  <div
    class="overflow-hidden rounded-lg border border-white/10 bg-[#0D0D0D]
           transition-colors hover:border-[#00ff66]/30"
  >
    <button
      class="flex w-full items-center justify-between px-6 py-4
             text-left text-white transition-colors
             hover:text-[#00ff66]"
      @click="extended = !extended"
    >
      <span>{{ qna.question }}</span>

      <!-- Green accent when the FAQ is expanded -->
      <span
        class="text-xl font-light text-[#00ff66]"
      >
        {{ extended ? '−' : '+' }}
      </span>
    </button>

    <div
      v-if="extended"
      class="border-t border-white/10 px-6 py-4 text-gray-400"
    >
      {{ qna.answer }}
    </div>
  </div>
</template>