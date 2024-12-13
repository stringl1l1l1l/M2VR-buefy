<template>
    <div>
        <title-bar :title-stack="titleStack" />
        <section class="section is-main-section">
            <card-component style="height:330px;" class="has-text-centered" v-for="(frames, index) in twoFrames"
                :index="index" :item="frames" :key="index">
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

            <card-component>
                <div class="columns">
                    <div v-for="(value, key) in MARK_EMUMS" :key="key" class="column has-text-centered">
                        <b-radio v-model="radio" :name="key" :native-value="value">
                            {{ key }}
                        </b-radio>
                    </div>
                </div>
                <div class="columns">
                    <div class="column has-text-centered">
                        <b-button v-if="!epochDone" :loading="this.buttonLoading" type=" is-light"
                            @click="onClickNextVideo()"
                            :disabled="this.originFrames.length == 0 || radio == 0">下一个视频</b-button>
                        <b-button v-else-if="markDone" type="is-primary " @click="gotoLabel()">视频全部标注完成，点击进行下一步</b-button>
                        <b-button v-else :loading="this.buttonLoading" type="is-info is-light"
                            :disabled="!epochDone || radio == 0" @click="nextEpoch()">该轮次标注完毕，点击进行下一步</b-button>

                    </div>
                </div>
            </card-component>
        </section>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findVideoByBvid, setMarkByBvid } from '@/api/video'
import { insertMark, findTodoBvidList } from '@/api/mark'
import { getObjectsUrls } from '@/utils/minio'
import { mapState } from 'vuex'

export default {
    components: {
        TitleBar,
        CardComponent,
    },
    created() {
        if (this.latestTopicObj) {
            this.topicId = this.latestTopicObj.topicId
            this.topic = this.latestTopicObj.topic
        }
        else if (this.$route.query && this.$route.query.topicId)
            this.topicId = this.$route.query.topicId
        else
            this.$router.back()

        if (this.$route.query && this.$route.query.bvid)
            this.originVideosList = [this.$route.query]
        else if (this.$store.state.originVideosList)
            this.originVideosList = this.$store.state.originVideosList
        // else
        //     this.$router.back()
        // console.log(this.originVideosList)
    },
    data() {
        return {
            titleStack: ['Workflow', 'Mark'],
            bucketName: import.meta.env.VITE_MINIO_BASE_BUCKET,
            MARK_EMUMS: {
                Copy: 1 << 0,
                Event: 1 << 1,
                Instance: 1 << 2,
                Reasoning: 1 << 3,
                Dependent: 1 << 4,
                Independent: 1 << 5,
            },

            topicId: 0,
            topic: '',
            originVideosList: [],
            originVideoListPtr: 0,
            targetVideo: {},
            todoBvidList: [],
            todoBvidListPtr: 0,
            originFrames: [],
            targetFrames: [],
            markMask: 0,

            framesLoading: [true, true],
            buttonLoading: false,
            radio: 1,
        }
    },
    computed: {
        ...mapState(['latestTopicObj']),
        markDone() {
            return this.originVideoListPtr == this.originVideosList.length - 1 && this.epochDone
        },
        epochDone() {
            return this.todoBvidListPtr >= this.todoBvidList.length + 1;
        },
        originVideo() {
            return this.originVideosList.length ? this.originVideosList[this.originVideoListPtr] : {}
        },
        twoVideos() {
            return [this.originVideo, this.targetVideo]
        },
        twoFrames() {
            return [this.originFrames, this.targetFrames]
        },
        framesIsEmpty() {
            return [this.originFrames.length == 0, this.targetFrames.length == 0]
        }
    },
    methods: {
        async initialize() {
            this.todoBvidList = (await findTodoBvidList(this.originVideo.bvid, this.originVideo.topicId)).data
            // console.log(this.todoBvidList)

            this.targetVideo = await this.fetchNextTargetVideo(this.todoBvidList)
            this.todoBvidListPtr++
            // console.log(this.targetVideo)
            this.originFrames = await this.fetchFrames(this.originVideo)
            this.targetFrames = await this.fetchFrames(this.targetVideo)

            this.framesLoading = [false, false]
            // if (this.markDone || this.todoBvidList.length == 0)
            //      this.framesLoading = [false, false] 
        },
        async fetchFrames(video, bucketName = this.bucketName) {
            const urls = await getObjectsUrls(bucketName, `${video.topicId}/${video.bvid}`)
            return urls.map(url => ({ image: url }))
        },
        async fetchNextTargetVideo(todoBvidList) {
            let video = {};
            for (let i = this.todoBvidListPtr; i < todoBvidList.length; i++) {
                if (todoBvidList[i] == this.originVideo.bvid) continue;

                video = (await findVideoByBvid(todoBvidList[i])).data;
                let isMarked = (await isMarked(this.originVideo.bvid, todoBvidList[i])).data;
                if (video.mark) continue;
                else break;
            }
            return video
        },
        async onClickNextVideo() {
            const markedObj = { begin: String, end: String, mark: Number, topicId: Number }
            markedObj.topicId = this.originVideo.topicId
            markedObj.begin = this.originVideo.bvid
            markedObj.end = this.targetVideo.bvid
            markedObj.mark = this.radio

            this.buttonLoading = true

            await insertMark(markedObj)
            this.$store.commit('add2Set', markedObj.end)
            this.$store.commit('addMark', markedObj.mark)
            this.markMask |= markedObj.mark

            this.targetVideo = await this.fetchNextTargetVideo(this.todoBvidList)
            this.todoBvidListPtr++
            this.targetFrames = await this.fetchFrames(this.targetVideo)
            this.buttonLoading = false
        },
        async gotoLabel() {
            if (this.originVideo.mark == 0 && this.markMask != 0)
                await setMarkByBvid({ begin: this.originVideo.bvid, end: this.targetVideo.bvid, mark: (this.markMask | this.originVideo.mark) })
            this.$router.push({ path: '/workflow/label', query: { topicId: this.originVideo.topicId, topic: this.originVideo.topic } })
        },
        async nextEpoch() {
            if (this.originVideo.mark == 0 && this.markMask != 0)
                await setMarkByBvid({ begin: this.originVideo.bvid, end: this.targetVideo.bvid, mark: (this.markMask | this.originVideo.mark) })
            this.markMask = 0
            this.todoBvidListPtr = 0
            this.originVideoListPtr++
        },
        videoFilter(video) {
            return { topic: this.originVideo.topic, bvid: video.bvid, title: video.title, author: video.author, area: video.area, tag: video.tag }
        },
        truncate(value, length) {
            if (value != null)
                return value.length > length
                    ? value.substr(0, length) + '...'
                    : value
        },
    },
    watch: {
        originVideo() {
            this.initialize()
        }
    }
}
</script>