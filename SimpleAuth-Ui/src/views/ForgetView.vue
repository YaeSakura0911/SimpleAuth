<script setup>
import {computed, reactive} from "vue";

const forgetForm = reactive({
    email: ''
})

function handleFinish() {

}
</script>

<template>
    <a-layout>
        <a-layout-content>
            <a-row justify="center" align="middle" style="min-height: 100vh">
                <a-col :span="5">
                    <a-card>
                        <a-typography-title :level="3">SimpleAuth</a-typography-title>

                        <br/>

                        <a-form layout="vertical" :model="forgetForm" @finish="handleFinish">
                            <a-form-item label="电子邮箱"
                                         name="email"
                                         :rules="[{ type: 'email', message: '请输入有效的邮箱地址。', trigger: 'blur' }]">
                                <a-input v-model:value="forgetForm.email" />
                            </a-form-item>
                            <a-form-item>
                                <a-space direction="vertical" style="width: 100%">
                                    <a-button type="primary" html-type="submit" block>继续</a-button>
                                    <router-link to="/login">
                                        <a-button block>取消</a-button>
                                    </router-link>
                                </a-space>
                            </a-form-item>
                        </a-form>
                    </a-card>
                </a-col>
            </a-row>

        </a-layout-content>
    </a-layout>
</template>

<style scoped>

</style>