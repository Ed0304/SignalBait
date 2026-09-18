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
        class="rounded bg-slate-500 px-6 py-3 font-semibold text-white shadow transition-colors
               hover:bg-white-500
               focus:outline-none focus:ring-2 focus:ring-white-500 focus:ring-offset-2 focus:ring-offset-slate-950"
        type="button"
    >
        Back
    </button>
</template>