--------------------------------------------------------
--  DDL for Procedure PRO_GETCARMILEAGE_MONTHRUN
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_GETCARMILEAGE_MONTHRUN" 
as
   tId number ;
   wId VARCHAR2(200);
   beginTime DATE;
   endTime DATE;
   done INT DEFAULT 0;
   GISmileage Float;

	 num INT DEFAULT 0;
begin
     declare
       --类型定义
       cursor c_job
       is
       SELECT

          woId , realUseCarTime , realReturnCarTime , terminal.id AS terminalId
                                          FROM

          car_workorder wo

          INNER JOIN car_cardriverpair cdpair ON cdpair.id = wo.carDriverPairId

          INNER JOIN car_car car ON car.id = cdpair.car_id

          INNER JOIN car_carterminalpair ctpair ON ctpair.car_id = car.id

          INNER JOIN car_terminal terminal ON terminal.id = ctpair.terminal_id
          WHERE to_char(realReturnCarTime,'YYYY-MM') = to_char(sysdate,'YYYY-MM');
     c_row c_job%rowtype;
begin
  for c_row in c_job loop
          pro_getTerminalLoc(c_row.realUseCarTime , c_row.realReturnCarTime , c_row.terminalId , GISmileage );
          GISmileage := round(GISmileage / 1000,2);
					UPDATE car_workorder SET gpsmileage = GISmileage WHERE woId = c_row.woId;
  END LOOP;

end;
end;

/
