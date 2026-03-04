# OSS Fitness Tracker 🏋️‍♂️

> **"누구나 쉽게 시작하고, 데이터로 성장하는 피트니스 솔루션"**

본 프로젝트는 입문자부터 숙련자까지 아우르는 사용자 경험을 제공하며, 객체지향 설계 원칙(SOLID)과 확장성을 고려한 아키텍처를 기반으로 개발된 오픈소스 피트니스 트래커입니다.

## 🎯 설계 배경 및 목적 (OSS Design)

본 앱은 `OSS_설계.pdf`에 정의된 요구 사항을 바탕으로 개발되었습니다.

- **페르소나 대응**: '무엇부터 해야 할지 모르는 헬린이', '루틴 설계에 어려움을 느끼는 루틴 미아' 등을 위한 맞춤형 솔루션 제공.
- **확장성(Extensibility)**: 새로운 운동 종목, 추천 알고리즘, 저장소 방식(DB 등)을 기존 코드 수정 없이 추가할 수 있는 인터페이스 기반 설계.
- **유연성(Flexibility)**: **Strategy Pattern**을 활용하여 사용자의 목적에 맞는 운동 추천 로직을 동적으로 전환.

## 🌟 주요 기능

### 1. 맞춤형 프로필 및 온보딩
- 사용자의 수준, 가용 시간, 보유 기구, 운동 목적(근비대/다이어트/체력)을 입력받아 최적의 환경을 설정합니다.

### 2. 하이브리드 루틴 시스템
- **자동 추천 (Strategy)**: 검증된 루틴 템플릿을 기반으로 사용자 목적에 맞는 루틴을 AI 기반 또는 전략 기반으로 즉시 제안합니다.
- **커스텀 디자인 (Designer)**: 사용자가 직접 운동 라이브러리에서 종목을 선택하고 세트/횟수/무게를 커스터마이징할 수 있습니다.

### 3. 직관적인 UI/UX
- **현대적인 CSS**: JavaFX에 외부 스타일시트를 적용하여 웹/모바일 앱 수준의 깔끔한 가시성을 확보했습니다.
- **즉시 피드백**: 운동 완료 시 총 볼륨을 즉시 계산하여 성취감을 고취합니다.

### 4. 데이터 지속성 (Persistence)
- **Repository Pattern**: 사용자 데이터 및 운동 기록을 안정적으로 저장하고 불러오며, 향후 RDB(PostgreSQL 등)로의 확장이 용이한 구조입니다.

## 🏗 아키텍처 (Architecture)

본 프로젝트는 **Clean Architecture**와 **전략 패턴**을 핵심으로 합니다.

- **Presentation Layer**: JavaFX 기반의 UI 및 사용자 입력 검증.
- **Application Layer (Use Case)**: 운동 루틴 추천, 운동 기록 로그 등 핵심 비즈니스 로직 처리.
- **Domain Layer**: `User`, `Exercise`, `Routine` 등 핵심 엔티티 및 도메인 규칙 정의.
- **Infrastructure Layer**: `FileRepository`를 통한 데이터 지속성 구현.

## 🚀 시작하기

### 실행 환경
- **Java**: JDK 17 이상
- **Build**: Maven 3.6+

### 실행 방법
```bash
git clone https://github.com/dlwlssud123/OSS_design_pre.git
cd OSS_design_pre/fitness-app
mvn clean compile javafx:run
```

## 📂 프로젝트 구조
```text
src/main/java/com/fitness/
├── domain/          # 핵심 엔티티 (User, Exercise, Routine)
├── service/         # 비즈니스 로직 및 추천 전략 (Strategy Pattern)
├── infrastructure/  # 데이터 저장소 구현 (Repository Pattern)
└── presentation/    # JavaFX UI 구성 및 메인 진입점
```

---
*본 프로젝트는 소프트웨어 설계 프로세스(요구사항 분석 -> 설계 -> 구현 -> 테스트)를 엄격히 준수하여 개발되었습니다.*
