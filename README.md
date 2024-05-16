# Miri-Miri

## **프로젝트 소개 내용**

```
모든 서비스의 기본이 되는 회원플랫폼과 그와 연관된 E-commerce 기능 개발
```

## **목표**

```
- 기본적인 E-commerce 서비스를 위한 회원플랫폼을 개발한다.
- 사용자는 플랫폼을 통해 회원가입, 로그인, 로그아웃, 마이페이지 등의 기본적인 유저 관리 기능을 편리하게 이용할 수 있어야 한다.
- 플랫폼은 인증/인가와 관련된 기본 기능 제공은 물론 커머스를 이용하기 위한 핵심 요소인 WishList, 주문내역, 주문상태 조회 등의 기능을 제공하여 사용자가 원하는 물품의 구매 및 진행상태를 원할하게 인지할 수 있도록 하여야 한다.
- 개발된 서비스는 확장 가능하고 효율적으로 동작해야 한다.
- 올바른 주문 및 결제/환불 등을 처리하기 위해선 회원의 개인정보가 저장되어야 한다. 이부분이 노출 되지 않도록 DB저장 시 혹은 네트워크 통신 시 보안적으로 충분히 고려되어야 한다.
```

## **활용하는 지식**

```
Docker, Spring Boot, Java / Kotlin, JWT, MSA, HTTP Request / Response
```

## **사용하는 라이브러리**

```
- 프로그래밍 언어: Java 21
- 빌드 툴: Gradle
- 프레임워크: SpringBoot 3.2.4
```

## **1주차 개요**

```
1주차 과제에서는 모든 서비스의 기본이 되는 회원플랫폼과 그와 연관된 E-commerce의 기부가 기능을 개발하는 것이 목표입니다.

이 플랫폼을 통해 인증 및 인가 에 대한 학습 및 개인정보처리 방침 등에 대해 학습 하는 것이 목표 입니다. 회원 플랫폼은 소셜 미디어뿐만 아니라 쇼핑몰, 중고거래 사이트 등 다양한 형태의 서비스로 변화할 수 있는 기본적이면서도 필수적인 플랫폼 입니다.
```

## **1주차 구현 요구사항**

```
Basic 구현 내용

1. 유저 관리
    - 회원가입 기능을 통해 사용자는 계정을 생성한다.
        - [x] 이메일 인증을 통한 회원가입 기능을 만든다.
        - [x] 개인정보, 이름, 비밀번호, 주소, 이메일은 암호화하여 저장한다.
        - [x] 필수 요소: 이름, 전화번호, 주소
    - 로그인 및 로그아웃 기능을 통해 사용자는 편리하게 서비스를 이용할 수 있다.
        - [x] jwt 토큰을 활용한 로그인 기능을 만든다.
        - [x] 로그아웃 기능을 제공한다.
    - 마이페이지를 통해 사용자는 자신의 정보를 업데이트할 수 있다.
        - [x] 주소, 전화번호를 업데이트 할 수 있다.
        - [x] 비밀번호를 업데이트 할 수 있다.
2. 상품
    - [x] 등록되어 있는 상품의 리스트를 보여주고 사용자가 구매할 수 있는 인터페이스를 제공한다.
    - [x] 상품을 클릭시 상품의 상세 정보를 제공해야한다.
    - [x] 상품의 재고 관리를 위한 유저 인터페이스는 별도로 구현하지 않는다.

주문
    - [x] 사용자는 노출된 상품에 한하여 주문 및 WishList에 등록 할 수 있다.
        - 1주차에서는 재고 부족으로 인한 주문 불가는 없다고 가정한다. 추후 구현 예정
    - 마이페이지를 통해 WishList에 등록한 상품과 주문한 상품의 상태를 조회 할 수 있다.
        - WishList에서는 내가 등록한 상품에 대한 정보를 보여주고, 아래의 기능을 제공한다.
            - [x] 제품의 상세 페이지로 이동
            - [x] 상품의 수량 변경 및 주문
            - [x] 위시리스트 내 항목의 수정
        - [x] 주문내역에서는 사용자가 주문한 상품에 대한 상태를 보여주고 상품에 대한 주문 취소, 반품 기능을 제공한다.(주문 내역중 일부 상품에 대한 취소및 반품은 없다고 가정합니다.)
            - [x] 주문 상품에 대한 상태 조회(주문 후 D+1에 배송중, D+2일에 배송완료가 됩니다.)
            - [x] 주문 상품에 대한 취소
                - 주문 상태가 배송중이 되기 이전까지만 취소가 가능하며 취소 후에 는 상품의 재고를 복구 되어야 합니다 주문 취소후 상태는 취소완료로 변경 됩니다.
            - [x] 상품에 대한 반품
                - [x] 배송 완료 후 D+1일까지만 반품이 가능하고 그이후에는 반품이 불가능 하다.
                - [x] 배송 완료가 된 상품에 대해서만 반품이 가능하며 반품한 상품은 반품 신청 후 D+1에 재고에 반영 된다. 재고에 반영된후 상태는 반품완료로 변경된다.

Advanced 구현 내용
- 모든 기기에서 로그아웃 기능을 제공한다.
- 비밀 번호 업데이트 시 모든 기기에서 로그아웃 되어야 한다.
- 이메일을 통한 2차 로그인 인증 기능을 제공합니다.
```

