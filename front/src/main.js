import { createApp } from 'vue'
import './style.css'
import router from "./router"
import Notifications from '@kyvg/vue3-notification'
import App from './App.vue'

let app = createApp(App);
app
    .use(router, app)
    .use(Notifications)
    .mount('#app')

export default app

