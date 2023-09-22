FROM reactnativecommunity/react-native-android:latest
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew assembleDebug
