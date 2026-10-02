<script setup lang="ts">
import { useRouter } from "vue-router";

const router = useRouter();

function goBack() {
  router.go(-1);
}
</script>

<template>
  <button
    @click="goBack"
    type="button"
    class="rounded-lg border border-white/10 bg-[#0D0D0D] px-6 py-3
           font-semibold text-gray-300 shadow transition-colors
           hover:border-[#00ff66]/50 hover:bg-[#00ff66] hover:text-black
           focus:outline-none focus:ring-2 focus:ring-[#00ff66]
           focus:ring-offset-2 focus:ring-offset-[#050505]"
  >
    Back
  </button>
</template>