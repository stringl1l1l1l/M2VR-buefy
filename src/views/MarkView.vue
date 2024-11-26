<template>
    <div>
        <title-bar :title-stack="titleStack" />
        <section class="section is-main-section">
            <card-component class="" v-for="(item, index) in frames" :index="index" :item="item" :key="item.id">
                <span class="">
                    frame {{ index }}
                </span>
                <b-carousel-list v-model="values" :data="item" :arrow="arrow" :arrow-hover="arrowHover"
                    :items-to-show="perList" :items-to-list="increment" :indicator-inside="true" :has-drag="drag"
                    :has-grayscale="gray" :has-opacity="opacity" />

            </card-component>

            <card-component>
                <div class="columns">
                    <div class="column has-text-centered">
                        <b-radio v-model="radio" name="Copy" native-value="Copy">
                            Copy
                        </b-radio>
                    </div>
                    <div class="column has-text-centered">
                        <b-radio v-model="radio" name="Event" native-value="Event">
                            Event
                        </b-radio>
                    </div>
                    <div class="column has-text-centered">
                        <b-radio v-model="radio" name="Independent" native-value="Independent">
                            Independent
                        </b-radio>
                    </div>
                    <div class="column has-text-centered">
                        <b-radio v-model="radio" name="Dependent" native-value="Dependent">
                            Dependent
                        </b-radio>
                    </div>

                </div>

                <div class="columns">
                    <div class="column has-text-centered">
                        <b-button type="is-primary">Submit</b-button>
                    </div>
                </div>
            </card-component>
        </section>
    </div>
</template>

<script>
import TitleBar from '@/components/TitleBar.vue'
import CardComponent from '@/components/CardComponent.vue'
export default {
    components: {
        TitleBar,
        CardComponent,
    },
    data() {
        return {
            titleStack: ['Home', 'Mark'],
            frames: [
                [{ 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/0.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/18.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/27.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/36.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/44.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/54.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/62.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/71.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/80.jpg' }, { 'title': 'BV11J4m1G7tJ', 'image': 'http://localhost:9000/archived/1/BV11J4m1G7tJ/9.jpg' }]
                ,
                [{ 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/0.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/10.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/16.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/21.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/26.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/31.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/36.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/41.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/46.jpg' }, { 'title': 'BV11M4m197F7', 'image': 'http://localhost:9000/archived/1/BV11M4m197F7/5.jpg' }]
            ],
            radio: '',
            arrow: true,
            arrowHover: true,
            drag: true,
            gray: false,
            opacity: false,
            values: 1,
            perList: 10,
            increment: 1,
            repeat: false
        }
    }
}
</script>