FROM ubuntu:bionic
RUN apt-get update && \
    apt-get install -y \
      bash \
      curl \
      wget \
      zip \
      gnupg \
      software-properties-common \
      openjdk-8-jdk openjdk-8-jre
#      default-jdk


# Define commonly used JAVA_HOME variable
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre
ENV ANDROID_SDK_ROOT=/android-sdk
ENV SDK_MANAGER=sdk-tools-linux-4333796.zip
ENV ANDROID_PLATFORM_VERSION=28
ENV ANDROID_VERSION=${ANDROID_PLATFORM_VERSION}.0.3
ENV REPO_OS_OVERRIDE=linux
ENV PATH=$PATH:$ANDROID_SDK_ROOT/tools:${ANDROID_SDK_ROOT}/tools/bin

# Install Kotlin and Gradle
RUN curl -s https://get.sdkman.io | bash && \
    bash -c 'source "/root/.sdkman/bin/sdkman-init.sh" && \
    sdk install kotlin 1.3.50 && \
    sdk install gradle 5.1.1'

# Install Android Tools
RUN wget -O /tmp/${SDK_MANAGER} -t 5 "https://dl.google.com/android/repository/${SDK_MANAGER}" && \
    unzip -q /tmp/${SDK_MANAGER} -d ${ANDROID_SDK_ROOT} && \
    rm /tmp/${SDK_MANAGER} && \
    mkdir -p ${ANDROID_SDK_ROOT} && \
    cd ${ANDROID_SDK_ROOT} && \
    sdkmanager --update && \
    sdkmanager "platforms;android-${ANDROID_PLATFORM_VERSION}" "build-tools;${ANDROID_VERSION}" "extras;google;m2repository" "extras;android;m2repository"
RUN yes | sdkmanager --licenses && yes | sdkmanager --update

COPY . /workspace
WORKDIR /workspace

RUN gradle build
