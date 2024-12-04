# Minecraft Economy Plugin

## 📌 소개
마인크래프트 서버(v1.21.3)를 위한 종합 경제 플러그인으로, 상점 시스템, NPC, 동적 가격 조정 기능을 제공합니다.

## 🛠 기술 스택
- Java
- Spring Boot
- MySQL
- Bukkit/Spigot API
- Citizens API

## 🏗 구조
### 엔티티
```java
- PlayerAccount: 플레이어 은행 및 경제 관리
- ShopItem: 상점 아이템 정보 및 가격
- ShopNPC: NPC 데이터 및 동작 관리
- Transaction: 거래 내역 및 로깅
```

### 레포지토리
```java
- PlayerAccountRepository: 플레이어 계정 데이터 관리
- ShopItemRepository: 상점 아이템 데이터 관리
- ShopNPCRepository: NPC 데이터 관리
- TransactionRepository: 거래 내역 관리
```

### 서비스
```java
- NPCService: NPC 생성 및 관리
- ShopService: 아이템 및 상점 관리
```

## 🎮 명령어
### 관리자 명령어
```
/additem <아이템명> <구매가격> <판매가격> <아이템태그>
- 새로운 아이템을 상점에 추가 (주손에 들고 있는 아이템)

/removeitem <아이템명>
- 상점에서 아이템 제거

/createnpc <NPC명> <월드> <NPC태그>
- 상점 NPC 생성
- 월드 옵션: 네더, 오버월드, 엔더

/removenpc <NPC명>
- 상점 NPC 제거

/reloadnpc
- 모든 NPC 리로드

/updateprices
- 가격 즉시 업데이트
```

### 플레이어 명령어
```
/transfer <플레이어> <금액>
- 다른 플레이어에게 송금
```

## ⚙️ 주요 기능

### 경제 시스템
- 플레이어 접속 시 자동 계좌 생성
- 실시간 스코어보드 잔액 업데이트
- 안전한 거래 시스템

### 상점 시스템
- 판매량 기반 동적 가격 조정
- 128개 판매 시 가격 조정
- 태그 기반 NPC 상점
- 커스터마이징 가능한 구매/판매 가격

### NPC 시스템
- 태그 기반 아이템 필터링
- 월드별 배치
- 영구 저장

## 🔜 개발 예정 기능
```
1. 경제 시스템:
- 거래 내역 조회
- 경제 통계

2. 상점 시스템:
- 커스텀 가격 공식
- 판매 분석
- 대량 거래

3. NPC 시스템:
- 커스텀 NPC 스킨
- 고급 상호작용 메뉴
- 상점 카테고리

4. 관리자 도구:
- 경제 모니터링
- 거래 롤백
- 상점 관리 GUI
```

## 💾 데이터베이스 스키마
```sql
CREATE TABLE player_accounts (
    uuid VARCHAR(36) PRIMARY KEY,
    player_name VARCHAR(16) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.0,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shop_items (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(50) NOT NULL UNIQUE,
    item_data TEXT NOT NULL,
    buy_price DECIMAL(15,2) NOT NULL,
    sell_price DECIMAL(15,2) NOT NULL,
    item_tag VARCHAR(20),
    sold_amount INT DEFAULT 0
);

-- 기타 테이블 생략
```

## 📝 라이선스
이 프로젝트는 MIT 라이선스를 따릅니다.
