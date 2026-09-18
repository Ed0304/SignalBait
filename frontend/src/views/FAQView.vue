<script setup lang="ts">
import DropdownBox from '@/components/DropdownBox.vue';
import { FAQ } from '@/data/faq';
import type { QNA } from '@/data/faq';
import BackButton from '@/components/BackButton.vue';
</script>



<template>
  <main class="mx-auto max-w-4xl px-6 py-12">
    <h1 class="text-4xl font-bold text-white">
      Frequently Asked Questions
    </h1>

    <div class="mt-8 flex flex-col gap-4">
      <DropdownBox
        v-for="item in FAQ"
        :key="item.question"
        :qna="item"
      />
    </div>
    <div class="mt-8 flex flex-col gap-4">
      <BackButton/>
    </div>
    
  </main>
</template>