<script setup lang="ts">
// First phase of bug reporting:
// User chooses problem they encountered:
// 1. Message falsely identified as potentially safe
// 2. Message falsely identified as potentially suspicious
// 3. Markers don't reflect the message's nature
//
// Then, they are automatically issued a ticket and asked to submit a Google Form.
// The ticket is sent to PostgreSQL without any information about the screenshot.

import { ref } from 'vue'

const API_BASE_URL = import.meta.env.VITE_BASE_API_URL
const GOOGLE_FORM_URL = import.meta.env.VITE_GOOGLE_FORMS_URL
const GOOGLE_FORM_TICKET_FIELD = import.meta.env.VITE_GOOGLE_FORMS_TICKET_FIELD

const formURL = new URL(GOOGLE_FORM_URL)

const submitted = ref(false)
const closed = ref(false)
const issue_type = ref('FALSE_POSITIVE') // Default value assigned

const emit = defineEmits<{
  close: []
}>()

const ticketNumber = ref<number | null>(null)

async function submitReport() {
  // Send the selected issue type to the Spring Boot backend.
  // The backend creates the ticket in PostgreSQL.

  try {
    const response = await fetch(
      `${API_BASE_URL}/api/tickets`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          issueType: issue_type.value
        })
      }
    )

    if (!response.ok) {
      throw new Error('Failed to create ticket')
    }

    const ticket = await response.json()

    ticketNumber.value = ticket.ticketNumber

    console.log('Ticket created:', ticket)

    formURL.searchParams.set(
      GOOGLE_FORM_TICKET_FIELD,
      ticket.ticketNumber.toString()
    )

    submitted.value = true

  } catch (error) {
    console.error('Failed to submit report:', error)
  }
}
</script>

<template>
  <!-- Modal backdrop -->
  <div
    class="fixed inset-0 z-50 flex items-center justify-center
           bg-black/80 px-4 backdrop-blur-sm"
  >

    <!-- Modal -->
    <div
      class="w-full max-w-lg rounded-2xl border border-white/10
             bg-[#0D0D0D] p-8 text-white shadow-2xl
             shadow-black/50"
    >

      <!-- Before ticket submission -->
      <div v-if="!submitted">

        <div class="mb-6">
          <h1 class="text-2xl font-bold">
            Report a Problem
          </h1>

          <p class="mt-2 text-sm text-gray-400">
            Help us identify problems with SignalBait's analysis.
          </p>
        </div>

        <!-- Privacy warning -->
        <div
          class="mb-6 rounded-lg border border-yellow-500/40
                 bg-yellow-500/10 p-4"
        >
          <h2 class="font-semibold text-yellow-400">
            ⚠️ Please do not include sensitive information.
          </h2>

          <p class="mt-2 text-sm text-gray-300">
            Do not submit passwords, OTPs, payment information,
            account numbers, or other confidential information.
          </p>
        </div>

        <form @submit.prevent="submitReport">

          <label
            for="issue-type"
            class="mb-2 block text-sm font-medium text-gray-300"
          >
            What went wrong?
          </label>

          <select
            id="issue-type"
            v-model="issue_type"
            class="w-full rounded-lg border border-white/10
                   bg-[#151515] px-4 py-3 text-white outline-none
                   transition
                   focus:border-[#00ff66]
                   focus:ring-2 focus:ring-[#00ff66]/20"
          >
            <option value="FALSE_POSITIVE">
              Message falsely identified as potentially suspicious.
            </option>

            <option value="FALSE_NEGATIVE">
              Message falsely identified as potentially safe.
            </option>

            <option value="INCORRECT_MARKERS">
              Markers don't reflect the message's nature.
            </option>
          </select>

          <div class="mt-6 flex justify-end gap-3">

            <!-- Cancel -->
            <button
              type="button"
              @click="emit('close')"
              class="rounded-lg border border-white/10
                     bg-[#151515] px-5 py-2.5
                     font-semibold text-gray-300 transition
                     hover:border-white/20 hover:bg-[#1A1A1A]
                     hover:text-white"
            >
              Cancel
            </button>

            <!-- Submit -->
            <button
              type="submit"
              class="rounded-lg bg-[#00ff66] px-5 py-2.5
                     font-semibold text-black shadow-md
                     transition-colors
                     hover:bg-[#00e65c]
                     focus:outline-none focus:ring-2
                     focus:ring-[#00ff66]
                     focus:ring-offset-2
                     focus:ring-offset-[#0D0D0D]"
            >
              Submit Report
            </button>

          </div>
        </form>
      </div>


      <!-- After ticket creation -->
      <div v-else>

        <div class="mb-6">
          <h1 class="text-2xl font-bold">
            Thanks for submitting!
          </h1>

          <p class="mt-3 text-gray-300">
            Your ticket
            <strong class="text-[#00ff66]">
              #{{ ticketNumber }}
            </strong>
            has been created.
          </p>
        </div>

        <div
          class="rounded-lg border border-white/10
                 bg-[#151515] p-4"
        >
          <p class="text-sm text-gray-300">
            Please click the following link to provide more details
            via Google Forms.
          </p>

          <!-- Google Forms -->
          <a
            :href="formURL.toString()"
            target="_blank"
            rel="noopener noreferrer"
            class="mt-4 inline-block rounded-lg
                   bg-[#00ff66] px-5 py-2.5
                   font-semibold text-black
                   transition-colors
                   hover:bg-[#00e65c]"
          >
            Submit Additional Details
          </a>
        </div>

        <div class="mt-6 flex justify-end">

          <button
            type="button"
            @click="emit('close')"
            class="rounded-lg border border-white/10
                   bg-[#151515] px-5 py-2.5
                   font-semibold text-gray-300 transition
                   hover:border-white/20
                   hover:bg-[#1A1A1A]
                   hover:text-white"
          >
            Close
          </button>

        </div>
      </div>

    </div>
  </div>
</template>