<template>
  <div class="home">

    <b-container class="pt-5">

      <b-alert variant="danger" v-if="disconnected" show>Disconnected from robot</b-alert>

      <b-button-toolbar class="pt-2">

        <b-button :disabled="speaking" class="mx-1" @click="send_robot_event('introduction')">
          Introduction
        </b-button>

        <b-button :disabled="speaking" class="mx-1" @click="send_robot_event('goodbye')">
          Final goodbye
        </b-button>

      </b-button-toolbar>

      <b-button-toolbar class="pt-5">

        <b-button :disabled="!speaking" class="mx-1" @click="send_robot_event('SpeechStop')" variant="danger">
          Stop speaking
        </b-button>

        <b-button :disabled="speaking" class="mx-1" @click="send_robot_event('HistoryClear')" variant="danger">
          Clear history
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
      speaking: false,
      last_alive_UTC: Date.now(),
      now: Date.now()
    }
  },
  mounted() {

    FurhatGUI()
        .then(connection => {
          this.furhat = connection
          this.setupSubscriptions()
        })
        .catch(console.error)

    this.interval = setInterval(() => {
      this.now = Date.now();
    }, 5000); // 5 seconds

  },
  computed: {
    disconnected: function () {
      // If we have lost connection between this GUI and the robot skill

      let disconnected = false;

      // If we haven't received an event for 10 seconds
      // last_alive_UTC defaults to when loaded, so will not be disconnected for first 10 seconds after load
      if (this.last_alive_UTC < (this.now - (10 * 1000))) {
        disconnected = true;
      }

      return disconnected;
    },
  },
  methods: {

    setupSubscriptions() {

      this.furhat.subscribe('furhatos.app.sahra.KeepAliveDelivery', () => {
        this.last_alive_UTC = Date.now();
      })

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
