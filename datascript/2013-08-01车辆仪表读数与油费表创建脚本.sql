-- Create table
create table CAR_INSTRUMENT_READING
(
  INSTRUMENT_READING_ID NUMBER not null,
  INSTRUMENT_READING    NUMBER,
  CAR_ID                NUMBER,
  RECORDING_TIME        DATE,
  CREATE_TIME           DATE,
  CREATE_USER_ID        NUMBER,
  CREATE_REMARKS        VARCHAR2(200),
  DELETE_TIME           DATE,
  DELETE_USER_ID        NUMBER,
  DELETE_CAUSE          VARCHAR2(200),
  STATUS                NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column CAR_INSTRUMENT_READING.INSTRUMENT_READING_ID
  is '����';
comment on column CAR_INSTRUMENT_READING.INSTRUMENT_READING
  is '�Ǳ����';
comment on column CAR_INSTRUMENT_READING.CAR_ID
  is '����ID';
comment on column CAR_INSTRUMENT_READING.RECORDING_TIME
  is '��¼ʱ��';
comment on column CAR_INSTRUMENT_READING.CREATE_TIME
  is '����ʱ��';
comment on column CAR_INSTRUMENT_READING.CREATE_USER_ID
  is '������ID';
comment on column CAR_INSTRUMENT_READING.CREATE_REMARKS
  is '������ע';
comment on column CAR_INSTRUMENT_READING.DELETE_TIME
  is 'ɾ��ʱ��';
comment on column CAR_INSTRUMENT_READING.DELETE_USER_ID
  is 'ɾ����ID';
comment on column CAR_INSTRUMENT_READING.DELETE_CAUSE
  is 'ɾ��ԭ��';
comment on column CAR_INSTRUMENT_READING.STATUS
  is '״̬ 1Ϊ���� 0Ϊ��Ч';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CAR_INSTRUMENT_READING
  add constraint PK_INSTRUMENT_READING_ID primary key (INSTRUMENT_READING_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;


-- Create table
create table CAR_FUEL_BILLS
(
  FUEL_BILLS_ID  NUMBER not null,
  FUEL_BILLS     NUMBER,
  CAR_ID         NUMBER,
  RECORDING_TIME DATE,
  CREATE_TIME    DATE,
  CREATE_USER_ID NUMBER,
  CREATE_REMARKS VARCHAR2(200),
  DELETE_TIME    DATE,
  DELETE_USER_ID NUMBER,
  DELETE_CAUSE   VARCHAR2(200),
  STATUS         NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column CAR_FUEL_BILLS.FUEL_BILLS_ID
  is '����';
comment on column CAR_FUEL_BILLS.FUEL_BILLS
  is '�ͷ�';
comment on column CAR_FUEL_BILLS.CAR_ID
  is '����ID';
comment on column CAR_FUEL_BILLS.RECORDING_TIME
  is '��¼ʱ��';
comment on column CAR_FUEL_BILLS.CREATE_TIME
  is '����ʱ��';
comment on column CAR_FUEL_BILLS.CREATE_USER_ID
  is '������ID';
comment on column CAR_FUEL_BILLS.CREATE_REMARKS
  is '������ע';
comment on column CAR_FUEL_BILLS.DELETE_TIME
  is 'ɾ��ʱ��';
comment on column CAR_FUEL_BILLS.DELETE_USER_ID
  is 'ɾ����ID';
comment on column CAR_FUEL_BILLS.DELETE_CAUSE
  is 'ɾ��ԭ��';
comment on column CAR_FUEL_BILLS.STATUS
  is '״̬ 1Ϊ���� 0Ϊ��Ч';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CAR_FUEL_BILLS
  add constraint PK_FUEL_BILLS_ID primary key (FUEL_BILLS_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;



-- Create sequence 
create sequence SEQ_CAR_INSTRUMENT_READING
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


create sequence SEQ_CAR_FUEL_BILLS
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


alter table car_fuel_bills modify create_remarks varchar2(300);  
alter table car_fuel_bills modify delete_cause varchar2(300);  
alter table car_instrument_reading modify create_remarks varchar2(300);  
alter table car_instrument_reading modify delete_cause varchar2(300);  
