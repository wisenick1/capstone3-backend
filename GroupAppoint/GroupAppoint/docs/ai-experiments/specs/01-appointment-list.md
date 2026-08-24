# Appointment List API

## Goal

약속 목록을 조회할 수 있다.

## Endpoint

GET /api/v1/appointments

## Query Parameter

status: optional

가능한 값:

- UPCOMING
- IN_PROGRESS
- COMPLETED
- CANCELLED

Example:

GET /api/v1/appointments?status=UPCOMING

## Response

[
{
"id": 1,
"title": "강남역 저녁 약속",
"scheduledAt": "2026-08-30T19:00:00",
"status": "UPCOMING",
"location": {
"name": "강남역",
"address": "서울특별시 강남구",
"latitude": 37.498,
"longitude": 127.027
},
"participantCount": 2,
"participants": [
{
"userId": 1,
"profileImageUrl": "https://example.com/profile1.jpg"
}
]
}
]

## Acceptance Criteria

### AC1
`GET /api/v1/appointments` 요청 시 약속 목록과 HTTP 200을 반환한다.

### AC2
`status`가 없으면 모든 상태의 약속을 조회한다.

### AC3
`status`가 주어지면 해당 상태의 약속만 조회한다.

### AC4
조회 결과는 `scheduledAt` 기준 오름차순으로 정렬한다.

### AC5
존재하지 않는 `status` 값이 주어지면 HTTP 400을 반환한다.

### AC6
Controller에서 Appointment Entity를 직접 반환하지 않고 Response DTO를 사용한다.