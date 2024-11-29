<template>
    <div>
        <title-bar :title-stack="titleStack" />
        <section class="section is-main-section">

            <card-component style="height:330px;" class="has-text-centered" v-for="(frames, index) in twoFrames"
                :index="index" :item="frames" :key="frames">
                <div class="level">
                    <div class="level-item" v-for="(value, key) in (videoFilter(videos[index]))" :key="key">
                        <div>
                            <p style="font-weight: bold;">{{ key }}</p>
                            <p>{{ value }}</p>
                        </div>
                    </div>
                </div>
                <b-carousel-list :data="frames" :items-to-show="10" />
                <b-loading :is-full-page="false" :active="loading[index]"></b-loading>
                <div class="container" v-if="isEmpty[index]">
                    暂无数据
                </div>
            </card-component>

            <card-component>
                <div class="columns">
                    <div v-for="(value, key) in marks" :key="key" class="column has-text-centered">
                        <b-radio v-model="radio" :name="key" :native-value="value">
                            {{ key }}
                        </b-radio>
                    </div>
                </div>
                <div class="columns">
                    <div class="column has-text-centered">
                        <b-button :loading="this.buttonLoading" type="is-primary is-light" @click="fetchNextRefVideo()"
                            :disabled="radio == 0 || markDone">下一个视频</b-button>
                    </div>
                </div>
            </card-component>
        </section>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
import { findNotMarkedBvidListWithTopicId, findVideoByBvid } from '@/api/video'
import { getObjectsUrls } from '@/utils/minio'

export default {
    components: {
        TitleBar,
        CardComponent,
    },
    created() {
        this.videos[0] = this.$route.query
        this.topicId = this.videos[0].topicId
        this.fetchBvidList(this.topicId)
        this.fetchFrames(0)
    },
    data() {
        return {
            minioBaseURL: import.meta.env.VITE_MINIO_BASE_API,
            titleStack: ['Workflow', 'Mark'],
            isEmpty: [true, true],
            loading: [true, true],
            buttonLoading: false,
            pos: 0,
            videos: [{}, {}],
            twoFrames: [[], []],
            bvidList: [],
            radio: 1,
            marks: {
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
            return this.pos == this.bvidList.length;
        }
    },
    methods: {
        videoFilter(video) {
            return { bvid: video.bvid, title: video.title, author: video.author, area: video.area, tag: video.tag }
        },
        async fetchFrames(videoIdx) {
            const video = this.videos[videoIdx];
            console.log()
            const arr = await getObjectsUrls("archived", `${video.topicId}/${video.bvid}`)
            if (arr.length != 0)
                this.$set(this.isEmpty, videoIdx, false)
            this.$set(this.twoFrames, videoIdx, arr.map(url => ({ image: url })))
            this.$set(this.loading, videoIdx, false)
        },
        async fetchBvidList(topicId) {
            this.bvidList = (await findNotMarkedBvidListWithTopicId(topicId)).data
            this.fetchNextRefVideo()
        },
        async fetchNextRefVideo() {
            this.buttonLoading = true;
            let video = null;
            for (let i = this.pos; i < this.bvidList.length; i++) {
                const bvid = this.bvidList[i];
                if (bvid == this.videos[0].bvid) continue;

                const result = await findVideoByBvid(bvid);
                video = result.data
                if (video.marked) continue;
                else break;
            }
            this.$set(this.videos, 1, video)
            await this.fetchFrames(1);
            this.pos++;
            this.buttonLoading = false;
            this.radio = 1;
        }
    }
}
</script>