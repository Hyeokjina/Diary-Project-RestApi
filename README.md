# 일기장 REST API 서버

이 프로젝트는 React 기반 일기장 애플리케이션에서 사용할
백엔드 REST API 서버입니다.

기존에 구현했던 Diary-Project-react 프로젝트를 기반으로,
프론트엔드에서 사용하던 기능들을 REST API 구조로 재설계하고 분리하는 것을 목표로 합니다.

React 클라이언트와의 연동을 고려하여
회원 관리, 일기 CRUD 기능 등을 API 형태로 제공하도록 구현되었습니다.

또한 API 동작을 직접 확인하고 테스트하기 위해
static 폴더에 HTML / CSS / JavaScript 기반의 정적 페이지를 함께 구성했습니다.
이를 통해 별도의 프론트엔드 빌드 과정 없이도
로그인, 일기 작성 및 조회 등 주요 기능을 바로 테스트할 수 있도록 했습니다.

해당 구조는 프론트엔드와 백엔드 분리 아키텍처를 유지하면서,
추후 React와 같은 SPA 프론트엔드로 교체하더라도
API 서버를 그대로 활용할 수 있도록 설계되었습니다.

<sub>🔗 프론트엔드 프로젝트:
https://github.com/Hyeokjina/Diary-Project-react
</sub>


## 프로젝트 개요

이 프로젝트는 일기장 기능을 제공하는 RESTful API 서버로, 회원가입/로그인, 일기 CRUD 기능을 제공합니다.

## 사용 기술 스택

- **Java 17**
- **Spring Boot 3.x**
- **MyBatis**
- **H2 Database** (인메모리 데이터베이스)
- **Gradle**
- **Lombok**

## 주요 도메인

### Member (회원)
- 회원 정보 관리
- 회원가입 및 로그인 기능

### Diary (일기)
- 일기 작성, 조회, 수정, 삭제
- 감정 기록 (emotion)
- 회원별 일기 조회

## API 명세

### 1. 회원 API

#### 1.1 회원가입
- **URL**: `POST /api/members/signup`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "홍길동"
}
```
- **Response**:
  - **201 Created**: 회원가입 성공
  - **409 Conflict**: 이메일 중복
  - **400 Bad Request**: 잘못된 요청
- **Response Body** (성공 시):
```json
"회원가입 성공"
```

#### 1.2 로그인
- **URL**: `POST /api/members/login`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
- **Response**:
  - **200 OK**: 로그인 성공
  - **400 Bad Request**: 이메일 또는 비밀번호 오류
- **Response Body** (성공 시):
```json
{
  "id": 1,
  "email": "user@example.com",
  "nickname": "홍길동",
  "createdAt": "2024-12-15T10:30:00"
}
```

---

### 2. 일기 API

#### 2.1 전체 일기 목록 조회
- **URL**: `GET /api/diaries`
- **Response**:
  - **200 OK**: 조회 성공
- **Response Body**:
```json
[
  {
    "id": 1,
    "title": "오늘의 일기",
    "emotion": "happy",
    "createdAt": "2024-12-15T10:30:00"
  },
  {
    "id": 2,
    "title": "즐거운 하루",
    "emotion": "excited",
    "createdAt": "2024-12-14T09:20:00"
  }
]
```

#### 2.2 회원별 일기 목록 조회
- **URL**: `GET /api/diaries/member/{memberId}`
- **Path Variable**: `memberId` (Long)
- **Response**:
  - **200 OK**: 조회 성공
- **Response Body**:
```json
[
  {
    "id": 1,
    "title": "오늘의 일기",
    "emotion": "happy",
    "createdAt": "2024-12-15T10:30:00"
  }
]
```

#### 2.3 일기 상세 조회
- **URL**: `GET /api/diaries/{id}`
- **Path Variable**: `id` (Long)
- **Response**:
  - **200 OK**: 조회 성공
  - **404 Not Found**: 일기를 찾을 수 없음
- **Response Body** (성공 시):
```json
{
  "id": 1,
  "memberId": 1,
  "title": "오늘의 일기",
  "content": "오늘은 정말 좋은 하루였다. 새로운 것을 배웠고...",
  "emotion": "happy",
  "createdAt": "2024-12-15T10:30:00",
  "updatedAt": "2024-12-15T10:30:00"
}
```

#### 2.4 일기 작성
- **URL**: `POST /api/diaries`
- **Request Body**:
```json
{
  "memberId": 1,
  "title": "오늘의 일기",
  "content": "오늘은 정말 좋은 하루였다.",
  "emotion": "happy"
}
```
- **Response**:
  - **201 Created**: 작성 성공
  - **400 Bad Request**: 잘못된 요청
- **Response Body** (성공 시):
```json
"일기 작성 성공"
```

#### 2.5 일기 수정
- **URL**: `PUT /api/diaries/{id}`
- **Path Variable**: `id` (Long)
- **Request Body**:
```json
{
  "title": "수정된 일기 제목",
  "content": "수정된 내용입니다.",
  "emotion": "sad"
}
```
- **Response**:
  - **200 OK**: 수정 성공
  - **404 Not Found**: 일기를 찾을 수 없음
  - **400 Bad Request**: 잘못된 요청
- **Response Body** (성공 시):
```json
"일기 수정 성공"
```

#### 2.6 일기 삭제
- **URL**: `DELETE /api/diaries/{id}`
- **Path Variable**: `id` (Long)
- **Response**:
  - **200 OK**: 삭제 성공
  - **404 Not Found**: 일기를 찾을 수 없음
- **Response Body** (성공 시):
```json
"일기 삭제 성공"
```

---

## HTTP 상태 코드 정리

| 상태 코드 | 설명 | 사용 예시 |
|---------|------|---------|
| 200 OK | 요청 성공 | 조회, 수정, 삭제 성공 |
| 201 Created | 리소스 생성 성공 | 회원가입, 일기 작성 성공 |
| 400 Bad Request | 잘못된 요청 | 유효성 검증 실패, 로그인 실패 |
| 404 Not Found | 리소스 없음 | 존재하지 않는 일기 조회 |
| 409 Conflict | 리소스 충돌 | 이메일 중복 |
| 500 Internal Server Error | 서버 오류 | 서버 내부 오류 |

---

## 데이터베이스 스키마

### member 테이블
```sql
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### diary 테이블
```sql
CREATE TABLE diary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(500) NOT NULL,
    emotion VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);
```

---

## 실행 방법

### 1. 프로젝트 클론 및 빌드
```bash
cd board
./gradlew build
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. 서버 접속
- **API Server**: `http://localhost:8080`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:diarydb`
  - Username: `sa`
  - Password: (없음)

---

## React 연동 시 CORS 설정

개발 단계에서 React와 연동 시, React 프로젝트의 `package.json`에 다음 설정을 추가하세요:

```json
{
  "proxy": "http://localhost:8080"
}
```

이후 API 호출 시 상대 경로로 요청:
```javascript
// 예시
fetch("/api/diaries")
  .then(res => res.json())
  .then(data => console.log(data));
```

---
