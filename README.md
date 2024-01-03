# IROAS-Server
Iroas VR 운영 관련 서버

dev 환경 구축하기
1. yml 파일 다운로드 받기
   [config-repo repository](https://github.com/MTVSquad/config-repo)에서 3개 다운로드
2. yml에 기재된 데이터베이스 생성
3. 기재된 데이터베이스에 대한 유저 생성 및 권한 부여

dev DB 구축
1. yml에서 ddl-auto 옵션 이용하기
2. create-drop으로 설정(주의 prod, stage 환경에서는 반드시 update, none으로 체크되어 있는지 확인하고 배포 할것)
3. 테이블 및 컬럼 정보는 Comment 확인할 것

Swagger 문서
1. IP주소:포트번호/swagger-ui/index.html

Rule
1. release/** 브랜치에는 PR 거쳐서 진행, 직접 PUSH 금지

## Domain
![image](https://github.com/MTVSquad/IROAS-Server/assets/94158097/972154c1-0782-4f0b-8812-37283f20ff62)

## Entity
![image](https://github.com/MTVSquad/IROAS-Server/assets/94158097/4b24fa5f-c18a-4995-88dd-bb8a77de678e)

## Entity Relation
![image](https://github.com/MTVSquad/IROAS-Server/assets/94158097/3e8f4f59-385e-430e-982a-18b6449f65fc)

## ERD
![image](https://github.com/MTVSquad/IROAS-Server/assets/94158097/83c3bd9a-5939-4310-a87f-95304732f97e)


