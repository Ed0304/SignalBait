import { createRouter, createWebHistory } from "vue-router";

import HomeView from "@/views/HomeView.vue";
import FAQView from "@/views/FAQView.vue";
import AnalyzeView from "@/views/AnalyzeView.vue";
import TrackTicketView from "@/views/TrackTicketView.vue";
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
        },
        {
            path:'/trackticket',
            component:TrackTicketView
        }

    ]


})

export default router