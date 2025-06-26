# Emolog_Server

> 이 프로젝트는 [프로젝트 목적]을 해결하기 위해 개발되었습니다.  
> [핵심 기능]을 제공하며, [주요 사용 사례]에 활용될 수 있습니다.  
>  
> - **개발 기간**: YYYY.MM.DD ~ YYYY.MM.DD  
> - **참여 인원**: 3명  
> - **배포 URL**: [배포 링크 또는 '미정']  

# Emolog Diary
 
## 📌 프로젝트 개요
간단한 프로젝트 설명 (무엇을 하는 프로젝트인지, 어떤 문제를 해결하는지 등)


## 🚀 기술 스택
- **언어** : Kotlin
- **프레임워크/라이브러리** : Spring Boot, Spring Security, Spring Batch, Spring Data JPA
- **저장소 관련** : MySQL, Redis, MongoDb
- **인증** : JWT, OAuth2 (Google, Kakao)
- **AI/ML** :  : Spring AI, Google Vertex AI, Gemini
- **기타** : Gradle, Docker, Jenkins, SpringDoc OpenAPI, QueryDSL, RabbitMQ 등

## 👥 팀원 소개
| 이름  | 역할             | GitHub |
|-------|----------------|--------|
| 박재욱 | 백엔드 개발(회원 서버)  | [@wodydtns](https://github.com/wodydtns) |
| 백정현 | 백엔드 개발(알림 서버)  | [@junghyun100](https://github.com/junghyun100) |
| 전선웅 | 백엔드 개발(MCP 서버) | [@bbororo5](https://github.com/bbororo5) |

## 📂 프로젝트 구조
```plaintext
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  ├─kotlin
    │  │  └─com
    │  │      └─fiveguysburger
    │  │          └─emodiary
    │  │              ├─client
    │  │              ├─core
    │  │              │  ├─config
    │  │              │  │  └─job
    │  │              │  ├─constant
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  │  ├─request
    │  │              │  │  └─response
    │  │              │  ├─entity
    │  │              │  ├─enums
    │  │              │  ├─exception
    │  │              │  ├─repository
    │  │              │  │  └─impl
    │  │              │  ├─scheduler
    │  │              │  ├─service
    │  │              │  │  └─impl
    │  │              │  └─util
    │  │              └─util
    │  └─resources

```

## 🌟 주요 기능
- ✅ [기능 1]: 간략한 설명
- ✅ [기능 2]: 간략한 설명
- ✅ [기능 3]: 간략한 설명


## 🤝 협업 방식 및 병합 방법
### 브랜치 전략
- `main` : 안정적인 배포 버전
- `develop` : 개발 브랜치
- `feature/{기능명}` : 새로운 기능 개발
- `hotfix/{수정명}` : 긴급 수정 사항
- `refactor/{수정명}` : 리팩토링
