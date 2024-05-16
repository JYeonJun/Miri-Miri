SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약 조건 비활성화
truncate table goods;
truncate table wish_list;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약 조건 활성화