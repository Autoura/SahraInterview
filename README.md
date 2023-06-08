# Sahra Interview

Interview Sahra using a Furhat robot with answers either powered by OpenAI chat API or as a puppet under manual control

## One time setup
* Setup an OpenAI account & API key
* Within the assets/gui directory, you need to build the UI app:
```
npm install
npm run build
```

This skill can be used either:
* Furhat virtual robot
* Compiled, and used on Furhat hardware robot

## Scope

This skill is designed for web streaming. In particular the robot will not attend a user like _regular_ Furhat skills.

It could be used for stage presentations but you need to think about where the robot looks.

## Modes

The skill can be used in manual mode, or say & listen mode.

## What to tell others

If you want to involve others chatting to Sahra they may not be familiar with how Furhat robots work. These simple rules will help

* Maximum speak time is 20 seconds (Microsoft Speech to Text limit). But really should keep questions and interactions short
* An umm or err is not an interaction
* OpenAI's responses can take 4-5 seconds to reply. Talk when green, don't talk when red

## Roadmap

* Event button for "err" or "ummm" - holding statement
* Longer listening (e.g. listen for 5 minutes then make a comment)
  * Say something (say anything related to conversation)
  * Say summary (summarise conversation to this point)
