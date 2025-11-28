
# 🟦 FaceAuth — Aadhaar/ID Based Face Authentication SDK (Android)

### On-device, secure facial verification using Aadhaar / PAN / Documents  
No server required · Works Offline · Privacy Safe 🔐 · TFLite Based

---


## 🚀 Why FaceAuth?

| Feature | Status |
|-------|:---:|
| Aadhaar/Document Face Extraction | 🟢 |
| Live Selfie Camera Detection | 🟢 |
| Offline Face Recognition (TFLite) | 🟢 |
| Threshold Verification Control | 🟢 |
| Document + Identity Verification Modes | 🟢 |
| Plug & Play SDK – 2 Lines Integration | 🔥 |
| No Cloud / No Upload Needed | 🔐 |

---



## 📥 Installation (JitPack)

### 1️⃣ Add JitPack Repository  
In **settings.gradle / settings.gradle.kts**

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```
---


## Add Dependency

### 2️⃣ Add Dependency

```kotlin
dependencies {
    implementation("com.github.Ojaswakesharwani:FaceAuthLibProject:v1.0.0")
}
```
---


## ⚡ Quick Start - Just 2 Lines!

```kotlin
FaceAuth.init(this,AuthenticationMode.IDENTITY/DOCUMENT)
FaceAuth.pickAadhaarImage()   // Step 1 → Aadhaar upload screen opens
```
---


## Library Flow (Auto Handling)

```kotlin
Upload Aadhaar → Extract Face → Capture Selfie → Compare → Result Screen
```
---


## 🔍 Verification Modes

```kotlin
| Mode                  | Use Case                                   | Threshold |
| --------------------- | ------------------------------------------ | --------- |
| DOCUMENT_VERIFICATION | Aadhaar/PAN documents (old images allowed) | 1.10f     |
| IDENTITY_VERIFICATION | Strong real-time identity match            | 0.90f     |
```
---


## 🧠 Face Score Meaning

```kotlin
| Distance      | Result                          |
| ------------- | ------------------------------- |
| `< 0.90`      | Strong Match ✔                  |
| `0.90 - 1.10` | Similar · Needs better selfie ⚠ |
| `> 1.10`      | Mismatch ❌                      |
```
---


## 🤝 Contribute

### We welcome PRs, Issues & Feature Requests.
```kotlin
Fork → Improve → Push → Pull Request
```
---


## 👨‍💻 Author
### Ojaswa Kesharwani
Android Developer | ML + TFLite | KYC Solutions
 ---


![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Release](https://img.shields.io/badge/version-v1.0.0-blue)
![Platform](https://img.shields.io/badge/Android-SDK%2021%2B-green)
![License](https://img.shields.io/badge/License-Apache--2.0-yellow)
![Verification](https://img.shields.io/badge/Aadhaar%20Match-Offline%20AI-blue)