## **2주차 개요**

```
<aside>
💡 2주차는 마이크로 서비스에 대한 과제입니다.
마이크로서비스 아키텍처는 현대 소프트웨어 개발에서 매우 중요한 개념 중 하나입니다.

이 아키텍처는, 소프트웨어 시스템을 작고 독립적인 서비스로 나누어 개발하고 운영하는 방식으로, **확장성, 유연성, 그리고 유지보수 용이성을 강화**합니다. 이에 따라 마이크로서비스 템플릿 강의를 통해 여러분은 마이크로서비스 아키텍처의 핵심 원칙과 구현 방법을 숙지할 것입니다.

본 과제에서는 다양한 주제를 다루게 됩니다.
우선, 마이크로서비스 아키텍처의 개념과 이점에 대해 간략히 소개한 후, 각 서비스 간 통신, 데이터 관리, 보안 등 다양한 측면에서의 구현에 대한 실전적인 내용을 다룰 것입니다.
또한, 실습을 통해 직접 마이크로서비스 애플리케이션을 개발하고 배포하는 경험을 쌓게 될 것입니다.

이 과제를 통해 여러분은 마이크로서비스 아키텍처를 이해하고 적용하는 데 필요한 핵심 개념과 기술을 습득하게 될 것입니다. 함께 실습하며 실무에서 바로 활용 가능한 기술 스택과 패턴에 대한 이해를 높이며, 미래의 소프트웨어 프로젝트에서 마이크로서비스를 성공적으로 구현할 수 있는 능력을 키우는 데에 도움이 될 것입니다.

</aside>
```

## **2주차 구현 요구사항**

### Basic 구현 내용

