# 일기장 REST API 서버

React 일기장 프로젝트에서 사용할 백엔드 REST API 서버입니다.

## 프로젝트 개요

이 프로젝트는 일기장 기능을 제공하는 **RESTful API 서버**로, 회원가입/로그인, 일기 CRUD 기능을 제공합니다. RESTful 설계 원칙에 따라 HTTP 메서드, URI 설계, 상태 코드를 적절히 사용하여 구현되었습니다.

## 사용 기술 스택

- **Java 17**
- **Spring Boot 3.4.12**
- **MyBatis 3.0.5**
- **H2 Database** (인메모리 데이터베이스)
- **Gradle**
- **Lombok**

## 주요 도메인

### Member (회원)
- 회원 정보 관리
- 회원가입 및 로그인 기능
- 회원 정보 조회, 수정, 탈퇴
- 이메일 중복 검증

### Diary (일기)
- 일기 작성, 조회, 수정, 삭제
- 감정 기록 (emotion: happy, sad, normal, fire)
- 회원별 일기 조회
- 전체 일기 목록 조회
- 감정별 필터링 기능
- 제목/내용 검색 기능

## API 명세

### 공통 응답 형식

모든 API는 다음 형식의 JSON 응답을 반환합니다:

**성공 응답:**
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": { ... }
}
```

**에러 응답:**
```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null
}
```

---

### 1. 회원 API

#### 1.1 회원가입
- **Method**: `POST`
- **URL**: `/api/members`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "홍길동"
}
```
- **Response** (201 Created):
```json
{
  "success": true,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "createdAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `201 Created`: 회원가입 성공
  - `409 Conflict`: 이메일 중복
  - `400 Bad Request`: 잘못된 요청

#### 1.2 로그인
- **Method**: `POST`
- **URL**: `/api/members/login`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "로그인에 성공했습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "createdAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `200 OK`: 로그인 성공
  - `400 Bad Request`: 이메일 또는 비밀번호 오류

#### 1.3 회원 정보 조회
- **Method**: `GET`
- **URL**: `/api/members/{id}`
- **Path Variable**: `id` (Long) - 회원 ID
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "createdAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `200 OK`: 조회 성공
  - `404 Not Found`: 회원을 찾을 수 없음

#### 1.4 회원 정보 수정
- **Method**: `PUT`
- **URL**: `/api/members/{id}`
- **Path Variable**: `id` (Long) - 회원 ID
- **Request Body**:
```json
{
  "email": "newemail@example.com",
  "password": "newpassword123",
  "nickname": "새로운 닉네임"
}
```
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "회원 정보가 수정되었습니다.",
  "data": {
    "id": 1,
    "email": "newemail@example.com",
    "nickname": "새로운 닉네임",
    "createdAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `200 OK`: 수정 성공
  - `404 Not Found`: 회원을 찾을 수 없음
  - `409 Conflict`: 이메일 중복

#### 1.5 회원 탈퇴
- **Method**: `DELETE`
- **URL**: `/api/members/{id}`
- **Path Variable**: `id` (Long) - 회원 ID
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "회원 탈퇴가 완료되었습니다.",
  "data": null
}
```
- **상태 코드**:
  - `200 OK`: 탈퇴 성공
  - `404 Not Found`: 회원을 찾을 수 없음

---

### 2. 일기 API

#### 2.1 전체 일기 목록 조회 (감정 필터링, 검색 지원)
- **Method**: `GET`
- **URL**: `/api/diaries`
- **Query Parameters** (선택):
  - `emotion` (String): 감정별 필터링 (예: `happy`, `sad`, `normal`, `fire`)
  - `keyword` (String): 검색 키워드 (제목 또는 내용에서 검색)
- **예시**:
  - 전체 조회: `GET /api/diaries`
  - 감정별 필터: `GET /api/diaries?emotion=happy`
  - 검색: `GET /api/diaries?keyword=오늘`
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": [
    {
      "id": 1,
      "title": "오늘의 일기",
      "content": "오늘은 정말 좋은 하루였다...",
      "emotion": "happy",
      "createdAt": "2024-12-15T10:30:00"
    },
    {
      "id": 2,
      "title": "즐거운 하루",
      "content": "새로운 것을 배웠다...",
      "emotion": "excited",
      "createdAt": "2024-12-14T09:20:00"
    }
  ]
}
```
- **상태 코드**:
  - `200 OK`: 조회 성공

#### 2.2 회원별 일기 목록 조회 (계층 구조, 감정 필터링, 검색 지원)
- **Method**: `GET`
- **URL**: `/api/members/{memberId}/diaries`
- **Path Variable**: `memberId` (Long) - 회원 ID
- **Query Parameters** (선택):
  - `emotion` (String): 감정별 필터링 (예: `happy`, `sad`, `normal`, `fire`)
  - `keyword` (String): 검색 키워드 (제목 또는 내용에서 검색)
- **예시**:
  - 전체 조회: `GET /api/members/1/diaries`
  - 감정별 필터: `GET /api/members/1/diaries?emotion=happy`
  - 검색: `GET /api/members/1/diaries?keyword=오늘`
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": [
    {
      "id": 1,
      "title": "오늘의 일기",
      "content": "오늘은 정말 좋은 하루였다...",
      "emotion": "happy",
      "createdAt": "2024-12-15T10:30:00"
    }
  ]
}
```
- **상태 코드**:
  - `200 OK`: 조회 성공

