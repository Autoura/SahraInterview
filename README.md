# Sahra Interview

Interview Sahra using a Furhat robot with answers either powered by OpenAI chat API or as a puppet under manual control.

You can find out more about Sahra here: https://www.autoura.com/recipes/sahra

## One time setup
* Setup an OpenAI account & API key
* Within the assets/gui directory, you need to build the UI app:

```
npm install
npm run build
```

This skill can be used either:
* Furhat virtual robot
* Compiled (e.g. IntelliJ), and uploaded/used on a Furhat hardware robot

## Scope

This skill is designed for web streaming. In particular the robot will not attend a user like _regular_ Furhat skills, instead will look ahead (i.e. at a video camera)

This skill could be used for stage presentations but you need to think about where the robot looks, perhaps including panned glancing towards the audience.

## Modes

The skill can be used in manual mode, or say & listen mode.

Or you can jump between modes during a conversation.

## Topics to talk about

* Ask about stories from when Sahra is a tour guide
* Ask about whether Sahra perfers day tours or multiday tours

What you can't ask about is locations or generate an itinerary. We have stopped Sahra talking about this (in this skill) via the OpenAI prompt.

## What to tell others

If you want to involve others chatting to Sahra they may not be familiar with how Furhat robots work. 

These simple rules will help:

* OpenAI's responses can take 4-5 seconds to reply. Talk when the LED is green (listening), don't talk when red (thinking)
* Maximum speak time is 20 seconds (Microsoft Speech to Text limit). But really should keep questions and interactions short
* An umm or err is not an interaction, nor is whistling. It has to be words that the speech to text can pickup

## Roadmap

* Event button for "err" or "ummm" - holding statement
* Longer listening (e.g. listen for 5 minutes then make a comment)
  * Say something (say anything related to conversation)
  * Say summary (summarise conversation to this point)
