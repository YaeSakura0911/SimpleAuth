import {defineStore} from "pinia";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            name: 0,
            permissions: [],
            isAuthentication: false
        }
    },
    actions: {
        /**
         * 判断是否有指定权限
         * @param permission 权限
         * @returns Boolean
         */
        hasPermission(permission) {
            return permissions.includes(permission)
        }
    }
})