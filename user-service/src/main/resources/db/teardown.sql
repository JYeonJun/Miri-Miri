SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약 조건 비활성화
truncate table goods;
truncate table order_details;
truncate table orders;
truncate table return_requests;
truncate table shipping;
truncate table users;
truncate table wish_list;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약 조건 활성화