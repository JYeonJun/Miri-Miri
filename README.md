<span style="margin-left: 10px;"><h1><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/44fdd68b-d4d7-4b87-bbef-b0bb439a8f7f" width="30" height="auto" align="left" />미리미리(Miri-Miri) 프로젝트</h1></span>

## 💡**프로젝트 소개**
미리미리(Miri-Miri) 프로젝트는 마이크로서비스 아키텍처를 활용해 미리 상품을 담아두고 필요할 때 구매하는 기능을 제공합니다.<br/>
마이크로서비스 아키텍처를 통해 유연하고 확장 가능한 서비스 운영이 가능합니다.<br/>

**프로젝트 기간**: 2024.04.17 ~ 2024.05.14

## 🚀**시스템 구조**
<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/8f54fb86-83e5-4f81-86b7-b17faaea4821"/></p>

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

**데이터베이스 & 캐싱**
<div>
    <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white"/>
    <img src="https://img.shields.io/badge/Redis-D92A2A?style=for-the-badge&logo=Redis&logoColor=white"/>
</div>

**메시징 시스템**
<div>
    <img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white"/>
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
- **Kafka** 이벤트 기반 **상품 주문**
  - 비동기 통신으로 머이크로서비스 간 결합도를 줄이고 확장성에 뛰어난 이벤트 기반 통신 사용
<details>
<summary>상품 주문 플로우차트</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/27f2efac-6248-4907-9a4e-ad2517f00e66"/></p>

</div>
</details>

- **Redis Caching**을 통한 상품 재고 관리 및 동시성 처리
  - 데이터베이스 부하를 줄이고, 높은 성능 유지
- **Redis Replication**을 통한 레디스 분산 처리
  - 데이터 가용성과 내구성을 보장하고, 읽기 성능 향상, 마스터-슬레이브 구조를 통해 데이터 복제와 장애 조치(Failover) 지원
- **OpenFeign**을 활용한 마이크로서비스 간 통신
  - 마이크로서비스 간의 데이터 조회
- **Resilience4j**를 통한 마이크로서비스 통신 안전성 향상
  - Circuit Breaker를 사용한 마이크로서비스 간 장애 전파 방지
  - Time Limiter를 사용한 무한 대기 방지
- **Api Gateway**를 통한 마이크로서비스 아키텍처 구축
  - 클라이언트 요청을 적절한 마이크로서비스로 라우팅, 인증/인가 기능 중앙 집중화
- 스케줄러를 통한 주문 및 배송 상태 관리
  - 주문 및 배송 상태의 주기적인 업데이트
- **Spring Security, JWT**를 사용한 인증/인가
  - 무상태(stateless) 애플리케이션 설계
- Google SMTP를 사용한 이메일 인증
  - 신뢰성이 높고 쉽게 구현할 수 있는 Google SMTP 도입

## 📂**API 명세서**
([바로가기](https://documenter.getpostman.com/view/20733282/2sA3JRaKQo))

## 📈**성능 최적화 및 트러블 슈팅**
프로젝트 진행 과정에서 발생한 트러블 슈팅과 성능 최적화 사례를 공유합니다.<br/>
#### **Redis Cache 쓰기 전략별(+ Lua 스크립트) 상품 주문 성능 개선 과정** ([자세히 보기](https://yenjjun187.tistory.com/1039))
<p align="center"><img src="https://github.com/JYeonJun/miri-miri/assets/97449471/3ce54f99-5857-43a0-883e-704aa305a4c7" style="width:60%;"/></p>

- 캐시 쓰기 전략 변경: **Write-Through** → **Write-Back**
- **Lua 스크립트** 적용: Redis의 원자성 활용한 **분산락(Lock) 해제**
- TPS: `164.5` → `431.2` (**약 162.2% 성능 개선**)

#### **트랜잭션과 카프카 이벤트 발행 사이 정합성 문제 해결** ([자세히 보기](https://yenjjun187.tistory.com/1043))
- 문제 상황: MSA 환경에서 트랜잭션과 이벤트 발행의 실행 시점 차이로 인해 데이터 일관성 유지 문제 발생
- 원인: 트랜잭션이 성공적으로 커밋되기 전에 이벤트를 발행하는 로직이 포함되어 있어, 트랜잭션의 실패와 상관없이 이벤트가 발행될 수 있는 구조
- 해결 방법: @TransactionalEventListener를 도입하여 트랜잭션의 결과(커밋 또는 롤백)에 따라 조건적으로 이벤트를 발행하도록 변경 -> 트랜잭션이 성공적으로 커밋된 후에만 이벤트를 발행하고, 트랜잭션이 롤백되는 경우 실패와 관련된 이벤트를 발행하도록 로직 수정

#### **Redis Replication을 사용한 상품 재고 관리** ([자세히 보기](https://yenjjun187.tistory.com/1042))
- 데이터 손실 방지: Redis 복제를 통해 주요 데이터를 여러 인스턴스에 복제하여 데이터 손실 위험 감소
- 성능 향상: 복제된 인스턴스를 통해 읽기 요청을 분산 처리함으로써 읽기 성능 향상
- 고가용성: 하나의 인스턴스에 문제가 발생하더라도 다른 인스턴스를 통해 지속적인 서비스 제공이 가능하여 시스템의 가용성 향상

#### **Kafka에서 동일한 토픽을 여러 서비스에서 소비하는 문제 해결** ([자세히 보기](https://yenjjun187.tistory.com/1040))
- 문제 상황: 주문 취소 기능을 구현하는 과정에서, 동일한 '주문 취소 토픽'을 상품 서비스와 결제 서비스가 동시에 소비하지 못하는 문제 발생
- 원인: 두 서비스가 동일한 컨슈머 그룹을 사용하면서 하나의 토픽에서 메시지를 소비하려 했기 때문에 한 서비스만 메시지를 소비
- 해결 방법: 각 서비스마다 별도의 컨슈머 그룹 ID를 지정하여, 서비스들이 독립적으로 메시지를 소비할 수 있도록 설정

## 🤖실행 방법
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
