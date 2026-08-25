# Experiment 01 — Claude vs Codex

## 1. 실험 목적

이번 실험의 목적은 Claude와 Codex 중 누가 더 좋은 모델인지 승부를 내는 것이 아니다.

같은 Spring Boot 프로젝트, 같은 코드 상태, 같은 요구사항을 제공했을 때 두 AI Coding Agent가 다음을 어떻게 다르게 처리하는지 관찰하는 것이 목적이다.

* 기존 프로젝트 탐색
* 요구사항 해석
* 설계
* 구현
* 테스트 작성
* 오류 해결
* 변경 범위
* 명세에 없는 부분에 대한 판단

장기적으로는 이런 경험을 통해:

> 어떤 작업을 어떤 Agent에게 맡기는 것이 좋은가?

를 판단할 수 있는 감각을 만드는 것이 목표다.

---

# 2. 실험 프로젝트

예전에 만들었던 **실시간 위치 기반 약속 관리 플랫폼**을 다시 활용했다.

기존 코드가 거의 남아 있지 않아 이번 실험에서는 사실상 최소 Spring Boot 프로젝트에 가까운 상태에서 시작했다.

기술 스택은 Agent들이 프로젝트 탐색을 통해 파악했다.

```text
Spring Boot 3.3.4
Groovy
Java 17
Spring Web
Spring Data JPA
Validation
H2
JUnit 5
Gradle
```

이 프로젝트는 앞으로도 유지하면서 기능을 하나씩 추가하며 Claude/Codex 실험에 사용한다.

---

# 3. 첫 번째 기능

첫 실험 기능은 **약속 목록 조회 API**로 선정했다.

처음부터 실시간 위치, WebSocket, Redis 등 복잡한 기능을 넣으면 Agent 자체의 차이보다 기술 복잡도가 실험에 영향을 줄 수 있기 때문에 단순한 조회 기능부터 시작했다.

API:

```http
GET /api/v1/appointments
```

상태 필터:

```http
GET /api/v1/appointments?status=UPCOMING
```

상태:

```text
UPCOMING
IN_PROGRESS
COMPLETED
CANCELLED
```

---

# 4. API Spec 작성

두 Agent에게 동일한 요구사항을 제공하기 위해 다음 문서를 작성했다.

```text
docs/
└── ai-experiments/
    └── specs/
        └── 01-appointment-list.md
```

이 파일에는:

```text
Goal
Endpoint
Query Parameter
Response Example
Acceptance Criteria
```

를 작성했다.

즉 이 파일이 두 AI에게 주는 **동일한 시험 문제** 역할을 한다.

이 문서 역시 Git에 포함시켰다.

---

# 5. Acceptance Criteria

첫 실험의 AC는 다음과 같이 정했다.

```text
AC1
GET /api/v1/appointments 요청 시
약속 목록과 HTTP 200을 반환한다.

AC2
status가 없으면 모든 상태의 약속을 조회한다.

AC3
status가 주어지면 해당 상태의 약속만 조회한다.

AC4
조회 결과는 scheduledAt 기준 오름차순으로 정렬한다.

AC5
존재하지 않는 status 값이 주어지면
HTTP 400을 반환한다.

AC6
Controller에서 Appointment Entity를 직접 반환하지 않고
Response DTO를 사용한다.
```

이번 실험에서는 pagination, 인증, 사용자별 권한 등은 제외했다.

---

# 6. 동일한 출발점 만들기

Claude와 Codex가 정확히 같은 프로젝트 상태에서 출발할 수 있도록 baseline을 만들었다.

```bash
git add .
git commit -m "chore: prepare appointment list experiment baseline"

git branch baseline/week1
```

구조:

```text
              baseline/week1
                    │
         ┌──────────┴──────────┐
         │                     │
       Claude                Codex
```

---

# 7. Git Worktree 사용

각 Agent가 서로의 결과에 영향을 받지 않도록 Git Worktree를 사용했다.

