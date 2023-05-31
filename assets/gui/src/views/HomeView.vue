<template>
    <div class="home">

      <button class="btn btn-lg" type="button" @click="say('hello')">
        Hello
      </button>

      <button class="btn btn-lg" type="button" @click="say('goodbye')">
        Goodbye
      </button>

    </div>
</template>

<style>

.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    flex-direction: column; /* set the flex-direction property to column */
}

.preference {
    color: whitesmoke;
}

.preference_emoji {
    padding-left: 12px;
    padding-right: 12px;
}

.header {
    color: whitesmoke;
}

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
        }
    },
    mounted() {

        FurhatGUI()
            .then(connection => {
                this.furhat = connection
            })
            .catch(console.error)

    },

    methods: {

      say(event) {

        if (event === "hello") {
          this.furhat.send({
            event_name: "hello",
          })
        }

        if (event === "goodbye") {
          this.furhat.send({
            event_name: "hello",
          })
        }

      }

    }
}

</script>
