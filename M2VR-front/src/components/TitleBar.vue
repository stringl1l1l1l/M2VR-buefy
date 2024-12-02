<template>
  <section class="section is-title-bar">
    <div class="level">
      <div class="level-left">
        <div class="level-item">
          <ul>
            <li v-for="(title, index) in titleStack" :key="index">
              {{ title }}
            </li>
          </ul>
        </div>
      </div>
      <div class="level-right">
        <div class="columns">
          <div class="column">
            <b-button class="is-right is-medium" @click="previous()">
              回退
            </b-button>
          </div>
          <div v-if="enableNext" class="column">
            <b-button class="is-righ is-medium" type="is-info" @click="$emit('next')">
              下一步
            </b-button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'TitleBar',
  props: {
    next: Function,
    enableNext: {
      type: Boolean,
      default: false
    },
    titleStack: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    previous() {
      this.$router.back();
    }
  }
})
</script>
