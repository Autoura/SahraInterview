<template>
  <div class="home">

    <b-container class="pt-5">

      <b-button-toolbar class="pt-2">

        <b-button :disabled="!speaking" class="mx-1" @click="send_robot_event('SpeechStop')" variant="danger">
          Stop speaking
        </b-button>

      </b-button-toolbar>

      <b-button-toolbar class="pt-2">

        <b-button :disabled="speaking" class="mx-1" @click="send_robot_event('introduction')">
          Introduction
        </b-button>

        <b-button :disabled="speaking" class="mx-1" @click="send_robot_event('goodbye')">
          Final goodbye
        </b-button>

      </b-button-toolbar>

    </b-container>

  </div>
</template>

<style>

</style>

<script>
import FurhatGUI from 'furhat-gui';

export default {
  name: 'HomeView',
  components: {},
  data() {
    return {
      furhat: null,
      mode: 'screensaver', // QR, screensaver, credentials
      speaking: false
    }
  },
  mounted() {

    FurhatGUI()
        .then(connection => {
          this.furhat = connection
          this.setupSubscriptions()
        })
        .catch(console.error)

  },

  methods: {

    setupSubscriptions() {

      this.furhat.subscribe('SpeechDone', () => {
        this.speaking = false;
      })

      this.furhat.subscribe('SpeechStarted', () => {
        this.speaking = true;
      })

    },

    send_robot_event(event) {
      this.furhat.send({
        event_name: event,
      })
    }

  }
}

</script>
