<template>
  <div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'HomeView',
  components: {
  },
  data () {
    return {
    }
  },
  mounted () {
    this.$router.push({path: '/workflow/topic'})
  },
  methods: {

  }
})
</script>
