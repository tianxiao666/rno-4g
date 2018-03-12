--------------------------------------------------------
--  DDL for Trigger TRIG_WM_WO_RESOURCE_ASS_DEL
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRIG_WM_WO_RESOURCE_ASS_DEL" 
after delete
on WM_WO_RESOURCE_ASSOCIATE
for each row 
declare 
	seq_num2 number;
	v_resourceType VARCHAR2(256):=:old.NETWORKRESOURCETYPE;
	v_resourceId number:=:old.NETWORKRESOURCEID;
	v_stationId number:=:old.STATIONID;
	v_stationType varchar(256):='Station';
	v_count number;
	v_count2 number;
	v_count3 number;
begin

	select SEQ_WM_COUNT_WORKORDER_OBJECT.nextval into seq_num2 from dual;

	if v_resourceType='car' THEN

		select count(*) into v_count from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_resourceType AND RESOURCEID=to_char(v_resourceId));

		if v_count>0 then 
				update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT-1 where RESOURCETYPE=v_resourceType and RESOURCEID=to_char(v_resourceId);
		end if;

	else
			select count(*) into v_count2 from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_stationType AND RESOURCEID=to_char(v_stationId));
			if v_count2>0 then 
					update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT-1 where RESOURCETYPE=v_stationType and RESOURCEID=to_char(v_stationId);
			end if;

			select count(*) into v_count3 from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_resourceType AND RESOURCEID=to_char(v_resourceId));
			if v_count3>0 then 
					update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT-1 where RESOURCETYPE=v_resourceType and RESOURCEID=to_char(v_resourceId);
			end if;

	end if;
end;
/