# KakaoPharmacy(약국 검색)
KakaoPharmacy는 Kakao OpenAPI를 사용하여 근처 약국 위치 조회 서비스를 지원하는 웹 애플리케이션입니다.

사용자는 주소를 입력하면 Harversine formula 공식을 적용하여 근처 10km 이내 약국 중 가장 가까운 3곳을 조회해줍니다.

그 후 약국의 정확한 위치 확인을 위한 로드뷰 링크 또한 제공합니다.

## E-R 다이어그램
![Image](https://github.com/user-attachments/assets/f3666812-a89d-4da7-8e1a-7043de4ccdf1)

### 프론트엔드
- Thymeleaf

### 백엔드
- Spring Boot
- Redis
- MariaDB

### 기타
- Docker (Redis, MariaDB 컨테이너화)

## 주요 기능
- **약국 검색**:
  - 최초 검색시 KAK kakao OpenAPI를 활용하여 약국 검색 및 Redis 저장 서비스를 구현
  - 중복 Redis 캐싱을 활용해 약국 데이터를 빠르게 조회하여 성능 최적화
      - LCP(Largest Contentful Paint) 기준 76% (약 4.17배) 성능 향상
  - haversine formula 공식을 적용하여 사용자에게 가장 가까운 약국 추천
  - Base62 인코딩을 활용하여 shortUrl을 제공하며 이를통해 약국의 로드뷰 바로가기 기능 제공