Worktree는:

> 같은 Git Repository의 서로 다른 branch를 별도의 실제 디렉토리에서 동시에 작업할 수 있게 해주는 기능

이다.

구조:

```text
appointment/
→ main

appointment-claude/
→ experiment/week1-claude

appointment-codex/
→ experiment/week1-codex
```

두 실험 branch 모두:

```text
baseline/week1
```

커밋에서 시작했다.

즉:

```text
같은 코드
+
같은 명세
+
다른 Agent
```

환경을 만든 것이다.

---

# 8. 두 Agent에게 동일한 프롬프트 제공

Claude와 Codex에게 같은 프롬프트를 제공했다.

핵심 요구사항은:

```text
1. 프로젝트 구조 탐색
2. 구현 계획 작성
3. AC에 맞게 구현
4. 자동화 테스트 작성
5. 테스트 실행 및 실패 시 수정
6. 변경 파일 / 설계 / 테스트 / 가정 정리
```

제약:

```text
명세 수정 금지
관련 없는 기능 변경 금지
불필요한 대규모 리팩터링 금지
기존 convention이 있으면 준수
```

특히 구현 방법은 지정하지 않았다.

예를 들어:

```text
Location은 @Embeddable로 만들어라
Controller-Service-Repository로 만들어라
```

같은 지시는 주지 않았다.

이를 통해 Agent가 스스로 어떤 설계를 선택하는지 관찰했다.

---

# 9. Claude 결과

Claude는 다음 구조를 만들었다.

```text
appointment/
├── controller/
│   ├── AppointmentController
│   └── AppointmentExceptionHandler
│
├── service/
│   └── AppointmentService
│
├── repository/
│   └── AppointmentRepository
│
├── domain/
│   ├── Appointment
│   ├── AppointmentStatus
│   ├── Location
│   └── Participant
│
└── dto/
    ├── AppointmentResponse
    ├── LocationResponse
    └── ParticipantResponse
```

추가로:

```text
application.yml 수정
AppointmentControllerTest 추가
```

했다.

### Claude의 주요 설계 판단

Location:

```text
@Embeddable value object
```

Participant:

```text
별도 Entity
Appointment와 1:N
```

정렬:

```text
Repository query에서 scheduledAt ASC
```

조회:

```text
@Transactional(readOnly = true)
```

응답:

```text
Entity → Response DTO
```

잘못된 status:

```text
MethodArgumentTypeMismatchException
→ RestControllerAdvice
→ HTTP 400 + 별도 JSON error response
```

---

# 10. Claude 테스트

Claude는 MockMvc + 실제 JPA/H2를 이용한 통합 테스트를 작성했다.

검증한 내용:

```text
전체 조회
status 조회
scheduledAt 정렬
잘못된 status → 400
location / participants 응답 구조
```

결과:

```text
신규 5개 테스트
기존 context test 1개

총 6개
6 PASS
```

---

# 11. Codex 결과

Codex는 조금 다른 구조를 선택했다.

```text
appointment/
├── web/
│   ├── AppointmentController
│   └── AppointmentResponse
│
├── application/
│   └── AppointmentQueryService
│
└── domain/
    ├── Appointment
    ├── AppointmentStatus
    ├── AppointmentLocation
    ├── AppointmentParticipant
    └── AppointmentRepository
```

총:

```text
9 files changed
329 insertions
```

이었다.

`application.yml` 등의 기존 설정 파일은 수정하지 않았다.

---

# 12. Codex의 주요 설계 판단

Location:

```text
AppointmentLocation
@Embeddable value object
```

Participant:

```text
AppointmentParticipant
@OneToMany
```

Repository:

```text
domain package 내부
```

Service:

```text
AppointmentService가 아니라
AppointmentQueryService
```

로 명명했다.

정렬:

```text
Repository 단계에서 scheduledAt ASC
```

응답:

