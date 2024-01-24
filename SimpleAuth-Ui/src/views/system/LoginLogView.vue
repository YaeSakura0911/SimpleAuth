<script setup>
import {onBeforeMount, reactive, ref} from "vue"
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import {getLoginLogByPage} from "@/apis/log";
import {message} from "ant-design-vue";
import {SearchOutlined, ReloadOutlined} from "@ant-design/icons-vue";
import PageHeaderComponent from "@/components/PageHeaderComponent.vue";

dayjs.locale('zh-cn')

const tableLoading = ref(false)
const columns = [
    {
        key: 'principal',
        title: '登录主体',
        dataIndex: 'principal'
    },
    {
        key: 'ip',
        title: 'IP地址',
        dataIndex: 'ip'
    },
    {
        key: 'locate',
        title: '登录地点',
        dataIndex: 'locate'
    },
    {
        key: 'browser',
        title: '浏览器',
        dataIndex: 'browser'
    },
    {
        key: 'platform',
        title: '操作系统',
        dataIndex: 'platform'
    },
    {
        key: 'status',
        title: '登录状态'
    },
    {
        key: 'remark',
        title: '备注',
        dataIndex: 'remark'
    },
    {
        key: 'time',
        title: '登录时间',
        dataIndex: 'time'
    }
]
const pagination = ref({
    current: 1,
    pageSize: 10,
    total: '0',
    showSizeChanger: true,
    showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} 条`
})
const dataSource = ref([])

const queryForm = reactive({
    current: pagination.value.current,
    pageSize: pagination.value.pageSize,
    principal: '',
    ip: '',
    status: undefined,
    dateRange: undefined
})

onBeforeMount(() => {
    initData()
})

/**
 * 初始化数据
 */
function initData() {
    tableLoading.value = true
    getLoginLogByPage(queryForm).then(resp => {
        console.log(resp)
        if (resp.code === 200) {
            const respData = resp.data
            dataSource.value = respData.data
            pagination.value.current = respData.current
            pagination.value.pageSize = respData.pageSize
            pagination.value.total = respData.total
        }
        if (resp.code === 500) {
            message.error('服务器错误')
        }
        tableLoading.value = false
    })
}

/**
 * 根据条件搜索
 */
function handleSearch() {
    initData()
}

/**
 * 重置搜索表单
 */
function handleReset() {
    queryForm.principal = ''
    queryForm.ip = ''
    queryForm.status = undefined
    queryForm.dateRange = undefined
    initData()
}

/**
 * 处理表格变化
 * @param pagination 分页数据
 */
function handleTableChange(pagination) {
    queryForm.current = pagination.current
    queryForm.pageSize = pagination.pageSize
    initData()
}
</script>

<template>
    <a-layout>
        <page-header-component>
            <template #extra>
                <a-space>
                    <a-button>
                        <ExportOutlined />
                        导出
                    </a-button>
                </a-space>
            </template>
        </page-header-component>

        <a-layout-content style="margin: 24px; padding: 24px; background-color: #ffffff">
            <a-form :model="queryForm" :label-col="{ style: { width: '72px' } }">
                <a-row :gutter="24">
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录主体">
                            <a-input v-model:value="queryForm.principal" allow-clear />
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="IP地址">
                            <a-input v-model:value="queryForm.ip" allow-clear />
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录状态">
                            <a-select v-model:value="queryForm.status" allow-clear>
                                <a-select-option :value="true">登录成功</a-select-option>
                                <a-select-option :value="false">登录失败</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录日期">
                            <a-range-picker v-model:value="queryForm.dateRange" :locale="locale" value-format="YYYY-MM-DD" allow-clear />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="24" style="text-align: right;">
                        <a-space>
                            <a-button type="primary" @click="handleSearch">
                                <search-outlined />搜索
                            </a-button>
                            <a-button @click="handleReset">
                                <reload-outlined />重置
                            </a-button>
                        </a-space>
                    </a-col>
                </a-row>
            </a-form>

            <br />

            <a-table :columns="columns" :data-source="dataSource" :pagination="pagination" :loading="tableLoading" :scroll="{ x: 'max-content' }" @change="handleTableChange">
                <template #bodyCell="{column, record}">
                    <template v-if="column.key === 'status'">
                        <a-tag color="success" v-if="record.status">登录成功</a-tag>
                        <a-tag color="error" v-else>登录失败</a-tag>
                    </template>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>
.ant-picker {
    width: 100%
}
</style>