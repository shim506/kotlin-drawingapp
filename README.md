# kotlin-drawingapp

- 요구사항 파악
- 구현 완료 후 자신의 github 아이디에 해당하는 브랜치에 PR을 통해 코드 리뷰 요청
- 코드 리뷰 피드백에 대한 개선 작업 후 push
- 모든 피드백 완료 후 다음 단계를 도전하고 이전 과정 반복

# 학습 정리

1. Adaptive Icon

- 스마트폰 제조사가 원하는 모양대로 이미지를 지원함
- foreground 와 background 이미지를 통해 제어
- mipmap-anydpi 폴더에 저장됨

2. View 의 동적 생성

- View 동적 생성을 위해서는 layoutParms 를 관리해주어야한다. (xml 의 android: 에 해당)
- constraint 는 ConstraintSet 객체를 이용한다.
- 동적생성의 인자값은 dp 보다는 px 를 많이 사용하므로 이에 주의한다.

