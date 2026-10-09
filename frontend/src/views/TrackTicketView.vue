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
  <main class="mx-auto w-full max-w-5xl px-6 py-16 text-white">
    <!-- Page heading -->
    <section class="mb-10">
      <p
        class="text-sm font-bold uppercase tracking-[0.3em] text-[#00ff66]"
      >
        Report Tracking
      </p>

      <h1 class="mt-3 text-4xl font-bold tracking-tight sm:text-5xl">
        Track your ticket
      </h1>

      <p class="mt-5 max-w-2xl text-base leading-relaxed text-gray-400">
        Check the latest status of your SignalBait report using your
        ticket number and the email address you provided when reporting.
      </p>
    </section>

    <!-- Tracking form -->
    <section
      class="rounded-2xl border border-white/10 bg-[#0D0D0D] p-6 sm:p-8"
    >
      <h2 class="text-xl font-semibold">Look up your report</h2>

      <p class="mt-2 text-sm text-gray-400">
        Enter your ticket details below to view the current status.
      </p>

      <form
        class="mt-8 space-y-6"
        @submit.prevent="trackTicket"
      >
        <div>
          <label
            for="ticket-number"
            class="mb-2 block text-sm font-medium text-gray-300"
          >
            Ticket number
          </label>

          <input
            id="ticket-number"
            v-model.trim="ticketNumber"
            type="number"
            min="1"
            step="1"
            required
            placeholder="e.g. 12345"
            class="w-full rounded-lg border border-white/10 bg-[#080808] px-4 py-3 text-white outline-none transition focus:border-[#00ff66] focus:ring-1 focus:ring-[#00ff66]"
          />
        </div>

        <div>
          <label
            for="track-email"
            class="mb-2 block text-sm font-medium text-gray-300"
          >
            Reporter email
          </label>

          <input
            id="track-email"
            v-model.trim="reporterEmail"
            type="email"
            maxlength="254"
            autocomplete="email"
            required
            placeholder="The email used when reporting"
            class="w-full rounded-lg border border-white/10 bg-[#080808] px-4 py-3 text-white outline-none transition focus:border-[#00ff66] focus:ring-1 focus:ring-[#00ff66]"
          />
        </div>

        <p
          v-if="errorMessage"
          role="alert"
          class="text-sm text-red-400"
        >
          {{ errorMessage }}
        </p>

        <button
          type="submit"
          :disabled="loading"
          class="w-full rounded-lg bg-[#00ff66] px-5 py-3 font-semibold text-black transition hover:bg-[#69ff91] disabled:cursor-not-allowed disabled:opacity-60 sm:w-auto"
        >
          {{ loading ? 'Checking…' : 'Check ticket status →' }}
        </button>
      </form>
    </section>

    <!-- Tracking result -->
    <section
      v-if="ticket"
      class="mt-8 rounded-2xl border border-white/10 bg-[#0D0D0D] p-6 sm:p-8"
      aria-live="polite"
    >
      <p class="text-sm font-bold uppercase tracking-widest text-[#00ff66]">
        Report Details
      </p>

      <h2 class="mt-3 text-2xl font-bold">
        Ticket #{{ ticket.ticketNumber }}
      </h2>

      <div class="mt-6 grid gap-5 sm:grid-cols-2">
        <div>
          <p class="text-sm text-gray-400">Current status</p>
          <p class="mt-2 text-lg font-semibold text-[#00ff66]">
            {{ ticket.ticketStatus.replaceAll('_', ' ') }}
          </p>
        </div>

        <div>
          <p class="text-sm text-gray-400">Issue type</p>
          <p class="mt-2 font-medium">
            {{ ticket.issueType.replaceAll('_', ' ') }}
          </p>
        </div>

        <div>
          <p class="text-sm text-gray-400">Submitted</p>
          <p class="mt-2 text-sm text-gray-300">
            {{ formatDate(ticket.createdAt) }}
          </p>
        </div>
      </div>
    </section>

    <p class="mt-6 text-sm text-gray-500">
      Your email is used to verify your ticket and is not displayed
      in the tracking results.
    </p>
  </main>
</template>