```text
read-only transaction 안에서
Entity → Response DTO
```

invalid status:

```text
Spring MVC enum parameter conversion 실패
→ 기본 HTTP 400 동작 활용
```

별도의 ExceptionHandler는 만들지 않았다.

---

# 13. Codex 테스트

Codex 역시 MockMvc + JPA + H2를 사용한 통합 테스트를 만들었다.

총 4개의 신규 테스트에서:

```text
전체 조회 + scheduledAt 정렬
UPCOMING 필터
response DTO 구조
invalid status → 400
```

을 확인했다.

기존 테스트 포함:

```text
총 5개
5 PASS
```

---

# 14. Codex의 흥미로운 오류 해결

Codex는 첫 테스트 실행에서 오류가 발생했다.

Groovy Controller parameter의 reflection metadata 문제로 요청 parameter가 정상적으로 처리되지 않았다.

Codex가 이를 분석해:

```groovy
@RequestParam(name = 'status')
```

처럼 parameter name을 명시적으로 지정했다.

그 뒤:

```text
gradlew test
→ BUILD SUCCESSFUL
```

까지 다시 확인했다.

즉 단순 코드 생성뿐 아니라:

```text
구현
→ 실제 테스트 실패
→ 원인 파악
→ 코드 수정
→ 재검증
```

과정을 스스로 수행했다.

---

# 15. AC 결과 비교

| Acceptance Criteria  | Claude | Codex |
| -------------------- | -----: | ----: |
| 목록 조회 + HTTP 200     |      ✅ |     ✅ |
| status 없음 → 전체 조회    |      ✅ |     ✅ |
| status 필터            |      ✅ |     ✅ |
| scheduledAt 오름차순     |      ✅ |     ✅ |
| invalid status → 400 |      ✅ |     ✅ |
| Response DTO 사용      |      ✅ |     ✅ |

**기능 정확도에서는 큰 차이가 없었다.**

두 Agent 모두 모든 AC를 만족했다.

---

# 16. 두 Agent의 공통점

흥미롭게도 독립적으로 실행했음에도 핵심 설계는 상당히 비슷했다.

둘 다:

```text
AppointmentStatus → Enum

Location → @Embeddable

Participant → Appointment와 1:N

정렬 → DB query에서 수행

조회 → readOnly Transaction

Entity 직접 반환 X
→ Response DTO

테스트
→ MockMvc + JPA + H2
```

를 선택했다.

따라서 첫 실험에서:

> 작은 Spring/JPA 조회 기능에서는 두 모델 모두 비교적 전형적인 설계에 수렴했다.

는 결과를 얻었다.

---

# 17. 가장 큰 차이 — 변경 범위

Claude가 상대적으로 더 많은 구조를 만들었다.

Claude:

```text
controller
service
repository
domain
dto

별도 ExceptionHandler
별도 DTO 3개
application.yml 수정
```

Codex:

```text
web
application
domain

Response 하나
Spring 기본 exception 처리 활용
설정 파일 수정 없음
```

따라서 첫 관찰은:

### Claude

> 요구사항 주변까지 적극적으로 완성하려는 경향

### Codex

> 요구사항을 충족하는 데 필요한 변경에 조금 더 집중하는 경향

이었다.

---

# 18. Claude에서 좋았던 점

* 기존 프로젝트가 Groovy임을 발견하고 Groovy를 사용했다.
* 모든 AC를 충족했다.
* 익숙한 Layered Architecture를 만들었다.
* Controller / Service / Repository / DTO의 책임이 명확하다.
* 오류 응답까지 고려했다.
* 테스트 범위가 충분했다.
* H2/JPA 실행 환경까지 명시적으로 구성했다.

---

# 19. Claude에서 아쉬웠던 점

조회 API 하나에 비해 구조가 조금 커졌다.

예를 들어 명세는 단순히:

```text
invalid status → HTTP 400
```

만 요구했는데 Claude는:

