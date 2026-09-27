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

const submitted = ref(false)
const closed = ref(false)
const emit = defineEmits<{
    close: []
}>()
function submitReport() {
    // Later:
    // Send the selected issue type to the Spring Boot backend.
    // The backend will create the ticket in PostgreSQL.

    submitted.value = true
}
</script>

<template>
    <!-- Modal backdrop -->
    <div
        class="fixed inset-0 z-50 flex items-center justify-center
               bg-black/70 px-4 backdrop-blur-sm"
    >
        <!-- Modal -->
        <div
            class="w-full max-w-lg rounded-2xl border border-slate-700
                   bg-slate-900 p-8 text-white shadow-2xl"
        >
            <!-- Before ticket submission -->
            <div v-if="!submitted">
                <div class="mb-6">
                    <h1 class="text-2xl font-bold">
                        Report a Problem
                    </h1>

                    <p class="mt-2 text-sm text-slate-400">
                        Help us identify problems with SignalBait's analysis.
                    </p>
                </div>

                <!-- Privacy warning -->
                <div
                    class="mb-6 rounded-lg border border-yellow-600/50
                           bg-yellow-500/10 p-4"
                >
                    <h2 class="font-semibold text-yellow-400">
                        ⚠️ Please do not include sensitive information.
                    </h2>

                    <p class="mt-2 text-sm text-slate-300">
                        Do not submit passwords, OTPs, payment information,
                        account numbers, or other confidential information.
                    </p>
                </div>

                <form @submit.prevent="submitReport">
                    <label
                        for="issue-type"
                        class="mb-2 block text-sm font-medium text-slate-300"
                    >
                        What went wrong?
                    </label>

                    <select
                        id="issue-type"
                        class="w-full rounded-lg border border-slate-600
                               bg-slate-800 px-4 py-3 text-white
                               outline-none transition
                               focus:border-red-500 focus:ring-2
                               focus:ring-red-500/30"
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
                        <button
                            type="button"
                            @click="emit('close')"
                            class="rounded-lg bg-slate-700 px-5 py-2.5
                                font-semibold text-white transition
                                hover:bg-slate-600"
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            class="rounded-lg bg-red-600 px-5 py-2.5
                                   font-semibold text-white shadow-md
                                   transition hover:bg-red-500
                                   focus:outline-none focus:ring-2
                                   focus:ring-red-500 focus:ring-offset-2
                                   focus:ring-offset-slate-900"
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

                    <h2 class="mt-3 text-base leading-relaxed text-slate-300">
                        Your ticket has been created, but we need additional
                        information to investigate your report.
                    </h2>
                </div>

                <div
                    class="rounded-lg border border-slate-700
                           bg-slate-800/70 p-4"
                >
                    <p class="text-sm text-slate-300">
                        Please click the following link to provide more details
                        via Google Forms.
                    </p>

                    <a
                        href="https://docs.google.com/forms/d/e/1FAIpQLSf2OnNz02z6C-I4sI76WehZATS7rZyZMW91UH8u9zHeIKBHOw/viewform?usp=dialog"
                        target="_blank"
                        rel="noopener noreferrer"
                        class="mt-4 inline-block rounded-lg bg-blue-600
                               px-5 py-2.5 font-semibold text-white
                               transition hover:bg-blue-500"
                    >
                        Submit Additional Details
                    </a>
                </div>

                <div class="mt-6 flex justify-end">
                    <button
                        type="button"
                        @click="emit('close')"
                        class="rounded-lg bg-slate-700 px-5 py-2.5
                            font-semibold text-white transition
                            hover:bg-slate-600"
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>