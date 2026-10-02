<script setup lang="ts">
import BackButton from '@/components/BackButton.vue'
import ReportBugModal from '@/components/ReportBugModal.vue'
import { ref, computed } from 'vue'

const inputMode = ref<'text' | 'image'>('text')
const showReportModal = ref(false)

// Stores the text entered by the user
const message = ref('')

// Stores the image selected by the user
const selectedFile = ref<File | null>(null)

// Stores the API response
const result = ref<any>(null)

// Prevents multiple submissions
const isAnalyzing = ref(false)

const API_URL = import.meta.env.VITE_NLP_API_URL

// Calculate the overall risk level from the high-risk score.
const riskLevel = computed(() => {
  if (!result.value) return null

  const score = result.value.high_risk_score

  if (score >= 0.7) return 'High'
  if (score >= 0.4) return 'Medium'
  return 'Low'
})

// Convert the high-risk score into a percentage for the progress bar.
const riskPercentage = computed(() => {
  if (!result.value) return 0

  return Math.round(result.value.high_risk_score * 100)
})

// Convert all supporting indicators into a format
// that is easy to display in the template.
const indicatorCategories = computed(() => {
  if (!result.value) return []

  return [
    {
      name: 'High-risk indicators',
      indicators: result.value.high_risk_indicators,
    },
    {
      name: 'Contextual markers',
      indicators: result.value.contextual_indicators,
    },
    {
      name: 'Neutral signals',
      indicators: result.value.neutral_indicators,
    },
  ]
})

function handleFileChange(event: Event) {
  const input = event.target as HTMLInputElement

  selectedFile.value = input.files?.[0] ?? null
}

// Analyze plain text using FastAPI + BART
async function analyzeText(content: string) {
  const response = await fetch(`${API_URL}/analyze`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ content }),
  })

  if (!response.ok) {
    throw new Error('Failed to analyze message')
  }

  return await response.json()
}

