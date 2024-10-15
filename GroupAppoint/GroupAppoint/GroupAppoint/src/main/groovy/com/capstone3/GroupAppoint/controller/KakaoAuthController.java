@RestController
@RequestMapping("/oauth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        // 액세스 토큰 요청 및 유저 정보 가져오기
        String accessToken = kakaoAuthService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoAuthService.getUserInfo(accessToken);
        
        // 성공할시 메시지
        return ResponseEntity.ok("카카오 로그인 성공: " + userInfo);
    }
}
