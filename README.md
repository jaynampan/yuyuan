# Yuyuan - Learn Chinese

Yuyuan(语缘) is an Android app for Chinese learning, especially words   
This app can run on API 26(Android 8.0) or higher without network, all data is bundled in the apk
file  
**Note**: 🚧Under Construction... Welcome to find bugs and contribute!

# Features
- [x] Material 3 UI powered by Compose 
- [x] HSK1-6 all words included
- [x] Character hand writing exercises
- [ ] Daily learning plan
- [ ] Word Training
- [ ] Lessons designed for different topics
- [ ] Learning calendar
- [ ] Article reading exercises

![demo](./demo/demo.jpg)

# New Updates

- 2025-05-20: Implemented character stroke animation and writing exercise
- 2025-04-21: Implemented basic function(play word & sentence sound, choose speed, choose book)
- 2024-12-13: Updated the structure to a modern way, with data and ui layers

# Build Notes

- This project configures repository mirrors in settings.gradle.kts and
  gradle/wrapper/gradle-wrapper.properties, in case for internet issues.
- Inside gradle.properties, the gradle is set to be able to use 5GB RAM at most, adjust it according
  to your local machine.
- Inside gradle.properties, both configuration cache and build cache are enabled to speed up build
  process. Adjust the settings if you have incompatible issues and clear caches if it's corrupted.



