<template>
    <div>
        <meta name="referrer" content="no-referrer">
        <modal-box :is-active="isModalActive" :trash-object-name="trashObject ? trashObject.name : null"
            @confirm="trashConfirm" @cancel="trashCancel" />

        <div class="box">
            <b-input v-model="filter" placeholder="搜索..." expanded></b-input>
        </div>

        <b-table :checked-rows.sync="checkedRows" :checkable="checkable" narrowed mobile-cards :debounce-search="1000"
            paginated :per-page="perPage" :current-page="currentPage" :pagination-position="paginationPosition" :data="data"
            default-sort="bvid" striped hoverable>

            <b-table-column sortable width="10em" v-for="(col, index) in columns" :key="index" :field="col.field"
                :label="col.label" v-slot="props">
                <span v-if="col.field != 'cover'"> {{ props.row[col.field] }} </span>
                <b-image v-else lazy :src="props.row[col.field]"> </b-image>
            </b-table-column>

            <b-table-column width="1em" field="cover" label="Operation">
                <div class="container">
                    <div class="column">
                        <b-button class="is-fullwidth" size="is-small" type="is-info">Update</b-button>
                    </div>
                    <div class="column">
                        <b-button class="is-fullwidth" size="is-small" type="is-danger">Delete</b-button>
                    </div>
                    <div class="column">
                        <b-button class="is-fullwidth" size="is-small" type="is-primary">Mark</b-button>
                    </div>
                </div>
            </b-table-column>

        </b-table>
    </div>
</template>
  
<script>
import { defineComponent } from 'vue'
import ModalBox from '@/components/ModalBox.vue'

export default defineComponent({
    name: 'TableSample',
    components: { ModalBox },
    props: {
        data: Array,
        columns: Array,
        checkable: Boolean,
        isEmpty: Boolean,
        perPage: {
            type: Number,
            default: 4
        }
    },
    data() {
        return {
            checkedRows: [],
            isModalActive: false,
            trashObject: null,
            filter: '',
            currentPage: 1,
            paginationPosition: 'bottom',
        }
    },
    methods: {
        trashModalOpen(obj) {
            this.trashObject = obj
            this.isModalActive = true
        },
        trashConfirm() {
            this.isModalActive = false

            this.$buefy.snackbar.open({
                message: 'Confirmed',
                queue: false
            })
        },
        trashCancel() {
            this.isModalActive = false
        }
    }
})

</script>
  