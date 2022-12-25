# emergency_rescue_app

### 프로젝트 설명
설치된 센서들의 값을 받아 응급상황발생시 구조문자를 전송하는 어플

***

### 개발내용
1.라즈베리파이에 온도/습도 센서, 불꽃 감지 센서, 초음파 센서를 연결
2.1초마다 센서의 값을 측정해서 실시간 클라우드 호스팅 DB에 동기화
3.실시간 클라우드 호스팅 DB에 동기화 된 센서들의 값을 애플리케이션으로 확인하고 감지
4.스마트폰의 자이로센서를 통해서 급격한 기울기 변화 시 낙상으로 감지하고, 119에 구조 문자 발송
5.애플리케이션 코딩으로 센서들의 값이 변화하거나 일정 수준 값을 넘으면 애플리케이션으로 알림을 출력 후 119에 구조 문자 발송

***

### 주요기능
1. 화재감지기능
2. 생체활동감지기능
3. ~~낙상감지기능~~ 
4. ~~온/습도 조절기~~ 

***

### 시스템 구성도
<img src = "https://user-images.githubusercontent.com/84118571/209464180-a4888b0b-176f-4f56-b5a4-f5e4a8718c45.png" width="50%" height="50%">
<img src = "https://user-images.githubusercontent.com/84118571/209464196-8aaad86a-7803-4780-9b70-9e8469c19337.png" width="50%" height="50%">
<img src = "https://user-images.githubusercontent.com/84118571/209464202-3a1ab093-ddf4-4024-9a3e-cea167ccfb10.png" width="50%" height="50%">

***

### DB구성
<img src = "https://user-images.githubusercontent.com/84118571/209464684-f041f0ff-f103-4a83-be5a-ee29d815f8bb.png" width="50%" height="50%">
NoSql Firebase 실시간DB

***

### 모듈 구성

<img src = "https://user-images.githubusercontent.com/84118571/209464590-120a4c99-e4b0-4428-940e-2187fffa5915.png" width="50%" height="50%">
라즈베리파이3b+, 온/습도센서, 화재감지센서, 초음파센서

***

### 어플 내용

<img src = "https://user-images.githubusercontent.com/84118571/209464764-9f011d47-9ae9-4251-a5a3-c0625d96d9df.png" width="50%" height="50%">
센서들의 값을 확인 후 응급상황일 시, 자동으로 문자발송여부알림창 출력후, 30초간 미입력시 자동 구조문자발송  


<img src = "https://user-images.githubusercontent.com/84118571/209464842-90160fbf-d6b9-439a-bd2f-253da46e1553.png" width="50%" height="50%">
구조문자를 보낼 연락처를 DB에 등록 및 수정,삭제  


<img src = "https://user-images.githubusercontent.com/84118571/209464960-ba029a86-1295-4693-8516-a6f145668b39.png" width="50%" height="50%">
카카오주소API로 집위치 검색 및 등록,수정  

***

### 개발환경
환경: 우분투, 윈도우11  
언어 : python, Java  
툴 : 안드로이드스튜디오

### 담당역할 및 성과
어플 제작 및 센서연결
