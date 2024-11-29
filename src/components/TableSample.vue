<template>
    <div>
        <meta name="referrer" content="no-referrer">
        <modal-box :is-active="isModalActive" :trash-object-name="trashObject ? trashObject.name : null"
            @confirm="trashConfirm" @cancel="trashCancel" />

        <div v-if="searchable" class="box">
            <b-input icon="magnify" type="search" v-model="filter" placeholder="搜索..." expanded></b-input>
        </div>

        <b-table narrowed mobile-cards paginated striped hoverable :loading="loading" :checked-rows.sync="checkedRows"
            :checkable="checkable" :per-page="perPage" :current-page="currentPage" :pagination-position="paginationPosition"
            :data="data" :default-sort="columns[defaultSortCol].field">

            <b-table-column sortable :width="width" v-for="(col, index) in columns" :key="index" :field="col.field"
                :label="col.label" v-slot="props">
                <div class="column">
                    <b-image v-if="col.field == 'cover'" lazy :src="props.row[col.field]"> </b-image>
                    <span v-else-if="col.field.toLowerCase().includes('time')"> {{ parseTime(props.row[col.field]) }}
                    </span>
                    <span v-else-if="col.field.toLowerCase().includes('duration')"> {{ parseDuration(props.row[col.field])
                    }}
                    </span>
                    <span v-else> {{ props.row[col.field] | truncate(maxChar) }} </span>
                </div>
            </b-table-column>

            <template>
                <slot></slot>
            </template>
        </b-table>
    </div>
</template>
  
<script>
import { defineComponent } from 'vue'
import ModalBox from '@/components/ModalBox.vue'
import { parseTime, parseDuration } from '@/utils'

export default defineComponent({
    name: 'TableSample',
    components: { ModalBox },
    props: {
        loading: {
            type: Boolean,
            default: true
        },
        searchable: {
            type: Boolean,
            default: true
        },
        maxChar: {
            type: Number,
            default: 50
        },
        data: Array,
        defaultSortCol: {
            type: Number,
            default: 0
        },
        columns: Array,
        checkable: Boolean,
        isEmpty: Boolean,
        width: {
            type: String,
            default: "10em"
        },
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
        parseTime,
        parseDuration,

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
    },
    filters: {
        truncate(value, length) {
            if (value != null)
                return value.length > length
                    ? value.substr(0, length) + '...'
                    : value
        },
    }
})

</script>
  