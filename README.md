<span style="margin-left: 10px;"><h1><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/44fdd68b-d4d7-4b87-bbef-b0bb439a8f7f" width="30" height="auto" align="left" />미리미리(Miri-Miri) 프로젝트</h1></span>

## 💡**프로젝트 소개**
미리미리(Miri-Miri) 프로젝트는 마이크로서비스 아키텍처를 활용해 미리 상품을 담고 구매하는 기능을 제공합니다.<br/>
마이크로서비스 아키텍처를 통해 유연하고 확장 가능한 서비스 운영이 가능합니다.<br/>

**프로젝트 기간**: 2024.04.17 ~ 2024.05.14

## 🚀**시스템 구조**
<details>
<summary>시스템 구조 이미지</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/a8e38ce7-3e0a-4907-9053-0f022343a83f"/></p>

</div>
</details>

### 개발 환경
- 프로그래밍 언어: Java 21
- 빌드 툴: Gradle
- 프레임워크: SpringBoot 3.2.5
- 운영체제: macOS

### 기술 스택
**프레임워크 & 라이브러리**
<div>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Gateway-6DB33F?style=for-the-badge&logo=Spring Cloud Gateway&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Netflix Eureka-6DB33F?style=for-the-badge&logo=Spring Cloud Netflix Eureka&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Config-6DB33F?style=for-the-badge&logo=Spring Cloud Config&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Bus-6DB33F?style=for-the-badge&logo=Spring Cloud Bus&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud OpenFeign-6DB33F?style=for-the-badge&logo=OpenFeign&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Circuit Breaker-6DB33F?style=for-the-badge&logo=CircuitBreaker&logoColor=white"/>
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"/>
    <img src="https://img.shields.io/badge/QueryDSL-59666C?style=for-the-badge&logo=QueryDSL&logoColor=white"/>
</div>

**메시징 시스템**
<div>
    <img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white"/>
    <img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white"/>
</div>

**인프라**
<div>
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
</div>

## 🌤️**ERD**
<details>
<summary>ERD 이미지</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/cdec958a-9c93-4585-807a-3ec80673e957"/></p>

</div>
</details>

## 📌**주요 기능**
- **Redis Caching**을 통한 상품 재고 관리 및 동시성 처리
- **Redis Replication**을 통한 레디스 분산 처리
- **Kafka**를 통한 이벤트 기반 아키텍처 구축
- **OpenFeign**을 활용한 마이크로서비스 간 통신
- **Circuit Breaker**를 활용한 회복탄력성 증진
- **Api Gateway**를 통한 마이크로서비스 아키텍처 구축
- 스케줄러를 통한 주문 및 배송 상태 관리
- **Spring Security, JWT**를 사용한 인증/인가
- Google SMTP를 사용한 이메일 인증
<details>
<summary><strong>상품 주문 플로우차트</strong></summary>
<div markdown="1">

<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/27f2efac-6248-4907-9a4e-ad2517f00e66"/></p>

</div>
</details>

