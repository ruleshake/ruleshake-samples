import {createRouter, createWebHistory} from 'vue-router'
import CarInsuranceQuote from "@/views/CarInsuranceQuote.vue";
import Home from "@/views/Home.vue";
import Discount from "@/views/Discount.vue";
import Impot from "@/views/Impot.vue";
import Bai from "@/views/Bai.vue";

const routes = [
    {
        name: "home",
        path: '/',
        component: Home,
    },
    {
        name: "car-insurance-quote",
        path: '/car-insurance-quote',
        component: CarInsuranceQuote,
    },
    {
        name: "discount",
        path: '/discount',
        component: Discount,
    },
    {
        name: "impot",
        path: '/impot',
        component: Impot,
    },
    {
        name: "bai",
        path: '/bai',
        component: Bai,
    },
]

let router = createRouter({
    history: createWebHistory(),
    routes: routes
});

export default {
    install(app, options) {
        router.install(app)
    }
}