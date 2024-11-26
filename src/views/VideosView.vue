<template>
  <div>
    <section class="section is-main-section">
      <card-component class="has-table has-mobile-sort-spaced">
        <table-sample checkable :data="list" :columns="columns" />
      </card-component>
    </section>
  </div>
</template>
  
<script>
import { defineComponent } from 'vue'
import NotificationBar from '@/components/NotificationBar.vue'
import { findVideosByPages, findAllVideos } from "@/api/video";
import CardComponent from '@/components/CardComponent.vue'
import TitleBar from '@/components/TitleBar.vue'
import HeroBar from '@/components/HeroBar.vue'
import TableSample from '@/components/TableSample.vue'

export default defineComponent({
  name: 'VideosView',
  components: {
    HeroBar,
    TitleBar,
    CardComponent,
    TableSample,
    NotificationBar
  },
  created() {
    this.fetchData();
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
      columns: [],
      video: {
        cover: '',
        bvid: '',
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
    fetchData() {
      findAllVideos().then(response => {
        this.list = response.data
      })
    }
  }
})
</script>
  