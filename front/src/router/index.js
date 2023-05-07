import {createRouter, createWebHistory} from 'vue-router'
import App from "@/App.vue";
import CarInsuranceQuote from "@/views/CarInsuranceQuote.vue";
import Home from "@/views/Home.vue";
import Discount from "@/views/Discount.vue";

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