```
1. Docker를 통한 로컬 개발 환경 구축
개인프로젝트나 초기 프로젝트에서는 주로 로컬 개발 환경을 Docker를 활용하여 구축하는 것이 효과적입니다. 이를 위해 아래는 Local Docker 환경 구축의 핵심 단계입니다:

- [ ] 도커 설치: 먼저, 해당 운영체제에 맞는 도커를 도커 공식 사이트에서 다운로드하고 설치합니다.
- [ ] 도커 컴포즈 작성: `docker-compose.yml` 파일을 작성하여 각 마이크로서비스 컴포넌트 및 필수 인프라스트럭처(예: 데이터베이스, 메시지 브로커)를 정의합니다.
    - [ ]  사용하는 DB(Postgres or MongoDB etc)
    - [ ]  편의성을 위한 데이터 베이스 관리 툴(ex. Adminer)
- [ ]  로컬 실행: 터미널 또는 명령 프롬프트에서 프로젝트 디렉터리로 이동한 후 `docker-compose up` 명령을 실행하여 도커 컴포즈를 시작합니다.
- [ ]  애플리케이션 확인: 웹 브라우저나 API 클라이언트 등을 사용하여 각 마이크로서비스가 정상적으로 동작하는지 확인합니다.

로컬 Docker 환경을 통해 애플리케이션을 구동함으로써 초기 개발 단계에서 빠르게 환경을 구성하고 테스트할 수 있습니다. 필요에 따라 도커 컴포즈 파일을 수정하여 서비스 간의 통신이나 외부 의존성을 관리할 수 있습니다.

2. 모노리스 서비스를 마이크로 서비스로 나누기

- (필독!) 상세 설명
E-Commerce Application
- User Service
- Order Service
- Product Service

E-commerce 플랫폼을 구축하기 위해 기존의 모노리스 서비스를 마이크로 서비스 아키텍처로 분할합니다. 이번 챕터에서는 인증, 주문, 상품 등 각기 다른 책임을 가진 독립적인 마이크로 서비스를 설계하고 구현합니다. 이제 각 서비스는 독립적으로 개발, 배포, 확장이 가능하며, 서로 협력하여 전체 시스템을 완성합니다.

1. 유저 서비스:
    - 사용자 등록, 이메일 확인, 로그인, 로그아웃, 토큰 생성 및 검증, 수정, 조회 등의 기능을 담당하는 서비스를 구현합니다.
    - 이 서비스는 독립적으로 배포 가능해야 하며, 유저 관리 데이터베이스와 프로필 데이터베이스를 관리합니다.
2. 주문 서비스:
    - 주문 및 위시리스트에 관련된 서비스를 제공합니다.
    - 이 서비스는 독립적으로 배포 가능해야 하며, 주문 데이터베이스를 관리합니다.
3. 상품 서비스:
    - 상품 정보를 제공 및 재고 관리에 관련된 기능을 제공합니다..
    - 이 서비스는 독립적으로 배포 가능해야 하며, 상품 데이터베이스를 관리합니다.

이렇게 변경된 구조에서, 각 마이크로서비스는 독립적으로 개발, 배포, 확장이 가능하며, 서로 통신하여 전체 시스템을 완성할 수 있습니다.

1. 3개의 모노리스 서비스 준비하기

- [ ]  settings.gradle 및 build.gradle을 활용해 멀티 모듈 프로젝트 만들기
    - [ ]  User Service에 기존의 모노리스 서비스를 그대로 복사해서 넣어 실행시켜 보기
        - [ ]  포트 8080
    - [ ]  Order Service에 기존의 모노리스 서비스를 그대로 복사해서 넣어 실행시켜 보기
        - [ ]  포트 8081
    - [ ]  Product Service에 기존의 모노리스 서비스를 그대로 복사해서 넣어 실행시켜 보기
        - [ ]  포트 8082

즉, 하나의 모노리스 서비스를 총 3벌로 복제 하여, 복사본 3개를 동시에 띄우기

2. 3개의 모노리스 서비스를 3개의 마이크로 서비스로 만들기

💡 이제 실제 마이크로 서비스에 가깝게, 서비스를 작게 나누어 봅시다. 이 과정의 난이도는 작성하신 코드의 복잡도에 크게 비례합니다. 대부분의 회사에서 모노리스를 마이크로 서비스로 나누길 시도하다가 여기에서 멈추고 미루게 되는 경우가 많습니다.

아래는 라이브로 운영 중인 상황에서 마이크로서비스로 전환하는 환경과는 조금 다르지만, 연습 상황에서 시행 가능한 크기의 작업 단위로 나눈 방법입니다.

체크 박스의 수는 작지만, 많은 고민을 하셔야 할 것입니다.

- 특정 로직이 어떤 도메인에 들어가야 할 지
- 여러 도메인에 걸쳐있는 로직의 경우, 어느쪽으로 보내야 할 지
- 클라이언트(프론트엔드, 모바일 앱 등)를 위한 API들은 각각 어떤 도메인으로 가야 할 지

- [ ]  User Service에 해당 도메인 로직만 남기고 나머지 코드들 지우기
    - [ ]  코드를 지워서 생기는 오류는 적당한 방법으로 임시처리 한다.
        - ex.  `throw new UnsupportedOperationException("TODO: Implement the rest of the functionality");` 또는 `// TODO:`   코멘트와 `return null`
- [ ]  Order Service에 해당 도메인 로직만 남기고 나머지 코드들 지우기
- [ ]  Product Service에 해당 도메인 로직만 남기고 나머지 코드들 지우기

3. 3개의 마이크로 서비스가 서로 통신하게 하기

💡 앞 과정에서 우리는 각 서비스들의 메인 도메인이 아닌 코드들을 덜어 냈습니다. 하지만 동작을 잘 해 내려면 모든 도메인 로직들이 서로 연결되어 있어야 합니다. 스프링의 Rest Controller와 Feign client를 통해 위의 화살표 들을 연결해 봅시다.

쉽지 않겠지만, 아래 사항을 모두 고려해서 적용해주세요.

- 어떤 작업 단위로 REST API 제공할 것인지
- 해당 API에 어떤 이름이 적합한지
- REQUEST / RESPONSE의 정의는 어떻게 할 지
- 마이크로 서비스 클라이언트(사용하는쪽. 화살표의 출발점)에서 코드는 어떻게 관리할 지
- 실제 클라이언트(프론트엔드, 모바일 앱 등)에서 제공하는 API는 누가 관리할 지

