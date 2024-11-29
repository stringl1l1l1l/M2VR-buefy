<template>
    <div>
        <section class="section is-main-section">
            <title-bar :title-stack="titleStack" />
            <card-component class="has-table has-mobile-sort-spaced">
                <table-sample :loading="loading" :default-sort-field="'topicId'" :data="list" :columns="columns"
                    :perPage="10" width="5em" :checkable="false">

                    <b-table-column width="5em" label="Status" v-slot="props">
                        <div class="column">
                            <b-tag v-if="props.row['marked'] == 0" type="is-danger"> 未开始 </b-tag>
                            <b-tag v-else-if="props.row['marked'] < props.row['total']"> 未完成 </b-tag>
                            <b-tag v-else type="is-primary">已完成 </b-tag>
                        </div>
                    </b-table-column>

                    <b-table-column width="5em" label="Operation" v-slot="props">
                        <div class="column">
                            <b-button v-if="props.row['marked'] == 0" size="is-small" type="is-primary is-light"
                                @click="next(props.row['topicId'])">开始</b-button>
                            <b-button v-else size="is-small" type="is-info is-light"
                                @click="next(props.row['topicId'])">继续</b-button>
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
import { findAllTopics } from "@/api/topic";
import CardComponent from '@/components/CardComponent.vue'
import TitleBar from '@/components/TitleBar.vue'
import HeroBar from '@/components/HeroBar.vue'
import TableSample from '@/components/TableSample.vue'

export default defineComponent({
    name: 'TopicView',
    components: {
        HeroBar,
        TitleBar,
        CardComponent,
        TableSample,
        NotificationBar
    },
    created() {
        this.fetchData();
        this.columns = Object.keys(this.entity)
            .map(key => ({
                field: key,
                label: key // 使用自定义 label 或默认使用属性名
            }));
    },
    data() {
        return {
            titleStack: ['WorkFlow', 'Topic'],
            list: [],
            columns: Array,
            loading: true,
            entity: {
                topicId: 0,
                topic: "",
                total: 0,
                marked: 0,
                labeled: 0
            },
        }
    },
    methods: {
        fetchData() {
            findAllTopics().then(response => {
                this.list = response.data;
                this.loading = false;
            })
        },
        next(param) {
            this.$router.push({ path: '/workflow/video', query: { topicId: param } });
        }
    }
})
</script>
    