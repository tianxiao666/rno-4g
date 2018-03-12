--------------------------------------------------------
--  DDL for Procedure PRO_CREATE_TABLE_SEQ
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_CREATE_TABLE_SEQ" (tableName in varchar2 ,idName in varchar2)
Authid Current_User  as
  idCount int Default 0;
  seqString varchar2(1024);
  sqlString varchar2(1024);

begin

  sqlString := 'select max('||idName||') idCount from '||tableName;
DBMS_OUTPUT.PUT_LINE(sqlString);
EXECUTE IMMEDIATE sqlString into idCount; 
DBMS_OUTPUT.PUT_LINE('idCount'||idCount);
IF idCount <= 0 or idCount is null or to_char(idCount) = '' or to_char(idCount) = ' ' THEN  
   idCount := 1;
else
   idCount := idCount + 20000;
end if;
seqString := 'create sequence SEQ_'||tableName||' start with '||idCount||' increment by 1';
DBMS_OUTPUT.PUT_LINE(seqString);
EXECUTE IMMEDIATE seqString;
DBMS_OUTPUT.PUT_LINE(idCount);
end pro_create_table_seq;

/