- [ ]  User Service에서 제공해야하는 REST API들 만들기
    - [ ]  기존에 있던 API(for 실제 클라이언트)와 구분하기 (ex. 주소 `/api/internal/xxxx`)
- [ ]  Order Service에서 제공해야하는 REST API들 만들기
- [ ]  Product Feed Service에서 제공해야하는 REST API 만들기
- [ ]  User Service에 클라이언트 코드 구현하기
    - [ ]  to Order Service
    - [ ]  to Product Service
- [ ]  Order Service에 클라이언트 코드 구현하기
    - [ ]  to User Service
    - [ ]  to Product Service
- [ ]  Product Service에 클라이언트 코드 구현하기
    - [ ]  to User Service
    - [ ]  to Order Service
```

### Advanced 구현 내용

````
1. API Gateway 만들기

💡 위의 경우, 클라이언트에 제공되는 API가 세 서비스로 분산되어 있습니다. 이로 인해 클라이언트는 각 서버가 제공하는 API 목록을 알아야 하며, 각 서버에 맞는 요청을 보내야 합니다.

마이크로서비스 환경에서는 각 서버가 제공하는 API 목록을 알아야 하는 것이 필수적이지만, 내부 동작과 외부로 노출되는 서비스 구조의 공개는 별개입니다. 이를 위해 API Gateway의 도입이 필요합니다.

- [ ]  API GATEWAY 서비스를 새로 만들어 실행시키기(port: 8083)
- [ ]  기존의 모노리스 서비스가 가지고 있던 모든 API 들을 API GATEWAY에 노출한다.
- [ ]  해당 API로 들어온 요청을, 내부의 마이크로 서비스로 전달한다.

2. 장애 상황 연출 및 회복 탄력성 갖추기

- [ ]  아래 컨트롤러를 임의의 서비스 A에 구현합니다.

    ```java
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.time.LocalTime;
    import java.util.Random;

    @RestController
    public class ErrorfulController {

        @GetMapping("/errorful/case1")
        public ResponseEntity<String> case1() {
            // Simulate 5% chance of 500 error
            if (new Random().nextInt(100) < 5) {
                return ResponseEntity.status(500).body("Internal Server Error");
            }

            return ResponseEntity.ok("Normal response");
        }

        @GetMapping("/errorful/case2")
        public ResponseEntity<String> case2() {
            // Simulate blocking requests every first 10 seconds
            LocalTime currentTime = LocalTime.now();
            int currentSecond = currentTime.getSecond();

            if (currentSecond < 10) {
                // Simulate a delay (block) for 10 seconds
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return ResponseEntity.status(503).body("Service Unavailable");
            }

            return ResponseEntity.ok("Normal response");
        }

        @GetMapping("/errorful/case3")
        public ResponseEntity<String> case3() {
            // Simulate 500 error every first 10 seconds
            LocalTime currentTime = LocalTime.now();
            int currentSecond = currentTime.getSecond();

            if (currentSecond < 10) {
                return ResponseEntity.status(500).body("Internal Server Error");
            }

            return ResponseEntity.ok("Normal response");
        }
    }
    ```

- [ ]  회복탄력성 갖추기
    - [ ]  Java Resilience 4j의 Circuit Breaker, Retry 개념을 공부한다.
        - https://resilience4j.readme.io/ 참고
    - [ ]  A와 다른 임의의 서비스 B에 feign client를 구현하여 위 API를 호출한다.
        - [ ]  1초에 몇 번씩, 1분 이상 주기적으로 호출하며 경과를 지켜본다.
    - [ ]  B의 feign client에 resilience4j를 활용하여 Circuit Breaker와 Retry를 붙인다.

    💡 현재는 "회복탄력성"이라는 개념만 이해해도 충분합니다. 현역 엔지니어들 중에서도 이 개념을 모르는 경우가 많습니다. 그리고 Resilience 4j라는 툴이 있다는 것만 알아도 충분합니다.  resilience4j에는 Circuit Breaker나 Retry 외에도 많은 회복탄력성 툴들이 있습니다. 또한 각 툴들 별로 조절할 수 있는 값들이 정말 많이 있는데요, 실제 상황에 맞는 도구를 고르시고 설정값들을 조절하며 사용하시면 됩니다.
````
