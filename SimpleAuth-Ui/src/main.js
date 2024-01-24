import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// Ant Design Vue
import Antd from 'ant-design-vue'
import * as AntdIcons from '@ant-design/icons-vue'
import 'ant-design-vue/dist/reset.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia).use(router).use(Antd).mount('#app')

const antdIcons = AntdIcons
for (let i in AntdIcons) {
    app.component(i, antdIcons[i])
}
