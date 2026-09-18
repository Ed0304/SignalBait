<script setup lang="ts">
import { ref } from 'vue'
import type { QNA } from '../data/faq'

defineProps<{
  qna: QNA //var_name: interface
}>()

const extended = ref(false)
</script>

<template>
  <div class="rounded-lg border border-white/10 bg-slate-900">
    <button
      class="flex w-full items-center justify-between px-6 py-4 text-left text-white"
      @click="extended = !extended"
    >
      <span>{{ qna.question }}</span>

      <span>
        {{ extended ? '−' : '+' }}
      </span>
    </button>

    <div
      v-if="extended"
      class="border-t border-white/10 px-6 py-4 text-slate-400"
    >
      {{ qna.answer }}
    </div>
  </div>
</template>