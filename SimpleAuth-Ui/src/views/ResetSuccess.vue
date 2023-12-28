<script setup>

</script>

<template>
    <a-layout>
        <a-layout-content>
            <a-row justify="center" align="middle" style="min-height: 100vh">
                <a-col :span="5">
                    <a-card>
                        <a-result
                            status="success"
                            title="重置成功！"
                            sub-title=""
                        >
                            <template #extra>
                                <a-button key="console" type="primary">登录</a-button>
                            </template>
                        </a-result>
                    </a-card>
                </a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>

</style>