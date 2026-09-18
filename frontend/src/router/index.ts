import { createRouter, createWebHistory } from "vue-router";

import HomeView from "@/views/HomeView.vue";
import FAQView from "@/views/FAQView.vue";
import AnalyzeView from "@/views/AnalyzeView.vue";
const router = createRouter({

    history: createWebHistory(),

    routes:[
        {
            path:'/',
            component:HomeView
        },
        {
            path:'/FAQ',
            component:FAQView
        },
        {
            path:'/analyze',
            component:AnalyzeView
        }

    ]


})

export default router