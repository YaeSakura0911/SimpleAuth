<script setup>

</script>

<template>
    <a-layout style="overflow: hidden">
        <router-view v-slot="{ Component }">
            <Transition name="slide-fade" mode="out-in">
                <component :is="Component" />
            </Transition>
        </router-view>
    </a-layout>
</template>

<style scoped>
.slide-fade-enter-active  {
    transition: all 150ms ease-in;
}

.slide-fade-leave-active {
    transition: all 150ms ease-out;
}

.slide-fade-enter-from {
    //transform: translateX(-20%) rotate(-35deg);
    transform: translateX(-10%);
    opacity: 0;
}
.slide-fade-leave-to {
    //transform: translateX(20%) rotate(35deg);
    transform: translateX(10%);
    opacity: 0;
}
</style>
