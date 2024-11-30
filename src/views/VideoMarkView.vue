<template>
    <div>
        <title-bar @next="gotoMarkWithCheckedRows(checkedRows)" :enable-next="enableNext" :title-stack="titleStack" />
        <section class="section is-main-section">
            <card-component class="has-table has-mobile-sort-spaced">
                <table-sample checkable :checked-rows.sync="checkedRows" :searchable="false" :paginated="false"
                    :maxChar="25" :loading="loading" :data="list" :columns="columns">
                    <b-table-column label="Status" v-slot="props">
                        <div class="container">
                            <div class="column">
                                <b-tag v-if="!props.row['mark']" type="is-danger"> 待标注 </b-tag>
                                <b-tag v-else-if="!props.row['label']" type="is-info"> 进行中 </b-tag>
                                <b-tag v-else type="is-primary">已完成 </b-tag>
                            </div>
                        </div>
                    </b-table-column>

                    <b-table-column label="Operation" v-slot="props">
                        <div class="container">
                            <div class="column">
                                <b-button size="is-small " type="is-primary is-light"
                                    :disabled="props.row['mark'] && props.row['label'] || enableNext"
                                    @click="gotoMark(props.row)">选择</b-button>
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
    computed: {
        enableNext() {
            return this.checkedRows.length >= 1
        }
    },
    data() {
        return {
            checkedRows: [],
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
        paramFilter(param) {
            return {
                topic: this.$route.query.topic,
                topicId: param.topicId,
                bvid: param.bvid,
                title: param.title,
                author: param.author,
                area: param.area,
                marked: param.marked,
                label: param.label,
                tag: param.tag
            }
        },
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
        gotoMark(params) {
            this.$store.commit('add2Set', 'latestOperatedBvidsSet', params.bvid)
            this.$router.push({ path: '/workflow/mark', query: { ...this.paramFilter(params) } });
        },
        gotoMarkWithCheckedRows(rows) {
            this.$store.commit('basic', {
                key: 'originVideosList', value: rows.map(row => this.paramFilter(row))
            })
            this.$router.push({ path: '/workflow/mark' })
        }
    },
    watch: {
        checkedRows(newVal) {
            const bvids = new Set(newVal.map(row => (row.bvid)))
            this.$store.commit('basic', { key: 'latestOperatedBvidsSet', value: bvids })
        }
    }
})
</script>
    