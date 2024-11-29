<template>
    <div>
        <title-bar :title-stack="titleStack" />
        <section class="section is-main-section">

            <card-component style="height:330px;" class="has-text-centered" v-for="(frames, index) in twoFrames"
                :index="index" :item="frames" :key="index">
                <div class="level">
                    <div class="level-item" v-for="(value, key) in (videoFilter(videos[index]))" :key="key">
                        <div>
                            <p style="font-weight: bold;">{{ key }}</p>
                            <p>{{ truncate(value, 50) }}</p>
                        </div>
                    </div>
                </div>
                <b-carousel-list :data="frames" :items-to-show="10" />
                <b-loading :is-full-page="false" :active="loading[index]"></b-loading>
                <div class="container" v-if="isEmpty[index]">
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
                        <b-button v-if="!markDone" :loading="this.buttonLoading" type="is-primary is-light"
                            @click="onClickNextVideo()" :disabled="radio == 0">下一个视频</b-button>
                        <b-button v-else type="is-primary" @click="gotoLabel()">下一步</b-button>
                    </div>
                </div>
            </card-component>
        </section>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findVideoByBvid, setMarkedByBvid } from '@/api/video'
import { insertMark, findTodoBvidList } from '@/api/mark'
import { getObjectsUrls } from '@/utils/minio'

export default {
    components: {
        TitleBar,
        CardComponent,
    },
    created() {
        this.videos[0] = this.$route.query
        this.baseBvid = this.videos[0].bvid
        this.topicId = this.videos[0].topicId
        this.fetchTodoBvidList(this.baseBvid, this.topicId)
        console.log(this.todoBvidList)
        this.fetchFrames(0)
        if (this.markDone || this.todoBvidList.length == 0)
            this.loading[1] = this.loading[0] = false;

    },
    data() {
        return {
            minioBaseURL: import.meta.env.VITE_MINIO_BASE_API,
            titleStack: ['Workflow', 'Label'],
            isEmpty: [true, true],
            loading: [true, true],
            buttonLoading: false,
            pos: 0,
            videos: [{}, {}],
            twoFrames: [[], []],
            baseBvid: String,
            todoBvidList: [],
            radio: 0,
            MARK_EMUMS: {
                Copy: 1 << 0,
                Event: 1 << 1,
                Instance: 1 << 2,
                Reasoning: 1 << 3,
                Dependent: 1 << 4,
                Independent: 1 << 5,
            },
        }
    },
    computed: {
        markDone() {
            return this.videos[0].marked || this.pos == this.todoBvidList.length;
        }
    },
    methods: {
        videoFilter(video) {
            return { bvid: video.bvid, title: video.title, author: video.author, area: video.area, tag: video.tag }
        },
        truncate(value, length) {
            if (value != null)
                return value.length > length
                    ? value.substr(0, length) + '...'
                    : value
        },
        gotoLabel() { },
        async onClickNextVideo() {
            const markObj = { begin: String, end: String, mark: Number, topicId: Number }
            markObj.topicId = this.topicId;
            markObj.begin = this.baseBvid;
            markObj.end = this.videos[1].bvid;
            markObj.mark = this.radio;

            await insertMark(markObj);
            this.buttonLoading = true;
            await this.fetchNextRefVideo();
            this.buttonLoading = false;
            this.radio = 0;
        },
        async fetchFrames(videoIdx) {
            const video = this.videos[videoIdx];
            const arr = await getObjectsUrls("archived", `${video.topicId}/${video.bvid}`)
            if (arr.length != 0)
                this.$set(this.isEmpty, videoIdx, false)
            this.$set(this.twoFrames, videoIdx, arr.map(url => ({ image: url })))
            this.$set(this.loading, videoIdx, false)
        },
        async fetchTodoBvidList(bvid, topicId) {
            this.todoBvidList = (await findTodoBvidList(bvid, topicId)).data
            if (this.todoBvidList.length > 0)
                this.fetchNextRefVideo()
        },
        async fetchNextRefVideo() {
            let video = null;
            for (let i = this.pos; i < this.todoBvidList.length; i++) {
                const bvid = this.todoBvidList[i];
                if (bvid == this.baseBvid) continue;

                const result = await findVideoByBvid(bvid);
                video = result.data
                if (video.marked) continue;
                else break;
            }
            this.$set(this.videos, 1, video)
            await this.fetchFrames(1);
            this.pos++;
        }
    },
    watch: {
        markDone(newValue) {
            if (newValue) setMarkedByBvid(this.baseBvid);
        }
    }
}
</script>