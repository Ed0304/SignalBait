<script setup lang="ts">
import { ref } from 'vue'

const API_BASE_URL = import.meta.env.VITE_BASE_API_URL
const ticketNumber = ref('')
const reporterEmail = ref('')
const loading = ref(false)
const errorMessage = ref('')
const ticket = ref<null | {
  ticketNumber: number
  issueType: string
  createdAt: string
  ticketStatus: string
}>(null)

async function trackTicket() {
  loading.value = true
  errorMessage.value = ''
  ticket.value = null
  try {
    const response = await fetch(`${API_BASE_URL}/api/tickets/track`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        ticketNumber: Number(ticketNumber.value),
        reporterEmail: reporterEmail.value.trim()
      })
    })
    const body = await response.json().catch(() => ({}))
    if (!response.ok) {
      throw new Error(body.message || 'Unable to find this ticket.')
    }
    ticket.value = body
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Unable to track this ticket.'
  } finally {
    loading.value = false
  }
}

function formatDate(value: string) {
  return new Date(value).toLocaleString()
}
</script>

<template>
  <section class="mx-auto w-full max-w-xl rounded-2xl border border-white/10 bg-[#0D0D0D] p-8 text-white">
    <h1 class="text-2xl font-bold">Track a Ticket</h1>
    <p class="mt-2 text-sm text-gray-400">Enter the ticket number and the email address used when reporting the issue.</p>

    <form class="mt-6 space-y-5" @submit.prevent="trackTicket">
      <div>
        <label for="ticket-number" class="mb-2 block text-sm font-medium text-gray-300">Ticket number</label>
        <input id="ticket-number" v-model.trim="ticketNumber" type="number" min="1" step="1" required class="w-full rounded-lg border border-white/10 bg-[#151515] px-4 py-3 text-white outline-none focus:border-[#00ff66]" />
      </div>
      <div>
        <label for="track-email" class="mb-2 block text-sm font-medium text-gray-300">Email address</label>
        <input id="track-email" v-model.trim="reporterEmail" type="email" maxlength="254" autocomplete="email" required class="w-full rounded-lg border border-white/10 bg-[#151515] px-4 py-3 text-white outline-none focus:border-[#00ff66]" />
      </div>
      <p v-if="errorMessage" role="alert" class="text-sm text-red-400">{{ errorMessage }}</p>
      <button type="submit" :disabled="loading" class="rounded-lg bg-[#00ff66] px-5 py-2.5 font-semibold text-black disabled:opacity-60">{{ loading ? 'Checking…' : 'Check Status' }}</button>
    </form>

    <div v-if="ticket" class="mt-6 rounded-lg border border-white/10 bg-[#151515] p-5" aria-live="polite">
      <p class="text-sm text-gray-400">Ticket #{{ ticket.ticketNumber }}</p>
      <p class="mt-2 text-lg font-semibold">Status: <span class="text-[#00ff66]">{{ ticket.ticketStatus.replaceAll('_', ' ') }}</span></p>
      <p class="mt-2 text-sm text-gray-300">Issue: {{ ticket.issueType.replaceAll('_', ' ') }}</p>
      <p class="mt-1 text-sm text-gray-400">Created: {{ formatDate(ticket.createdAt) }}</p>
    </div>
  </section>
</template>
