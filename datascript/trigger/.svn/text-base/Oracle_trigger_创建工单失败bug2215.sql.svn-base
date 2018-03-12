create or replace trigger "TRIG_WM_WO_RESOURCE_ASS_ADD" 
after insert
on WM_WO_RESOURCE_ASSOCIATE
for each row 
declare 
	v_resourceType VARCHAR2(256):=:new.NETWORKRESOURCETYPE;
	v_resourceId number:=:new.NETWORKRESOURCEID;
	v_stationId number:=:new.STATIONID;
	v_stationType varchar(256):='Station';
	v_count number;
	v_count2 number;
	v_count3 number;
begin

	if v_resourceType='car' THEN

		select count(*) into v_count from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_resourceType AND RESOURCEID=to_char(v_resourceId));

		if v_count>0 then 
				update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT+1 where RESOURCETYPE=v_resourceType and RESOURCEID=to_char(v_resourceId);
		else
				insert into WM_COUNT_WORKORDER_OBJECT(ID,RESOURCEID,RESOURCETYPE,TASKCOUNT) values(SEQ_WM_COUNT_WORKORDER_OBJECT.nextval,v_resourceId,v_resourceType,1);
		end if;
			
	else
			select count(*) into v_count2 from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_stationType AND RESOURCEID=to_char(v_stationId));

			if v_count2>0 then 
					update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT+1 where RESOURCETYPE=v_stationType and RESOURCEID=to_char(v_stationId);
			else
					insert into WM_COUNT_WORKORDER_OBJECT(ID,RESOURCEID,RESOURCETYPE,TASKCOUNT) values(SEQ_WM_COUNT_WORKORDER_OBJECT.nextval,v_stationId,v_stationType,1);
			end if;

			
			select count(*) into v_count3 from (select 1 from WM_COUNT_WORKORDER_OBJECT where RESOURCETYPE=v_resourceType AND RESOURCEID=to_char(v_resourceId));
			if v_count3>0 then 
					update WM_COUNT_WORKORDER_OBJECT set TASKCOUNT=TASKCOUNT+1 where RESOURCETYPE=v_resourceType and RESOURCEID=to_char(v_resourceId);
			else
					insert into WM_COUNT_WORKORDER_OBJECT(ID,RESOURCEID,RESOURCETYPE,TASKCOUNT) values(SEQ_WM_COUNT_WORKORDER_OBJECT.nextval,v_resourceId,v_resourceType,1);
			end if;

	end if;
end;
