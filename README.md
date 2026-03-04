# OSS Fitness Tracker 🏋️‍♂️

현대적인 JavaFX UI와 개인화된 운동 추천 로직을 갖춘 자바 기반 피트니스 트래커 애플리케이션입니다.

## 🌟 주요 기능

- **현대적인 CSS UI**: JavaFX에 외부 CSS를 적용하여 웹/모바일 앱 스타일의 세련된 디자인을 구현했습니다.
- **개인화된 자동 추천**: 사용자의 운동 목적(근비대, 다이어트, 체력 증진)에 따라 최적화된 운동 루틴을 자동으로 추천합니다.
- **커스텀 루틴 생성**: 시중의 전문 앱처럼 운동 라이브러리에서 원하는 종목을 직접 선택하여 나만의 루틴을 구성할 수 있습니다.
- **프로필 관리**: 이름, 체중, 목표 체중 및 운동 목적을 설정하고 로컬 파일(`user_data.dat`)에 안전하게 저장합니다.
- **운동 기록**: 추천받거나 생성한 루틴으로 운동을 시작하고 완료 시 총 볼륨 등의 데이터를 기록합니다.

## 🛠 기술 스택

- **Language**: Java 17
- **UI Framework**: JavaFX
- **Build Tool**: Maven
- **Design Pattern**: Strategy Pattern (운동 추천 로직), Repository Pattern (데이터 저장)
- **Styling**: Vanilla CSS for JavaFX

## 🚀 실행 방법

### 요구 사항
- JDK 17 이상
- Maven

### 실행 명령
프로젝트 루트 디렉토리에서 아래 명령어를 실행하세요.

```bash
cd fitness-app
mvn clean compile javafx:run
```

## 📂 프로젝트 구조

- `com.fitness.domain`: 운동, 루틴, 사용자 등 핵심 도메인 모델
- `com.fitness.service`: 운동 추천 전략(Strategy) 및 비즈니스 로직
- `com.fitness.infrastructure`: 파일 기반 데이터 저장소 (Repository)
- `com.fitness.presentation`: JavaFX 기반 GUI (MainApp)
- `src/main/resources`: 앱 스타일을 담당하는 `style.css`

---
*본 프로젝트는 오픈소스 설계 원칙을 준수하여 개발되었습니다.*
