<template>
    <div>
        <card-component>
            <title-bar :title-stack="titleStack" />
            <section>
                <b-field>
                    <b-checkbox-button class="has-text-centered" v-for="(value, key) in this.MARK_EMUMS" :key="key"
                        v-model="checkboxGroup" :native-value="value" type="is-primary">
                        <span>{{ key }}</span>
                    </b-checkbox-button>
                </b-field>
            </section>
        </card-component>
        <card-component>
            <b-carousel-list :data="frames" :items-to-show="10" />
            <b-loading :is-full-page="false" :active="loading"></b-loading>
            <!-- <div class="container" v-if="isEmpty[index]">
                <p v-if="markDone">标注已完成</p>
                <p v-else>暂无数据</p>
            </div> -->
        </card-component>
        <card-component>
            <table-sample :searchable="false" :loading="loading" :data="list" :columns="columns"> </table-sample>
        </card-component>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findMarksByTopicId, findMarksByBegin} from '@/api/mark'
import TableSample from '@/components/TableSample.vue'
import { getObjectsUrls } from '@/utils/minio'

export default {
    components: {
        TitleBar,
        CardComponent,
        TableSample
    },
    created() {
        this.beginVideo = this.$route.query
        this.bvid = this.$route.query.bvid
        this.topicId = this.$route.query.topicId
        this.markedVideosBvidSet.add(this.beginVideo.bvid)
        this.columns = Object.keys(this.entity)
            .map(key => ({
                field: key,
                label: key
            }));
        this.loading = true;
        this.fetchData()
        this.loading = false;
    },
    data() {
        return {
            titleStack: ['Workflow', 'Label'],
            MARK_EMUMS: {
                Copy: 1 << 0,
                Event: 1 << 1,
                Instance: 1 << 2,
                Reasoning: 1 << 3,
                Dependent: 1 << 4,
                Independent: 1 << 5,
            },
            frames: [],
            beginVideo: Object,
            checkboxGroup: [],
            columns: Array,
            loading: true,
            entity: {
                id: 0,
                begin: '',
                end: '',
                topicId: 0,
                mark: 0,
            },
            list: [],
            markedVideosBvidSet: new Set(),
        }
    },
    methods: {
        async fetchData() {
            this.list = (await findMarksByBegin(this.bvid)).data;
            this.list.forEach(item => this.markedVideosBvidSet.add(item.end))
        },
        async fetchFrames(video) {
            const arr = await getObjectsUrls("archived", `${video.topicId}/${video.bvid}`)
            if (arr.length != 0)
                this.isEmpty = false
            this.twoFrames = arr.map(url => ({ image: url }))
            this.loading = false
        },
    },
}
</script>