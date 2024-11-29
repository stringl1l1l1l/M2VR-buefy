<template>
  <div>
    <section class="section is-main-section">
      <card-component class="has-table has-mobile-sort-spaced">
        <table-sample :maxChar="50" :loading="loading" :data="list" :columns="columns">
          <b-table-column width="1em" field="cover" label="Operation">
            <div class="container">
              <div class="column">
                <b-button class="is-fullwidth" size="is-small" type="is-info">Update</b-button>
              </div>
              <div class="column">
                <b-button class="is-fullwidth" size="is-small" type="is-danger">Delete</b-button>
              </div>
            </div>
          </b-table-column>
        </table-sample>
      </card-component>
    </section>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import NotificationBar from '@/components/NotificationBar.vue'
import { findAllVideos, findVideosByTopicId } from "@/api/video";
import CardComponent from '@/components/CardComponent.vue'
import TitleBar from '@/components/TitleBar.vue'
import HeroBar from '@/components/HeroBar.vue'
import TableSample from '@/components/TableSample.vue'

export default defineComponent({
  name: 'VideoView',
  components: {
    HeroBar,
    TitleBar,
    CardComponent,
    TableSample,
    NotificationBar
  },
  created() {
    this.fetchData(this.$route.query.topicId);
    this.columns = Object.keys(this.video)
      .map(key => ({
        field: key,
        label: key // 使用自定义 label 或默认使用属性名
      }));
  },
  data() {
    return {
      list: [],
      titleStack: ['Home', 'Videos'],
      loading: true,
      columns: Array,
      video: {
        bvid: '',
        topicId: 0,
        cover: '',
        title: '',
        author: '',
        updateTime: '',
        duration: '',
        danmaku: 0,
        likes: 0,
        play: 0,
        collect: 0,
        area: '',
        tag: '',
        description: '',
      },
    }
  },
  methods: {
    fetchData(query) {
      if (!query) {
        findAllVideos().then(response => {
          this.list = response.data
          this.loading = false
        })
      }
      else {
        findVideosByTopicId(query).then(response => {
          this.list = response.data
          this.loading = false
        })
      }
    }
  },
})
</script>
  