```text
ExceptionHandler
+
별도의 error JSON format
```

까지 만들었다.

또한:

```text
application.yml
```

도 수정했다.

따라서:

> 도움이 되는 추가 작업이지만, Agent에게 최소 변경을 기대하는 상황이라면 과도한 변경이 될 가능성이 있다.

는 점을 확인했다.

---

# 20. Codex에서 좋았던 점

* 모든 AC를 충족했다.
* 상대적으로 변경 파일 수가 적었다.
* 기존 설정 파일을 수정하지 않았다.
* Spring의 기본 enum conversion 400 처리를 활용했다.
* `AppointmentQueryService`처럼 역할이 명확한 이름을 사용했다.
* `AppointmentLocation`, `AppointmentParticipant`처럼 도메인 범위가 명확한 이름을 사용했다.
* `web / application / domain` 구조로 책임을 구분했다.
* 테스트 실패 후 실제 원인을 찾아 수정하고 재검증했다.

특히:

```text
AppointmentService
```

대신:

```text
AppointmentQueryService
```

를 만든 것은 조회 책임을 명확히 표현한다는 점에서 흥미로웠다.

---

# 21. Codex에서 아쉬운 점 / 추가 관찰이 필요한 점

Codex의 구조가 무조건 더 좋은 것은 아니다.

예를 들어:

```text
domain/AppointmentRepository
```

구조가 현재 프로젝트 전체 convention과 잘 맞을지는 아직 판단할 수 없다.

또한:

```text
AppointmentResponse
```

하나에 nested response 구조를 넣었다면 규모가 커졌을 때 DTO 분리가 필요할 수도 있다.

따라서 첫 실험만으로:

> Codex 구조가 Claude보다 우수하다.

라고 판단할 수는 없다.

---

# 22. 테스트 품질 비교

표면적으로는:

```text
Claude: 5개
Codex: 4개
```

였지만 실제 검증 범위는 거의 동일했다.

Claude:

```text
전체 조회
필터
정렬
invalid status
응답 구조
```

Codex:

```text
전체 조회 + 정렬
필터
invalid status
응답 구조
```

즉:

> 테스트 개수 차이는 있었지만 실제 테스트 커버리지에는 큰 차이가 없었다.

이번 실험에서는 테스트 품질에서도 명확한 우열은 발견하지 못했다.

---

# 23. 이번 실험에서 가장 중요한 발견

오히려 Claude vs Codex보다 이게 더 중요한 학습이었다.

우리가 API 명세에는:

```json
"location": { ... },
"participants": [ ... ]
```

라는 응답 형식만 작성했다.

그런데 두 Agent 모두 알아서:

```text
Location persistence model
Participant Entity
Appointment와 관계
Cascade / 관계 설정
```

등을 결정했다.

즉:

> **명세에 적지 않은 부분은 Agent가 알아서 추론하고 설계한다.**

이번에는 두 모델 모두 합리적인 선택을 했지만 실제 기존 서비스에서는 위험할 수 있다.

예를 들어 원래:

```text
Member
AppointmentMember
```

라는 기존 모델이 있는데 Agent가:

```text
Participant
```

라는 새로운 Entity를 생성한다면 문제가 생길 수 있다.

---

# 24. 그래서 앞으로 명세가 달라져야 한다

단순히:

```text
무엇을 구현해야 하는가
```

뿐 아니라:

```text
Agent가 결정해도 되는 것

Agent가 변경하면 안 되는 것
```

도 명시할 필요가 있다.

예:

```text
Constraints

- 기존 데이터 모델을 변경하지 않는다.
- 새로운 Entity를 임의로 생성하지 않는다.
- 기존 AppointmentMember를 사용한다.
- API Response DTO 추가는 허용한다.
- DB Migration 변경이 필요하다면 구현 전 이유를 설명한다.
```

이런 제약이 Agent를 제어하는 **Harness**의 일부가 된다.

