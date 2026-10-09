<script setup lang="ts">
import { computed, ref } from 'vue'

const API_BASE_URL = import.meta.env.VITE_BASE_API_URL
const GOOGLE_FORM_URL = import.meta.env.VITE_GOOGLE_FORMS_URL
const GOOGLE_FORM_TICKET_FIELD = import.meta.env.VITE_GOOGLE_FORMS_TICKET_FIELD

const submitted = ref(false)
const issue_type = ref('FALSE_POSITIVE')
const reporterEmail = ref('')
const submitting = ref(false)
const errorMessage = ref('')
const confirmationEmailSent = ref(false)
const ticketNumber = ref<number | null>(null)
const formURL = computed(() => {
  const url = new URL(GOOGLE_FORM_URL)
  if (ticketNumber.value !== null) {
    url.searchParams.set(GOOGLE_FORM_TICKET_FIELD, String(ticketNumber.value))
  }
  return url.toString()
})

const emit = defineEmits<{ close: [] }>()

async function submitReport() {
  errorMessage.value = ''
  submitting.value = true

  try {
    const response = await fetch(`${API_BASE_URL}/api/tickets`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        issueType: issue_type.value,
        reporterEmail: reporterEmail.value.trim()
      })
    })

    const body = await response.json().catch(() => ({}))
    if (!response.ok) {
      throw new Error(body.message || 'Failed to create ticket. Please try again.')
    }

    ticketNumber.value = body.ticketNumber
    confirmationEmailSent.value = body.confirmationEmailSent === true
    submitted.value = true
  } catch (error) {
    errorMessage.value = error instanceof Error
      ? error.message
      : 'Something went wrong. Please try again.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/80 px-4 backdrop-blur-sm">
    <div class="w-full max-w-lg rounded-2xl border border-white/10 bg-[#0D0D0D] p-8 text-white shadow-2xl shadow-black/50">
      <div v-if="!submitted">
        <div class="mb-6">
          <h1 class="text-2xl font-bold">Report a Problem</h1>
          <p class="mt-2 text-sm text-gray-400">Help us identify problems with SignalBait's analysis.</p>
        </div>

        <div class="mb-6 rounded-lg border border-yellow-500/40 bg-yellow-500/10 p-4">
          <h2 class="font-semibold text-yellow-400">⚠️ Please do not include sensitive information.</h2>
          <p class="mt-2 text-sm text-gray-300">Do not submit passwords, OTPs, payment information, account numbers, or other confidential information.</p>
        </div>

        <form @submit.prevent="submitReport" class="space-y-5">
          <div>
            <label for="issue-type" class="mb-2 block text-sm font-medium text-gray-300">What went wrong?</label>
            <select id="issue-type" v-model="issue_type" required class="w-full rounded-lg border border-white/10 bg-[#151515] px-4 py-3 text-white outline-none focus:border-[#00ff66] focus:ring-2 focus:ring-[#00ff66]/20">
              <option value="FALSE_POSITIVE">Message falsely identified as potentially suspicious.</option>
              <option value="FALSE_NEGATIVE">Message falsely identified as potentially safe.</option>
              <option value="INCORRECT_MARKERS">Markers don't reflect the message's nature.</option>
            </select>
          </div>

          <div>
            <label for="reporter-email" class="mb-2 block text-sm font-medium text-gray-300">Email address</label>
            <input id="reporter-email" v-model.trim="reporterEmail" type="email" autocomplete="email" maxlength="254" required placeholder="you@example.com" class="w-full rounded-lg border border-white/10 bg-[#151515] px-4 py-3 text-white outline-none focus:border-[#00ff66] focus:ring-2 focus:ring-[#00ff66]/20" />
            <p class="mt-2 text-xs text-gray-400">We'll email your ticket number so you can track the report later.</p>
          </div>

          <p v-if="errorMessage" role="alert" class="text-sm text-red-400">{{ errorMessage }}</p>

          <div class="flex justify-end gap-3">
            <button type="button" @click="emit('close')" class="rounded-lg border border-white/10 bg-[#151515] px-5 py-2.5 font-semibold text-gray-300 transition hover:bg-[#1A1A1A] hover:text-white">Cancel</button>
            <button type="submit" :disabled="submitting" class="rounded-lg bg-[#00ff66] px-5 py-2.5 font-semibold text-black transition-colors hover:bg-[#00e65c] disabled:cursor-not-allowed disabled:opacity-60">
              {{ submitting ? 'Submitting…' : 'Submit Report' }}
            </button>
          </div>
        </form>
      </div>

      <div v-else>
        <div class="mb-6">
          <h1 class="text-2xl font-bold">Thanks for submitting!</h1>
          <p class="mt-3 text-gray-300">Your ticket <strong class="text-[#00ff66]">#{{ ticketNumber }}</strong> has been created.</p>
          <p v-if="confirmationEmailSent" class="mt-2 text-sm text-gray-300">A confirmation email has been sent to {{ reporterEmail }}.</p>
          <p v-else class="mt-2 text-sm text-amber-300">Your ticket was saved, but we couldn't send the confirmation email. Please keep your ticket number.</p>
        </div>

        <div class="rounded-lg border border-white/10 bg-[#151515] p-4">
          <p class="text-sm text-gray-300">You can also provide more details using Google Forms.</p>
          <a :href="formURL" target="_blank" rel="noopener noreferrer" class="mt-4 inline-block rounded-lg bg-[#00ff66] px-5 py-2.5 font-semibold text-black transition-colors hover:bg-[#00e65c]">Submit Additional Details</a>
        </div>

        <div class="mt-6 flex justify-end">
          <button type="button" @click="emit('close')" class="rounded-lg border border-white/10 bg-[#151515] px-5 py-2.5 font-semibold text-gray-300 transition hover:bg-[#1A1A1A] hover:text-white">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