## 📂**API 명세서**
([바로가기](https://documenter.getpostman.com/view/20733282/2sA3JRaKQo))

## 📈**성능 최적화 및 트러블 슈팅**
프로젝트 진행 과정에서 발생한 트러블 슈팅과 성능 최적화 사례를 공유합니다.<br/>
- **Redis Cache 쓰기 전략별(+ Lua 스크립트) 상품 주문 성능 개선 과정** ([바로가기](https://yenjjun187.tistory.com/1039))
- **Kafka에서 동일한 토픽을 여러 서비스에서 소비하는 문제 해결** ([바로가기](https://yenjjun187.tistory.com/1040))
- **Redis Replication을 사용한 상품 재고 관리** ([바로가기](https://yenjjun187.tistory.com/1042))
- **트랜잭션과 카프카 이벤트 발행 사이 정합성 문제 해결** ([바로가기](https://yenjjun187.tistory.com/1043))

## 실행 방법
### 실행 요구 사항
<details>
<summary>간략히</summary>
<div markdown="1">

1. 빈 폴더 생성
2. application.yml, goods-service.yml, order-service.yml, payment-service.yml, user-service.yml 파일 생성(아래 참고)
3. config-service의 application.yml 파일의 spring.cloud.config.server.native.search-locations 값으로 폴더 위치 지정

**application.yml**
```yaml
token:
  expiration_time: <토큰 만료 시간 단위(밀리초)>
  secret: <JWT 생성에 사용될 시크릿 키>

api:
  gateway:
    ip: <API 게이트웨이 IP 주소>

kafka:
  server: localhost:9092
  # 주문 서비스를 위한 카프카 그룹 ID. 예: 'order-service-group'
  order-group-id: <주문 서비스의 카프카 그룹 ID>
  # 상품 서비스를 위한 카프카 그룹 ID. 예: 'goods-service-group'
  goods-group-id: <상품 서비스의 카프카 그룹 ID>
  # 결제 서비스를 위한 카프카 그룹 ID. 예: 'payment-service-group'
  payment-group-id: <결제 서비스의 카프카 그룹 ID>

miri:
  aes:
    secret-key: AbCdEfGhIjKlMnOpQrStUvWxYz123456
```

**goods-service.yml, order-service.yml, payment-service.yml**
```yaml
database:
  password: mariadb1234
```

**user-service.yml**
```yaml
miri:
  mail:
    username: <이메일 주소>
    password: <구글 앱 비밀번호>

database:
  password: mariadb1234
```

</div>
</details>

### docker-compose.yml 실행
```bash
docker compose up -d
```

### 서비스 실행 순서
```
config-service -> discovery-service -> apigateway-service -> 나머지 서비스(user, order, goods, payment)
```

## 🗂️파일 구조
<details>
<summary>접기/펼치기</summary>
<div markdown="1">

```
📦miri-miri-msa
 ┣ 📂apigateway-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂generated
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂apigatewayservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtAuthorizationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ApigatewayServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📜application-secret.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜bootstrap.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂config-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂configservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ConfigServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┗ 📜application.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂core-module
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂coremodule
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CreatedDateEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂goods
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FeignGoodsReqDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FeignGoodsRespDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CancelOrderEventDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FeignOrderRespDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂wishlist
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FeignWishListReqDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FeignWishListRespDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂aop
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomValidationAdvice.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ex
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomApiException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CustomExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂vo
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaVO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CoreModuleApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┗ 📜application.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂discovery-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂discoveryservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiscoveryServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┗ 📜application.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂discoveryservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiscoveryServiceApplicationTests.java
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂goods-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂goodsservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserServiceClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaConsumerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaProducerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂redis
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedisProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Resilience4JConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂goods
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Goods.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂wishlist
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WishList.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂goods
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestGoodsDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseGoodsDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂wishlist
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestWishListDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseWishListDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dummy
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DummyDevInit.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DummyObject.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂event
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsToOrderEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoodsToOrderEventListener.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂facade
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedissonLockStockFacade.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂goods
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsInternalService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsInternalServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoodsServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaReceiver.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaSender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂redis
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedisStockService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂wishlist
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WishListService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WishListServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DatabaseSyncScheduler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoodsServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📂db
 ┃ ┃ ┃ ┃ ┃ ┗ 📜teardown.sql
 ┃ ┃ ┃ ┃ ┣ 📜application-test.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜bootstrap.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂order-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂orderservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoodsServiceClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaConsumerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaProducerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Resilience4JConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Order.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂returnrequest
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReturnRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂shipping
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Shipping.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestOrderDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseOrderDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dummy
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DummyObject.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂event
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CancelOrderEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaReceiver.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaSender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderStatusScheduler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AESUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📂db
 ┃ ┃ ┃ ┃ ┃ ┗ 📜teardown.sql
 ┃ ┃ ┃ ┃ ┣ 📜application-test.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜bootstrap.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂payment-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂paymentservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaConsumerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaProducerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂payment
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Payment.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂event
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentAbortedEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentAbortedEventListener.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kafka
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaReceiver.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KafkaSender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂payment
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PaymentService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PaymentServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📜application-test.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜bootstrap.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📂user-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂miri
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂userservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoodsServiceClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderServiceClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂redis
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedisProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Resilience4JConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailVerificationApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserClientController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂email
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailVerificationCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜EmailVerificationCodeRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRole.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestUserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseUserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dummy
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DummyDevInit.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DummyObject.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PrincipalDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PrincipalDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂email
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailVerificationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜EmailVerificationServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AESUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomResponseUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StringEncryptUniqueConverter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📂db
 ┃ ┃ ┃ ┃ ┃ ┗ 📜teardown.sql
 ┃ ┃ ┃ ┃ ┣ 📜application-test.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜bootstrap.yml
 ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┗ ...
 ┃ ┣ 📜build.gradle
 ┃ ┣ ...
 ┣ 📜build.gradle
 ┣ 📜docker-compose.yml
 ┗ 📜settings.gradle
 ┣ …
```

</div>
</details>
