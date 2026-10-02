<script setup lang="ts">
</script>

<template>
  <footer class="border-t border-white/10 bg-[#050505]">
    <div class="mx-auto max-w-6xl px-6 py-10">

      <div class="flex flex-col gap-8 md:flex-row md:justify-between">

        <!-- Brand -->
        <div>
          <h2 class="text-xl font-bold text-white">
            Signal<span class="text-[#00ff66]">Bait</span>
          </h2>

          <p class="mt-2 max-w-xs text-sm text-gray-400">
            Catch the red flags before proceeding.
          </p>
        </div>

        <!-- Navigation -->
        <nav class="flex gap-8">
          <div>
            <h3 class="text-sm font-semibold text-white">
              Product
            </h3>

            <div class="mt-3 flex flex-col gap-2 text-sm">
              <RouterLink
                to="/analyze"
                class="text-gray-400 transition-colors hover:text-[#00ff66]"
              >
                Analyze
              </RouterLink>

              <RouterLink
                to="/FAQ"
                class="text-gray-400 transition-colors hover:text-[#00ff66]"
              >
                FAQ
              </RouterLink>
            </div>
          </div>
        </nav>

      </div>

      <!-- Bottom -->
      <div class="mt-10 border-t border-white/10 pt-6">
        <p class="text-sm text-gray-500">
          © 2026 SignalBait. Built as a software engineering project.
        </p>
      </div>

    </div>
  </footer>
</template>