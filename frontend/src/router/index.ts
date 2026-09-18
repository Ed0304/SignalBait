import { createRouter, createWebHistory } from "vue-router";

import HomeView from "@/views/HomeView.vue";
import FAQView from "@/views/FAQView.vue";
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
        }

    ]


})

export default router