#### 2.3 일기 상세 조회
- **Method**: `GET`
- **URL**: `/api/diaries/{id}`
- **Path Variable**: `id` (Long) - 일기 ID
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "title": "오늘의 일기",
    "content": "오늘은 정말 좋은 하루였다. 새로운 것을 배웠고...",
    "emotion": "happy",
    "createdAt": "2024-12-15T10:30:00",
    "updatedAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `200 OK`: 조회 성공
  - `404 Not Found`: 일기를 찾을 수 없음

#### 2.4 일기 작성
- **Method**: `POST`
- **URL**: `/api/diaries`
- **Request Body**:
```json
{
  "memberId": 1,
  "title": "오늘의 일기",
  "content": "오늘은 정말 좋은 하루였다.",
  "emotion": "happy"
}
```
- **Response** (201 Created):
```json
{
  "success": true,
  "message": "일기가 작성되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "title": "오늘의 일기",
    "content": "오늘은 정말 좋은 하루였다.",
    "emotion": "happy",
    "createdAt": "2024-12-15T10:30:00",
    "updatedAt": "2024-12-15T10:30:00"
  }
}
```
- **상태 코드**:
  - `201 Created`: 작성 성공
  - `400 Bad Request`: 잘못된 요청 (제목, 회원 ID 필수)

#### 2.5 일기 수정
- **Method**: `PUT`
- **URL**: `/api/diaries/{id}`
- **Path Variable**: `id` (Long) - 일기 ID
- **Request Body**:
```json
{
  "title": "수정된 일기 제목",
  "content": "수정된 내용입니다.",
  "emotion": "sad"
}
```
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "일기가 수정되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "title": "수정된 일기 제목",
    "content": "수정된 내용입니다.",
    "emotion": "sad",
    "createdAt": "2024-12-15T10:30:00",
    "updatedAt": "2024-12-15T11:00:00"
  }
}
```
- **상태 코드**:
  - `200 OK`: 수정 성공
  - `404 Not Found`: 일기를 찾을 수 없음
  - `400 Bad Request`: 잘못된 요청

#### 2.6 일기 삭제
- **Method**: `DELETE`
- **URL**: `/api/diaries/{id}`
- **Path Variable**: `id` (Long) - 일기 ID
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "일기가 삭제되었습니다.",
  "data": null
}
```
- **상태 코드**:
  - `200 OK`: 삭제 성공
  - `404 Not Found`: 일기를 찾을 수 없음

---

## RESTful 설계 원칙 적용

### 1. URI 설계
- ✅ 리소스는 복수형 명사 사용: `/api/members`, `/api/diaries`
- ✅ 동사 대신 HTTP 메서드로 행위 표현: `POST /api/members` (회원가입)
- ✅ 계층 구조 표현: `/api/members/{memberId}/diaries` (회원별 일기 조회)

### 2. HTTP 메서드 사용
- `GET`: 조회 (일기 목록, 상세 조회, 회원 정보 조회)
- `POST`: 생성 (회원가입, 일기 작성, 로그인)
- `PUT`: 전체 수정 (일기 수정, 회원 정보 수정)
- `DELETE`: 삭제 (일기 삭제, 회원 탈퇴)

### 3. HTTP 상태 코드 설계
| 상태 코드 | 설명 | 사용 예시 |
|---------|------|---------|
| 200 OK | 요청 성공 | 조회, 수정, 삭제 성공, 로그인 성공 |
| 201 Created | 리소스 생성 성공 | 회원가입, 일기 작성 성공 |
| 400 Bad Request | 잘못된 요청 | 유효성 검증 실패, 로그인 실패 |
| 404 Not Found | 리소스 없음 | 존재하지 않는 일기 조회/수정/삭제 |
| 409 Conflict | 리소스 충돌 | 이메일 중복 |
| 500 Internal Server Error | 서버 오류 | 서버 내부 오류 |

---

## 데이터베이스 스키마

### member 테이블
```sql
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### diary 테이블
```sql
CREATE TABLE IF NOT EXISTS diary (
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

## 프로젝트 구조

```
src/main/java/com/kh/board/
├── BoardApplication.java          # 메인 애플리케이션
├── config/
│   └── WebConfig.java             # CORS 설정
├── controller/
│   ├── MemberController.java      # 회원 API 컨트롤러
│   ├── DiaryController.java       # 일기 API 컨트롤러
│   ├── MemberDiaryController.java # 회원별 일기 API 컨트롤러
│   └── dto/
│       ├── request/               # 요청 DTO
│       │   ├── MemberRequest.java
│       │   └── DiaryRequest.java
│       └── response/              # 응답 DTO
│           ├── ApiResponse.java   # 공통 응답 형식
│           ├── MemberResponse.java
│           └── DiaryResponse.java
├── entity/                        # 엔티티
│   ├── Member.java
│   └── Diary.java
├── mapper/                        # MyBatis 매퍼 인터페이스
│   ├── MemberMapper.java
│   └── DiaryMapper.java
└── service/                       # 비즈니스 로직
    ├── MemberService.java
    ├── MemberServiceImpl.java
    ├── DiaryService.java
    └── DiaryServiceImpl.java
```
