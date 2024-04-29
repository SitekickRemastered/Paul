# Paul
- Paul

---

# Commands
**GLOBAL SYNTAX:**
> Paul

---
**create_poll** - *Creates a poll.*
>/create_poll #channel [question] [answers] Optional: [emojis] [duration]
>
> **Note: Answers and Emojis should be separated by commas. (i.e. Option 1, Option 2, Option 3)**

**end_poll** - *Ends a poll using the poll's message ID.*
>/end_poll [message_id]

**depaul** - *Makes Paul ignore a user. Name will be added to a list (bannedUsers.txt).*
> /depaul [user]
>
**enpaul** - *Makes Paul re-watch a user. Name will be removed from a list (bannedUsers.txt).*
> /enpaul [user]
---

# Dependencies
JDA v5.0.0-beta.23 (Included) - https://github.com/discord-jda/JDA

Logback Classic 1.5.6 (Should be installed with Maven) - https://logback.qos.ch/download.html 

Amazon Corretto 21 (OpenJDK) - https://corretto.aws/downloads/latest/amazon-corretto-21-x64-windows-jdk.msi

# Other Notes:
Make sure the environment variables file is named ".env"
