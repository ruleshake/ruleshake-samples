import {createRouter, createWebHistory} from 'vue-router'
import App from "@/App.vue";
import CarInsuranceQuote from "@/views/CarInsuranceQuote.vue";
import Home from "@/views/Home.vue";

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