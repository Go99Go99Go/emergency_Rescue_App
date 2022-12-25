# emergency_rescue_app

### 프로젝트 설명

설치된 센서들의 값을 받아 응급상황발생시 구조문자를 전송하는 어플

### 개발내용
1.라즈베리파이에 온도/습도 센서, 불꽃 감지 센서, 초음파 센서를 연결
2.1초마다 센서의 값을 측정해서 실시간 클라우드 호스팅 DB에 동기화
3.실시간 클라우드 호스팅 DB에 동기화 된 센서들의 값을 애플리케이션으로 확인하고 감지
4.스마트폰의 자이로센서를 통해서 급격한 기울기 변화 시 낙상으로 감지하고, 119에 구조 문자 발송
5.애플리케이션 코딩으로 센서들의 값이 변화하거나 일정 수준 값을 넘으면 애플리케이션으로 알림을 출력 후 119에 구조 문자 발송

### 시스템 구성도
<img src = "https://user-images.githubusercontent.com/84118571/209464180-a4888b0b-176f-4f56-b5a4-f5e4a8718c45.png" width="50%" height="50%">
<img src = "https://user-images.githubusercontent.com/84118571/209464196-8aaad86a-7803-4780-9b70-9e8469c19337.png" width="50%" height="50%">
<img src = "https://user-images.githubusercontent.com/84118571/209464202-3a1ab093-ddf4-4024-9a3e-cea167ccfb10.png" width="50%" height="50%">


### 사용한 기술 스택

언어 : Java
Frontend, Backend 나눠서 보기 좋게 정리합니다.
기능 설명
프로젝트 주요 기능을 개발 용어를 최대한 사용하여 정리합니다.
담당역할 및 성과

