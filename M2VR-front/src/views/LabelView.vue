<template>
    <div>
        <card-component>
            <title-bar :title-stack="titleStack" />
            <section>
                <b-field>
                    <b-checkbox v-model="checkAll" type="is-info is-light ">
                        <span>All</span>
                    </b-checkbox>
                    <b-checkbox class="has-text-centered" v-for="(value, key) in this.MARK_ENUMS" :key="key"
                        v-model="checkboxGroup" :native-value="value" type="is-info is-light ">
                        <span>{{ key }}</span>
                    </b-checkbox>
                </b-field>
            </section>
            <card-component style="height:400px;" class="has-text-centered" :item="frames">
                <div style="height:40px;">
                    <b>{{ 'Topic：' + this.topic }}</b>
                </div>
                <div class="level">
                    <div class="level-item" v-for="(value, key) in (videoFilter(this.curVideo))" :key="key">
                        <div>
                            <p style="font-weight: bold;">{{ key }}</p>
                            <p v-if="key == 'mark'"> {{ showMark(value) }} </p>
                            <p v-else>{{ truncate(value, 30) }}</p>
                        </div>
                    </div>
                </div>
                <b-carousel-list :data="frames" :items-to-show="10" />
                <b-loading :is-full-page="false" :active="framesLoading"></b-loading>
                <div class="container" v-if="frameIsEmpty">
                    <p v-if="labelDone">标注已完成</p>
                    <p v-else>暂无数据</p>
                </div>
            </card-component>

        </card-component>
        <card-component>
            <div class="columns">
                <div v-for="(value, key) in LABEL_ENUMS" :key="key" class="column has-text-centered">
                    <b-radio v-model="radio" :name="key" :native-value="value">
                        {{ key }}
                    </b-radio>
                </div>
            </div>
            <div class="columns">
                <div class="column has-text-centered">
                    <b-button v-if="!labelDone" :loading="this.buttonLoading" type=" is-light" @click="onClickNextVideo()"
                        :disabled="this.frames.length == 0 || radio == 0">下一个视频</b-button>
                    <b-button v-else type="is-primary " @click="goBack()">视频全部标注完成，点击返回</b-button>

                </div>
            </div>
        </card-component>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findUnLabeledVediosByBvidList, setLabelByBvid } from '@/api/video'
import TableSample from '@/components/TableSample.vue'
import { getObjectsUrls } from '@/utils/minio'
import { mapState } from 'vuex'
import { watch } from 'vue'

export default {
    components: {
        TitleBar,
        CardComponent,
        TableSample
    },
    created() {
        // console.log(this.latestOperatedBvidsSet)
        // console.log(this.latestOperatedMarkMask)
        this.topic = this.$route.query.topic
        this.topicId = this.$route.query.topicId
        this.checkboxGroup = this.initMarkEnumsArr
        this.checkAll = (this.filterMarkMask == 63)
        this.initialize()
    },
    computed: {
        ...mapState(['latestOperatedBvidsSet', 'latestOperatedMarkMask']),
        filteredBvidList() {
            return Array.from(this.latestOperatedBvidsSet)
        },
        curVideo() {
            return this.filteredVedioList[this.videoListPtr] ? this.filteredVedioList[this.videoListPtr] : {}
        },
        labelDone() {
            return this.videoListPtr >= this.filteredVedioList.length
        },
        frameIsEmpty() {
            return this.frames.length == 0
        },
        filterMarkMask() {
            return this.checkboxGroup.reduce((a, b) => a | b, 0)
        },
        filteredVedioList() {
            return this.videoList.filter(video => video.mark && (video.mark & this.filterMarkMask))
        },
    },
    data() {
        return {
            titleStack: ['Workflow', 'Label'],
            bucketName: import.meta.env.VITE_MINIO_BASE_BUCKET,

            LABEL_ENUMS: {
                A: 1 << 0,
                B: 1 << 1,
                C: 1 << 2,
                D: 1 << 3,
                E: 1 << 4,
                F: 1 << 5,
            },
            MARK_ENUMS: {
                Copy: 1 << 0,
                Event: 1 << 1,
                Instance: 1 << 2,
                Reasoning: 1 << 3,
                Dependent: 1 << 4,
                Independent: 1 << 5,
            },
            initMarkEnumsArr: [1, 2, 4, 8, 16, 32],

            topic: '',
            topicId: 0,
            checkboxGroup: this.initMarkEnumsArr,
            radio: 0,
            framesLoading: true,
            buttonLoading: false,
            checkAll: false,

            videoList: [],
            videoListPtr: 0,
            frames: [],
            filteredMarks: [],
        }
    },
    methods: {
        async initialize() {
            // console.log(this.filteredBvidList)
            this.videoList = (await findUnLabeledVediosByBvidList(this.filteredBvidList)).data
        },
        async fetchFrames(video, bucketName = this.bucketName) {
            const urls = await getObjectsUrls(bucketName, `${video.topicId}/${video.bvid}`)
            return urls.map(url => ({ image: url }))
        },
        async onVideoChange() {
            this.framesLoading = true
            this.frames = await this.fetchFrames(this.curVideo)
            this.framesLoading = false
        },
        videoFilter(video) {
            return { bvid: video.bvid, title: video.title, author: video.author, area: video.area, tag: video.tag, mark: video.mark }
        },
        truncate(value, length) {
            if (value != null)
                return value.length > length
                    ? value.substr(0, length) + '...'
                    : value
        },
        showMark(mark) {
            if (!mark) return '未标注'
            return Object.keys(this.MARK_ENUMS)
                .filter(key => this.MARK_ENUMS[key] & mark)
                .reduce((acc, key) => acc + key + ' | ', '| ');
        },
        async onClickNextVideo() {
            this.buttonLoading = true;
            await setLabelByBvid(this.curVideo.bvid, this.radio);
            this.videoListPtr++;
            this.buttonLoading = false;
        },
        goBack() {
            this.$store.commit('clearSet')
            this.$store.commit('clearMark')
            this.$router.push({ path: '/workflow/video', query: { topic: this.topic, topicId: this.topicId } })
        }
    },
    watch: {
        curVideo() {
            this.onVideoChange()
        },
        checkAll(newVal) {
            this.checkboxGroup = newVal ? this.initMarkEnumsArr : []
        }
    }
}
</script> 