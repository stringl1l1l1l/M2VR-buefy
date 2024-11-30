<template>
    <div>
        <card-component>
            <title-bar :next="next()" enable-next :title-stack="titleStack" />

            <card-component style="height:330px;" class="has-text-centered" :item="frames">
                <div class="level">
                    <div class="level-item" v-for="(value, key) in (videoFilter(twoVideos[index]))" :key="key">
                        <div>
                            <p style="font-weight: bold;">{{ key }}</p>
                            <p>{{ truncate(value, 30) }}</p>
                        </div>
                    </div>
                </div>
                <b-carousel-list :data="frames" :items-to-show="10" />
                <b-loading :is-full-page="false" :active="framesLoading[index]"></b-loading>
                <div class="container" v-if="framesIsEmpty[index]">
                    <p v-if="markDone">标注已完成</p>
                    <p v-else>暂无数据</p>
                </div>
            </card-component>

            <section>
                <b-field>
                    <b-checkbox-button class="has-text-centered" v-for="(value, key) in this.MARK_EMUMS" :key="key"
                        v-model="checkboxGroup" :native-value="value" type="is-primary">
                        <span>{{ key }}</span>
                    </b-checkbox-button>
                </b-field>
            </section>
        </card-component>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findMarksByTopicId } from '@/api/mark'
import TableSample from '@/components/TableSample.vue'
import { getObjectsUrls } from '@/utils/minio'
import { mapState } from 'vuex'

export default {
    components: {
        TitleBar,
        CardComponent,
        TableSample
    },
    created() {
        // console.log(this.latestOperatedBvidsSet)
        // console.log(this.latestOperatedMarkMask)

        this.initialize()

    },
    computed: {
        ...mapState(['latestOperatedBvidsSet', 'latestOperatedMarkMask']),
        curBvid() {
            return this.filteredBvidList[this.listPtr]
        }
    },
    data() {
        return {
            titleStack: ['Workflow', 'Label'],
            bucketName: import.meta.env.VITE_MINIO_BASE_BUCKET,
            MARK_EMUMS: {
                Copy: 1 << 0,
                Event: 1 << 1,
                Instance: 1 << 2,
                Reasoning: 1 << 3,
                Dependent: 1 << 4,
                Independent: 1 << 5,
            },

            bvidMarkMap: {},
            listPtr: 0,
            frames: [],
            filteredMarks: [],
            loading: true,
        }
    },
    methods: {
        async initialize() {
            this.topicId = this.$route.query.topicId
            this.filteredMarks = await this.fetchFilteredMarks()
            this.filteredMarks.forEach(item => {
                this.bvidMarkMap[item.begin] = this.bvidMarkMap[item.begin] ? (this.bvidMarkMap[item.begin] | item.mark) : 0
                this.bvidMarkMap[item.end] = this.bvidMarkMap[item.end] ? (this.bvidMarkMap[item.end] | item.mark) : 0
            })
            console.log(this.bvidMarkMap)
            this.frames = await this.fetchFrames({ bvid: this.curBvid, topicId: this.topicId })
            this.loading = false;
        },
        async fetchFilteredMarks() {
            let marks = (await findMarksByTopicId(this.topicId)).data;
            return marks.filter(mark => (this.latestOperatedMarkMask | mark.mark)
                && this.latestOperatedBvidsSet.has(mark.begin)
                && this.latestOperatedBvidsSet.has(mark.end)
            )
        },
        async fetchFrames(video, bucketName = this.bucketName) {
            const urls = await getObjectsUrls(bucketName, `${video.topicId}/${video.bvid}`)
            return urls.map(url => ({ image: url }))
        },
        next() {

        }
    },
    watch: {
    }
}
</script> 