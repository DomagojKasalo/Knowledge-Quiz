# Knowledge Quiz Game Application

## Overview
This Java-based mobile application is an engaging "Knowledge Quiz" game that challenges users to answer questions correctly within a set time frame. 
Utilizing Firebase for user authentication, it offers a seamless experience allowing players to test their knowledge across various subjects. 
The game records scores, enabling players to track their progress and compete for a place on the global leaderboard.

## Features
* **User Authentication:** Utilizes Firebase to manage user sign-in, ensuring that only authenticated users can access the game and save their scores.
* **Gameplay Mechanics:** Players answer quiz questions to earn points and additional time. Incorrect answers may result in penalties. The game concludes when the timer reaches zero.
* **Interruption Handling:** If a call interrupts gameplay, the game pauses, preserving the current score and state. After handling the call, players can resume the quiz without losing progress.
* **Celebratory Animations:** Achieving a high score triggers a special celebration screen, enhancing the user experience.
* **Leaderboard:** Features a global leaderboard showcasing the top 10 scores among all players, encouraging competition and repeated play.
  
## Technology Stack
* Java: The primary development language for creating the application.
* Firebase: Used for handling user authentication and potentially storing user scores and leaderboard data.
* Android SDK: Provides the tools and APIs necessary to develop, test, and debug the application for Android devices.
  
## Development and Deployment
This application is developed using Android Studio, the official IDE for Android development. 
Ensure you have the latest version of Android Studio and the Android SDK to build and run the application.

## Future Enhancements
* Expansion of the quiz question database to cover more topics and increase variability.
* Introduction of difficulty levels to cater to players of different knowledge levels.
* Implementation of social features, allowing users to challenge friends and share scores.
