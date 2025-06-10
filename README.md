## 단축 URL 기반 실시간 인기 순위 플랫폼
### 목표 : API 개발, 프로그램의 필수적인 기능 완성

### <span style = 'color : #FFCD28'>요구 사항</span><br/>

- [ ] 사용자가 입력한 URL을 조건에 따라 단축 URL로 변환할 수 있어야 한다.<br/><br/>
- [ ] 단축된 URL을 복사할 수 있는 기능을 제공한다.<br/><br/>
- [ ] 사용자가 변환한 URL 목록을 확인할 수 있어야 한다.<br/><br/>
- [ ] 각 URL에는 [방문 기록 추적] 기능이 포함되어야 한다.<br/><br/>
- [ ] [방문 기록 추적] 기능에서는 해당 URL의 총 조회 수와 유입 경로를 확인할 수 있어야 한다.<br/><br/>
- [ ] 실시간으로 가장 많이 클릭된 URL Top 10을 확인할 수 있어야 한다.br/><br/>
- [ ] 해당 순위는 국가별(예: 한국, 미국, 일본)로 필터링이 가능하다.<br/><br/>
- [ ] 연령대별(10대~60대) 인기 순위 확인도 지원한다.<br/><br/>
- [ ] 일정 조회 수를 초과한 단축 URL은 클릭 시 광고 배너를 노출한다.<br/><br/>
- [ ] 노출되는 광고는 해당 URL의 주제와 관련 있는 콘텐츠로 매칭된다.<br/><br/>

### <span style = 'color : #FFCD28'>API 구성</span><br/>

#### *<span style = 'color : #A8F552'>@PostMapping("/user")</span>*<br/>

🔆 회원가입을 처리한다.<br/>
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

