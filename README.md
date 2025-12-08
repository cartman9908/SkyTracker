
⸻

✈️ SkyTracker

항공권을 더 빠르고, 더 똑똑하게 찾을 수 있도록 돕는 항공권 가격 추적 서비스
실시간 항공권 데이터 기반 알림 및 검색 기능 제공

⸻

📌 프로젝트 개요

SkyTracker는 사용자가 직접 여러 번 검색하지 않아도
특정 항공권의 가격 하락과 인기 노선을 빠르게 파악할 수 있도록 지원합니다.
항공권 데이터는 수시로 변동되며, 이를 실시간으로 파악하는 것은 비용적/기술적으로 비효율적이라는 문제를 해결하고자 했습니다.  ￼

이를 위해
	•	Pareto 법칙 기반 Top10 인기 노선 분석
	•	Redis 기반 캐싱
	•	Kafka 기반 비동기 데이터 수집
등을 설계하여 항공권 검색 효율성과 사용자 경험을 높였습니다.  ￼

⸻

아키텍처 

<img width="1482" height="932" alt="image" src="https://github.com/user-attachments/assets/d28d0de9-651a-4c6a-8e40-04c9ea1ee08c" />

아키텍처 구성은 다음과 같습니다. (Spring Boot 3개 서비스 + Kafka + Redis + MySQL + ElasticSearch)
Kubernetes 기반 클러스터에서 구성, NGINX Ingress 적용
데이터 수집 → Kafka → 저장 → API 서버에서 제공

⸻

ERD

<img width="1928" height="1312" alt="image" src="https://github.com/user-attachments/assets/5930de0b-509a-4441-a242-eb1317f20902" />


✨ 핵심 기능
	•	항공권 조건 검색 (출발/도착지, 날짜, 좌석 등)
	•	알림 등록 및 이메일 수신
	•	인기 노선 Top10 가격 트렌드 제공
	•	검색한 항공권 가격 알림 등록/해제
	•	GPT 기반 항공권 추천 챗봇
	•	Oauth2 기반 소셜 로그인 + 이메일 알림 발송  ￼

⸻