// Analyze an image using FastAPI + PaddleOCR + BART
async function analyzeImage(file: File) {
  const formData = new FormData()
  formData.append('file', file)

  const response = await fetch(`${API_URL}/scan-image`, {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    throw new Error('Failed to scan image')
  }

  return await response.json()
}

async function handleAnalyze() {
  isAnalyzing.value = true
  result.value = null

  try {
    if (inputMode.value === 'text') {
      // Send text to FastAPI → BART
      result.value = await analyzeText(message.value)
    } else {
      // Make sure an image was selected
      if (!selectedFile.value) {
        throw new Error('Please select an image.')
      }

      // Send image to FastAPI → PaddleOCR → BART
      result.value = await analyzeImage(selectedFile.value)
    }

    console.log(result.value)

  } catch (error) {
    console.error(error)
  } finally {
    // Always stop the loading state,
    // even if the API request fails.
    isAnalyzing.value = false
  }
}
</script>

<template>
  <main class="mx-auto max-w-5xl px-6 py-14">

    <!-- Page heading -->
    <div>
      <p
        class="text-sm font-semibold uppercase tracking-[0.25em]
               text-[#00ff66]"
      >
        Signal Analysis
      </p>

      <h1 class="mt-2 text-4xl font-bold tracking-tight text-white">
        Analyze
      </h1>

      <p class="mt-3 max-w-2xl text-gray-400">
        Analyze suspicious messages and identify semantic signals
        that may require further verification.
      </p>

      <p class="mt-4 text-sm text-gray-500">
        New to SignalBait?
        <RouterLink
          to="/faq"
          class="font-medium text-[#00ff66] underline
                 underline-offset-4 transition-colors
                 hover:text-[#00e65c]"
        >
          Read the FAQ
        </RouterLink>
        first.
      </p>
    </div>


    <!-- Main disclaimer -->
    <div
      class="mt-8 rounded-xl border border-yellow-500/20
             bg-yellow-500/5 p-5"
    >
      <div class="flex gap-3">
        <span class="text-lg">⚠️</span>

        <div>
          <h2 class="font-semibold text-yellow-400">
            Analysis Disclaimer
          </h2>

          <p class="mt-2 text-sm leading-6 text-gray-400">
            SignalBait's analysis results may contain mistakes and should not
            be treated as definitive. If you're unsure whether a message or
            link is legitimate, contact your bank, service provider, or
            relevant local authorities through their official channels
            to verify it.
          </p>
        </div>
      </div>
    </div>


    <!-- Analyzer -->
    <div
      class="mt-8 rounded-2xl border border-white/10
             bg-[#0D0D0D] p-6 shadow-2xl shadow-black/30"
    >

      <!-- Input mode selector -->
      <div
        class="flex w-fit rounded-lg border border-white/10
               bg-[#151515] p-1"
      >
        <button
          type="button"
          @click="inputMode = 'text'"
          :class="[
            'rounded-md px-6 py-2 text-sm font-semibold transition-all',
            inputMode === 'text'
              ? 'bg-[#00ff66] text-black shadow-[0_0_15px_rgba(0,255,102,0.15)]'
              : 'text-gray-400 hover:text-white'
          ]"
        >
          Text
        </button>

        <button
          type="button"
          @click="inputMode = 'image'"
          :class="[
            'rounded-md px-6 py-2 text-sm font-semibold transition-all',
            inputMode === 'image'
              ? 'bg-[#00ff66] text-black shadow-[0_0_15px_rgba(0,255,102,0.15)]'
              : 'text-gray-400 hover:text-white'
          ]"
        >
          Image
        </button>
      </div>


      <!-- Report bug -->
      <div class="mt-5">
        <button
          type="button"
          @click="showReportModal = true"
          class="text-sm font-medium text-gray-500
                 transition-colors hover:text-[#00ff66]"
        >
          Report a problem →
        </button>
      </div>

      <ReportBugModal
        v-if="showReportModal"
        @close="showReportModal = false"
      />


      <!-- Input area -->
      <div class="mt-6">

        <!-- Text input -->
        <textarea
          v-if="inputMode === 'text'"
          v-model="message"
          placeholder="Paste suspicious message here..."
          class="min-h-56 w-full resize-y rounded-xl
                 border border-white/10 bg-[#080808]
                 px-5 py-4 text-white
                 placeholder:text-gray-600
                 outline-none transition-all
                 focus:border-[#00ff66]/60
                 focus:ring-1 focus:ring-[#00ff66]/30"
        ></textarea>


        <!-- Image input -->
        <label
          v-else
          class="flex min-h-56 cursor-pointer flex-col
                 items-center justify-center rounded-xl
                 border-2 border-dashed border-white/10
                 bg-[#080808] px-6 py-10 text-center
                 transition-all
                 hover:border-[#00ff66]/50
                 hover:bg-[#0A0F0B]"
        >
          <span
            class="flex h-12 w-12 items-center justify-center
                   rounded-full border border-[#00ff66]/30
                   bg-[#00ff66]/5 text-xl text-[#00ff66]"
          >
            ↑
          </span>

          <span class="mt-4 text-lg font-semibold text-white">
            Upload an image
          </span>

          <span class="mt-2 text-sm text-gray-500">
            Upload a screenshot of the suspicious message
          </span>

          <span
            class="mt-5 rounded-lg border border-white/10
                   bg-[#151515] px-5 py-2.5
                   text-sm font-semibold text-gray-300
                   transition-colors
                   hover:border-[#00ff66]/40
                   hover:text-[#00ff66]"
          >
            Choose image
          </span>

          <input
            type="file"
            accept="image/*"
            class="hidden"
            @change="handleFileChange"
          />
        </label>

      </div>


      <!-- Selected image -->
      <p
        v-if="selectedFile && inputMode === 'image'"
        class="mt-3 text-sm text-gray-500"
      >
        Selected:
        <span class="text-gray-300">
          {{ selectedFile.name }}
        </span>
      </p>


      <!-- Submit -->
      <button
        type="button"
        :disabled="isAnalyzing"
        @click="handleAnalyze"
        class="mt-6 flex w-full items-center justify-center gap-2
               rounded-xl bg-[#00ff66] px-6 py-3.5
               font-bold text-black
               shadow-[0_0_20px_rgba(0,255,102,0.08)]
               transition-all
               hover:bg-[#00e65c]
               hover:shadow-[0_0_25px_rgba(0,255,102,0.18)]
               disabled:cursor-not-allowed
               disabled:opacity-50
               focus:outline-none
               focus:ring-2 focus:ring-[#00ff66]
               focus:ring-offset-2
               focus:ring-offset-[#0D0D0D]"
      >

        <!-- Loading spinner -->
        <span
          v-if="isAnalyzing"
          class="h-5 w-5 animate-spin rounded-full
                 border-2 border-black/20 border-t-black"
        ></span>

        <span>
          {{ isAnalyzing ? 'Analyzing...' : 'Analyze Signal' }}
        </span>

      </button>

      <div class="mt-4 flex justify-center">
        <BackButton />
      </div>

    </div>


    <!-- ================================================== -->
    <!-- Analysis Result -->
    <!-- ================================================== -->

    <div
      v-if="result"
      class="mt-8 overflow-hidden rounded-2xl
             border border-white/10 bg-[#0D0D0D]"
    >

      <!-- Result header -->
      <div class="border-b border-white/10 px-6 py-5">
        <p
          class="text-xs font-semibold uppercase tracking-[0.2em]
                 text-[#00ff66]"
        >
          Analysis Complete
        </p>

        <h2 class="mt-1 text-2xl font-bold text-white">
          Analysis Result
        </h2>
      </div>


      <!-- Overall risk -->
      <div class="p-6">

        <div class="flex items-end justify-between">
          <div>
            <p class="text-sm text-gray-500">
              Risk Level
            </p>

            <p class="mt-1 text-3xl font-bold text-white">
              {{ riskLevel }}
            </p>
          </div>

          <span
            class="text-2xl font-bold text-gray-300"
          >
            {{ riskPercentage }}%
          </span>
        </div>


        <!-- Overall risk progress -->
        <div
          class="mt-4 h-3 w-full overflow-hidden
                 rounded-full bg-[#1A1A1A]"
        >
          <div
            class="h-full rounded-full transition-all duration-700"
            :class="
              riskLevel === 'High'
                ? 'bg-red-500'
                : riskLevel === 'Medium'
                  ? 'bg-yellow-400'
                  : 'bg-[#00ff66]'
            "
            :style="{ width: `${riskPercentage}%` }"
          ></div>
        </div>

        <p class="mt-2 text-xs text-gray-600">
          Risk score generated from detected semantic signals.
        </p>

      </div>


      <!-- Supporting indicators -->
      <div class="border-t border-white/10 p-6">

        <div>
          <p
            class="text-xs font-semibold uppercase
                   tracking-[0.2em] text-[#00ff66]"
          >
            Signal Breakdown
          </p>

          <h3 class="mt-1 text-xl font-semibold text-white">
            Supporting Indicators
          </h3>

          <p class="mt-2 text-sm leading-6 text-gray-500">
            These semantic indicators show the signals detected by
            the analysis.
          </p>
        </div>


        <div class="mt-8 space-y-8">

          <!-- Each category -->
          <div
            v-for="category in indicatorCategories"
            :key="category.name"
          >

            <h4 class="text-sm font-semibold uppercase
                       tracking-wider text-gray-300">
              {{ category.name }}
            </h4>

            <div class="mt-4 space-y-5">

              <!-- Each indicator -->
              <div
                v-for="indicator in category.indicators"
                :key="indicator.label"
              >

                <div class="flex items-center justify-between gap-4">
                  <span class="text-sm text-gray-300">
                    {{ indicator.label }}
                  </span>

                  <span class="shrink-0 text-sm font-medium text-gray-500">
                    {{ Math.round(indicator.score * 100) }}%
                  </span>
                </div>

                <!-- Indicator progress bar -->
                <div
                  class="mt-2 h-1.5 w-full overflow-hidden
                         rounded-full bg-[#1A1A1A]"
                >
                  <div
                    class="h-full rounded-full bg-[#00ff66]/70
                           transition-all duration-700"
                    :style="{
                      width: `${indicator.score * 100}%`
                    }"
                  ></div>
                </div>

              </div>

            </div>
          </div>

        </div>
      </div>


      <!-- Disclaimer -->
      <div
        class="border-t border-white/10
               bg-[#090909] px-6 py-5"
      >
        <p class="text-sm leading-6 text-gray-500">
          <span class="font-semibold text-gray-300">
            Note:
          </span>
          Analysis results may be incorrect. If in doubt, verify with
          official authorities.
        </p>
      </div>

    </div>

  </main>
</template>