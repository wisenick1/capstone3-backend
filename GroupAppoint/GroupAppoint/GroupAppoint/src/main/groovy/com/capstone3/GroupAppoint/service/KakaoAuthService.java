package com.capstone3.GroupAppoint.service;

@Service
public class KakaoAuthService {

    private final RestTemplate restTemplate;

    public KakaoAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //인증 코드로 액세스 토큰을 요청함수
    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "YOUR_KAKAO_REST_API_KEY");
        params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 카카오 서버에 POST 요청하여 액세스 토큰을 받기
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        // 응답에서 액세스 토큰 추출
        return parseAccessToken(response.getBody());
    }

    //액세스 토큰으로 사용자 정보를 요청함수
    public Map<String, Object> getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // 카카오 서버에 GET 요청하여 사용자 정보를 받음
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        
        // 응답에서 사용자 정보 추출
        return parseUserInfo(response.getBody());
    }

    //응답에서 액세스 토큰을 파싱함수 (실제 응답 형식에 맞게 구현) -  미구현
    private String parseAccessToken(String responseBody) {
        // JSON 라이브러리를 사용해 응답 본문에서 액세스 토큰을 파싱 (예: Jackson, Gson)
        // 실제로는 JSON에서 access_token을 추출하는 코드가 필요
        return "parsed_access_token";  // 응답에서 추출한 토큰을 반환
    }

    //응답에서 사용자 정보를 파싱함수 (실제 응답 형식에 맞게 구현) - 미구현
    private Map<String, Object> parseUserInfo(String responseBody) {
        // JSON 라이브러리를 사용해 응답 본문에서 사용자 정보를 파싱
        Map<String, Object> userInfo = new HashMap<>();
        // 실제로는 JSON에서 필요한 사용자 정보를 추출하는 코드가 필요
        userInfo.put("id", "parsed_user_id");
        userInfo.put("nickname", "parsed_nickname");
        return userInfo;
    }

    
}

