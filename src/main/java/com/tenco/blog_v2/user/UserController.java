package com.tenco.blog_v2.user;

import com.tenco.blog_v2.common.errors.Exception401;
import com.tenco.blog_v2.common.errors.Exception403;
import com.tenco.blog_v2.common.utils.ApiUtil;
import com.tenco.blog_v2.common.utils.Define;
import com.tenco.blog_v2.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {

    // DI 처리
    private final UserService userService;
    private final HttpSession session;

    /**
     * 회원 정보 수정
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @RequestBody UserRequest.UpdateDTO reqDTO, HttpServletRequest request) {

        // 헤더에 있는 JWT 토큰을 가져오기
        // 토큰에서 사용자 정보 추출
        // 사용자 정보 수정 로직을 그대로 사용

        String authorizationHeader = request.getHeader(Define.AUTHORIZATION);

        // null이거나 Bearer로 시작하지 않는다면
        if(authorizationHeader == null || !authorizationHeader.startsWith(Define.BEARER)){
            throw new Exception401("인증 정보가 유효하지 않습니다.");
        }

        //Bearer 한 칸 공백을 제거함
        String token = authorizationHeader.replace(Define.BEARER, "");
        User sessionUser = JwtUtil.verify(token); // user 객체를 생성함

        if(sessionUser == null){
            throw new Exception401("인증 토큰이 유효하지 않습니다.");
        }

        // id를 5라고 던졌을 때 변경하면 안 되므로 방어적 코드 작성
        if(sessionUser.getId() != id){
            throw new Exception403("해당 사용자를 수정할 권한이 없습니다.");
        }

        // 서비스에 사용자 정보 수정 요청
        UserResponse.DTO resDTO = userService.updateUser(id, reqDTO); // pk 값 id와 넘겨받은 reqDTO를 담아야 함.

        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }


//    @ResponseBody // 데이터 반환
    @PostMapping("/join")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> join(UserRequest.JoinDTO reqDTO)  {

        System.out.println("111111111111111");
        // 회원 가입 서비스는 --> 서비스 객체에게 위임한다.
        UserResponse.DTO resDTO = userService.signUp(reqDTO);
        System.out.println("2222222222222222");
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    /**
     * 자원에 요청은 GET 방식이지만 보안에 이유로 예외 !
     * 로그인 처리 메서드
     * 요청 주소 POST : http://localhost:8080/login
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> login(@RequestBody UserRequest.LoginDTO reqDto) {

        // @RequestBody 사용 이유 <-- body에 있는 {"id" = 1 , "name" = "길동"} 이 데이터를 받아 와야 함.

        // 사용자가 던진 값 받아서 DB에 사용자 정보 있는지 확인
        String jwt = userService.signIn(reqDto);

        return ResponseEntity.ok()
                .header(Define.AUTHORIZATION, Define.BEARER + jwt)
                .body(new ApiUtil<>(null));
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 세션을 무효화 (로그아웃)
        return "redirect:/";
    }
}