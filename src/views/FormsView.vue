<template>
  <section class="section is-main-section">
      <b-carousel :autoplay="false" indicator-custom :indicator-inside="true" :overlay="gallery"
          @click="switchGallery(true)">
          <b-carousel-item v-for="(item, i) in 20" :key="i">
          </b-carousel-item>
          <span v-if="gallery" @click="switchGallery(false)" class="modal-close is-large" />
          <template #indicators="props">
              <figure class="image al" :draggable="false">
                  <img :draggable="false" :src="getImgUrl(props.i)" :title="props.i">
              </figure>
          </template>
      </b-carousel>
      
      <hr>
      
      <b-carousel :autoplay="false" indicator-custom :indicator-inside="false" :overlay="gallery"
          @click="switchGallery(true)">
          <b-carousel-item v-for="(item, i) in 20" :key="i">
          </b-carousel-item>
          <span v-if="gallery" @click="switchGallery(false)" class="modal-close is-large" />
          <template #indicators="props">
              <figure class="image al" :draggable="false">
                  <img :draggable="false" :src="getImgUrl(props.i)" :title="props.i">
              </figure>
          </template>
      </b-carousel>

      <div class="block">
          <b-radio v-model="radio" name="name" native-value="Copy">
              Copy
          </b-radio>
          <b-radio v-model="radio" name="name" native-value="Event">
              Event
          </b-radio>
          <b-radio v-model="radio" name="name" native-value="Independent">
              Independent
          </b-radio>
          <b-radio v-model="radio" name="name" native-value="Dependent">
              Dependent
          </b-radio>
      </div>
      
  </section>
</template>

<script>
export default {
  data() {
      return {
          gallery: false
      }
  },
  methods: {
      getImgUrl(value) {
          value += 50
          return `https://picsum.photos/id/10${value}/1230/500`
      },
      switchGallery(value) {
          this.gallery = value
          if (value) {
              return document.documentElement.classList.add('is-clipped')
          } else {
              return document.documentElement.classList.remove('is-clipped')
          }
      }
  }
}
</script>

<style>
.is-active .al img {
  border: 1px solid #fff;
  filter: grayscale(0%);
}

.al img {
  border: 1px solid transparent;
  filter: grayscale(100%);
}
</style>