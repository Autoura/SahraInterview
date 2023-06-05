<template>
  <div class="home">

    <b-container class="pt-5">

      <b-alert variant="danger" v-if="disconnected" show>Disconnected from robot</b-alert>

      <b-alert class="float-right" variant="warning" v-if="thinking" show>Thinking</b-alert>
      <b-alert class="float-right" variant="secondary" v-if="listening" show>Listening</b-alert>
      <b-alert class="float-right" variant="success" v-if="speaking" show>Speaking</b-alert>

      <h4>Listening</h4>

      <b-form-group class="mb-0">
        <b-form-radio-group style="width:50vw"
                            id="listen_mode"
                            v-model="listen_mode"
                            :options="[
              { text: 'Manual', value: 'nointeraction', disabled: false },
             // { text: 'Listen', value: 'listen', disabled: true },
              { text: 'Listen & Reply', value: 'listenreply', disabled: false }]"
                            name="listen_mode"
                            button-variant="outline-secondary"
                            buttons
                            @change="send_robot_listen_mode(listen_mode)"
        ></b-form-radio-group>
      </b-form-group>

      <b-button-toolbar class="pt-2">

        <b-button variant="success" :disabled="thinking || speaking || listening || listen_mode ==='nointeraction'" class="mr-2"
                  @click="send_robot_event('listen')">
          Listen now
        </b-button>

        <b-button :disabled="!listening" class="mr-2" @click="send_robot_event('ListenStop')" variant="danger">
          Stop listening
        </b-button>

      </b-button-toolbar>

      <h4 class="pt-4">Speaking</h4>

      <b-button-toolbar class="pt-1">

        <b-button :disabled="speaking" class="mr-2" @click="send_robot_event('introduction')">
          Introduction
        </b-button>

        <b-button :disabled="speaking" class="mr-2" @click="send_robot_event('goodbye')">
          Final goodbye
        </b-button>

        <b-button :disabled="!speaking" class="mr-2" @click="send_robot_event('SpeechStop')" variant="danger">
          Stop speaking
        </b-button>

        <!--<b-button :disabled="speaking" class="mr-2" @click="send_robot_event('HistoryClear')" variant="danger">
  Clear dialogue history
</b-button> -->

      </b-button-toolbar>

      <b-form-textarea
          class="mt-2"
          style="width:50vw"
          id="textarea"
          v-model="say"
          placeholder="Say this..."
          rows="3"
          max-rows="5"
      ></b-form-textarea>

      <b-button-toolbar class="pt-2">

        <b-button v-if="listen_mode === 'nointeraction'" :disabled="!say" class="mr-2" @click="send_robot_say_this()" variant="success">
          Say
        </b-button>

        <b-button :disabled="!say" class="mr-2" @click="send_robot_say_this('listenreply')" variant="success">
          Say & listen
        </b-button>

        <b-button :disabled="!say" class="mr-2" @click="say = ''" variant="danger">
          Clear
        </b-button>

      </b-button-toolbar>

      <h4 class="pt-4">Face</h4>

      <b-button-toolbar class="pt-1">

        <b-button class="mr-2" @click="send_robot_event('unattend')">
          Unattend
        </b-button>

      </b-button-toolbar>

      <b-button-toolbar class="pt-2">

        <b-button variant="success" class="mr-2" @click="send_robot_event('smile')">
          Smile
        </b-button>

        <b-button variant="success" class="mr-2" @click="send_robot_event('nod')">
          Nod
        </b-button>

      </b-button-toolbar>

      <b-button-toolbar class="pt-2">

        <b-button variant="warning" class="mr-2" @click="send_robot_event('disgust')">
          Disgust
        </b-button>

        <b-button variant="warning" class="mr-2" @click="send_robot_event('shake')">
          Shake
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
      now: Date.now(),
      listen_mode: 'nointeraction',
      listening: false,
      thinking: false,
      say: ''
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

      this.furhat.subscribe('SpeechEnded', () => {
        this.speaking = false;
      })

      this.furhat.subscribe('SpeechStarted', () => {
        this.speaking = true;
      })

      this.furhat.subscribe('ListeningStarted', () => {
        this.listening = true;
      })

      this.furhat.subscribe('ListeningEnded', () => {
        this.listening = false;
      })

      this.furhat.subscribe('ThinkingStarted', () => {
        this.thinking = true;
      })

      this.furhat.subscribe('ThinkingEnded', () => {
        this.thinking = false;
      })

    },

    send_robot_event(event) {
      this.furhat.send({
        event_name: event,
      })
    },

    send_robot_listen_mode(new_listen_mode) {
      this.furhat.send({
        event_name: "NewListenMode",
        data: new_listen_mode
      })
    },

    send_robot_say_this(new_listen_mode = '') {

      if (new_listen_mode) {
        this.listen_mode = new_listen_mode;
      }

      this.furhat.send({
        event_name: "SayThis",
        data: this.say,
        new_listen_mode: new_listen_mode
      })
    }

  }
}

</script>
