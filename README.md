## 단축 URL 기반 사용자 맞춤 통계 제공 서비스

### <span style = 'color : #FFCD28'>요구 사항</span><br/>

- [X] 사용자가 입력한 URL을 조건에 따라 단축 URL로 변환할 수 있어야 한다.<br/><br/>
- [X] 사용자가 변환한 URL 목록을 확인할 수 있어야 한다.<br/><br/>
- [X] 각 URL에는 [방문 기록 추적] 기능이 포함되어야 한다.<br/><br/>
- [X] 사용자는 최근 7일간의 방문 트렌드를 그래프로 확인할 수 있다.<br/><br/>
- [X] 기간 필터를 통해 원하는 날짜 범위를 설정할 수 있어야 한다.<br/><br/>
- [X] 유입 경로(referrer)별 방문자 수를 실시간 그래프로 시각화한다.<br/><br/>
- [X] 사용자 디바이스(PC, 모바일 등)별 방문자 수를 실시간 그래프로 시각화한다.<br/><br/>


### <span style = 'color : #FFCD28'>API 구성</span><br/>

#### *<span style = 'color : #A8F552'>@PostMapping("/user")</span>*<br/>

🔆 회원가입을 처리한다.<br/>
🔆 입력값이 유효하지 않을 경우 예외를 발생시킨다.<br/>

#### *<span style = 'color : #A8F552'>@PostMapping("/user/login")</span>*<br/>

🔆 사용자의 로그인 요청을 처리한다.<br/>
🔆 입력값이 유효하지 않을 경우 예외를 발생시킨다.<br/>

#### *<span style = 'color : #A8F552'>@PatchMapping("/user/{id}")</span>*<br/>

🔆 사용자의 비밀번호를 변경한다.<br/>

#### *<span style = 'color : #A8F552'>@PostMapping("/users/{userId}/urls")</span>*<br/>

🔆 사용자가 요청한 OriginalUrl로 단축 URL을 생성한다.<br/>
🔆 동일한 Original URL이 이미 존재할 경우, 기존 단축 URL을 반환한다.<br/>
🔆 입력값이 유효하지 않을 경우 예외를 발생시킨다.<br/>

#### *<span style = 'color : #A8F552'>@GetMapping("/users/{userId}/urls")</span>*<br/>

🔆 역할 : 사용자의 단축 URL 목록을 조회한다.<br/>

#### *<span style = 'color : #A8F552'>@GetMapping("/{short-url}")</span>*<br/>

🔆 단축 URL(shortUrl)로 요청 시 원본 URL(originalUrl)로 리다이렉트한다.<br/>
🔆 존재하지 않는 경우 예외를 발생시킨다.<br/>

#### *<span style = 'color : #A8F552'>@PatchMapping("/user-urls/{userUrlId}")</span>*<br/>

🔆 사용자가 단축 URL의 제목(title)을 수정한다.<br/>
🔆 입력값이 없을 경우 예외를 발생시킨다.<br/>

#### *<span style = 'color : #A8F552'>@DeleteMapping("/user-urls/{userUrlId}")</span>*<br/>

🔆 사용자의 단축 URL을 삭제한다.<br/>

