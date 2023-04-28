## [항해] 제출 과제 3  
## with Gengar
https://github.com/gengar0917  

1. 회원 가입 API
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글, 댓글 수정 / 삭제 가능
    - 참고자료
        1. [https://mangkyu.tistory.com/174](https://mangkyu.tistory.com/174)
        2. [https://ko.wikipedia.org/wiki/정규_표현식](https://ko.wikipedia.org/wiki/%EC%A0%95%EA%B7%9C_%ED%91%9C%ED%98%84%EC%8B%9D)
        3. [https://bamdule.tistory.com/35](https://bamdule.tistory.com/35)
        
2. 로그인 API
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
    발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
3. 댓글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
    - 선택한 게시글의 DB 저장 유무를 확인하기
    - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
4. 댓글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
5. 댓글 삭제 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
6. 예외 처리
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기
-------------------------------------------------------------------------------
요구 사항  
1. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기
    - 각각의 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
2. 게시글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
    - 제목, 작성자명(username), 작성 내용을 저장하고
    - 저장된 게시글을 Client 로 반환하기
3. 선택한 게시글 조회 API
    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기 
    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    - 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
4. 선택한 게시글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
5. 선택한 게시글 삭제 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
  
## ERD 설계
  
![erd설계](https://user-images.githubusercontent.com/122955367/233663415-a3ee3b65-3a73-4138-93dc-aa166f51bc0f.png)  
## API 명세서 
  
https://documenter.getpostman.com/view/26921017/2s93Y3vMEp#4b2717f9-46ae-4dee-92a9-2a5af806abef  
## 생각해보기

1. JWT를 사용하여 인증/인가를 구현 했을 때의 장점은 무엇일까요?  
- 동시 접속자가 많을 때 서버측 부하를 낮출수 있다. (트래픽 부담이 낮다)
- client, server가 다른 도메인을 사용할 때

2. 반대로 JWT를 사용한 인증/인가의 한계점은 무엇일까요?  
- 구현의 복잡도가 증가
- JWT 에 담는 내용이 커질 수록 네트워크 비용 증가
- 기존에 생성된 JWT 를 일부만 만료시킬 수가 없다. 
- Secret Key 유출 시 시스템 전체가 위험에 노출

3. 만약 댓글 기능이 있는 블로그에서 댓글이 달려있는 게시글을 삭제하려고 한다면 무슨 문제가 발생할까요? Database 테이블 관점에서 해결방법이 무엇일까요?  
- 댓글과 게시글 사이의 관계 설정이 필요
- 게시글과 댓글 테이블을 게시글 기준 id 로 join 하여 관계를 설정 

4. IoC / DI 에 대해 간략하게 설명해 주세요!  
- DI (Dependency Injection) : 의존성 주입. 외부에서 객체 생성하여 주입시키는 방식. 모듈간의 결합도가 낮아지고 유연성이 높아진다
- IoC (Injection of Control) : 제어의 역전. 제어의 흐름을 바꾼다. 메소드나 객체의 포출 작업을 외부에서 결정되는 것을 의미, 객체간의 결합도를 낮추고, 유연한 코드 작성 
