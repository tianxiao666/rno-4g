--------------------------------------------------------
--  DDL for Procedure PRO_GETTERMINALLOC
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_GETTERMINALLOC" (startDate in DATE , endDate in DATE ,tId in number,gpsmileage out FLOAT)
as
       newLat FLOAT;
	     newLng FLOAT;
	     oldLat FLOAT;
	     oldLng FLOAT ;
	     jd FLOAT;
	     wd FLOAT;
	     finish INT;
	     GISmileage FLOAT;
begin
       declare
       --类型定义
       cursor c_job
       is SELECT
																	jingdu ,
weidu
																FROM

mobile_gps_location
																WHERE
																	pickTime
> startDate
																	AND
pickTime < endDate
																	AND
clientId = tId;
c_row c_job%rowtype;
begin
  for c_row in c_job loop
       newLng := c_row.jingdu;
			 newLat := c_row.weidu;
				IF oldLng IS NOT NULL THEN
					pro_GetDistance(oldLat , oldLng , newLat , newLng, GISmileage);
					GISmileage := GISmileage + GISmileage;
				END IF;
				oldLng := c_row.jingdu;
				oldLat := c_row.weidu;
  END LOOP;

end;
gpsmileage := GISmileage;
end;

/
