<template>
    <div>
        <router-view />
    </div>
</template>

<script>
import { defineComponent } from 'vue'
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
export default defineComponent({
    name: 'WorkflowView',
    components: {
        TitleBar,
        CardComponent,
    },
    // data() {

    // }
})
</script>