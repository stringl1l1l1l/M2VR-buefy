<template>
    <div>
        <title-bar :title-stack="titleStack" />
        <section class="section is-main-section">
            <card-component class="has-table has-mobile-sort-spaced">
                <table-sample :maxChar="25" :perPage="10" :loading="loading" :data="list" :columns="columns">

                    <b-table-column label="Status" v-slot="props">
                        <div class="container">
                            <div class="column">
                                <b-tag v-if="!props.row['marked']" type="is-danger"> 未开始 </b-tag>
                                <b-tag v-else-if="!props.row['label']" type="is-light"> 进行中 </b-tag>
                                <b-tag v-else type="is-primary">已完成 </b-tag>
                            </div>
                        </div>
                    </b-table-column>

                    <b-table-column label="Operation" v-slot="props">
                        <div class="container">
                            <div class="column">
                                <b-button size="is-small " type="is-primary is-light" :disabled="props.row['marked'] && props.row['label'] " @click="next(props.row)">选择</b-button>
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
    name: 'MarkVideoView',
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
            titleStack: ['Workflow', 'Video'],
            loading: true,
            columns: Array,
            video: {
                bvid: '',
                topicId: 0,
                cover: '',
                title: '',
                author: '',
                // updateTime: '',
                // duration: '',
                // danmaku: 0,
                // likes: 0,
                // play: 0,
                // collect: 0,
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
        },
        next(param) {
            this.$router.push({ path: '/workflow/mark', query: { ...param } });
        }
    },

})
</script>
    