---

# 25. 이번 실험의 최종 결론

이번 실험만 놓고 보면 기능 정확도에는 큰 차이가 없었다.

### Claude

```text
요구사항
→ 주변 구조까지 살펴봄
→ 좀 더 완성된 Spring 구조를 적극적으로 구축
```

### Codex

```text
요구사항
→ 필요한 범위에 집중
→ 기존 framework 기능을 활용
→ 상대적으로 작은 변경
```

정도로 성향 차이가 나타났다.

하지만 Experiment 01 한 번으로:

```text
Claude는 항상 이렇다
Codex는 항상 저렇다
```

라고 일반화하면 안 된다.

앞으로 다른 종류의 작업을 반복해보며 패턴인지 확인해야 한다.

---

# 26. 현재까지 내가 얻은 Claude/Codex 사용 가설

아직 **가설**이다.

### Claude가 유리할 가능성이 있는 작업

```text
새 기능을 처음 설계할 때
기존 코드 구조를 탐색해야 할 때
설계 선택지를 설명받고 싶을 때
기능 주변까지 전체적으로 검토하고 싶을 때
```

### Codex가 유리할 가능성이 있는 작업

```text
이미 요구사항이 명확한 구현
변경 범위를 작게 유지해야 하는 수정
특정 버그 수정
테스트 실행 → 오류 원인 탐색 → 수정
```

이 가설은 앞으로 Experiment 02, 03에서 계속 검증한다.

---

# 27. 그리고 Worktree도 이번에 배운 것

이번 실험에서 Git Worktree도 처음 사용했다.

개념:

```text
하나의 Git Repository
             │
      같은 baseline
             │
      ┌──────┴──────┐
      ↓             ↓
Claude branch     Codex branch
Claude folder     Codex folder
```

이를 통해 AI Coding Agent들을 **독립된 환경에서 동시에 실험**할 수 있었다.

앞으로 여러 Agent를 비교하거나 병렬로 작업시킬 때 활용할 수 있다.

---

# 28. Experiment 01에서 배운 핵심을 아주 압축하면

이번 실험에서 진짜 얻은 건 이 5개야.

**① 동일한 요구사항이면 Claude와 Codex 모두 작은 Spring 기능은 잘 구현했다.**

**② Claude는 주변 구조까지 적극적으로 추가했고, Codex는 조금 더 최소 변경에 가까웠다.**

**③ 테스트 개수보다는 실제 무엇을 검증하는지가 중요했다.**

**④ API 명세에 없는 설계 결정은 AI가 알아서 채운다.**

**⑤ 따라서 AI Coding Agent를 잘 쓰려면 프롬프트를 잘 쓰는 것을 넘어서, AC·변경 가능 범위·금지 사항·테스트 같은 실행 환경을 설계해야 한다.**

특히 **⑤가 이번 1주차에서 가장 중요한 학습**이라고 봐. 이게 이후 하네스 엔지니어링을 배울 때도 연결되는 지점이야.

---

## 다음 실험은 이렇게 이어가면 좋아

지금 바로 새 기능을 크게 만들 필요 없어.

현재 프로젝트에 **pagination을 추가하는 Experiment 02**가 딱 좋다.

```http
GET /api/v1/appointments?page=0&size=20
```

이번에는 Experiment 01에서 한 단계 발전해서:

```text
1. 네가 요구사항/AC 작성
2. Claude/Codex에게 기존 구현을 각각 확장하게 함
3. 이번에는 Agent가 만든 테스트 외에
   네가 생각한 edge case도 따로 검증
4. 기존 코드 수정 방식 비교
```

를 해보면 돼.

Experiment 01은 **"아무것도 없는 곳에서 새 기능 만들기"**였다면, Experiment 02부터는 훨씬 현실적인:

> **이미 존재하는 코드를 AI가 얼마나 잘 이해하고 안전하게 수정하는가**

를 볼 수 있게 된다.
