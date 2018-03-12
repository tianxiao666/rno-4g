--------------------------------------------------------
--  DDL for Procedure PRO_REPORT_GETURGENCYREPAIR
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_REPORT_GETURGENCYREPAIR" 
as 
   id number(12); 
   WOID VARCHAR2(128); 
   WOTITLE VARCHAR2(1024); 
   CREATETIME DATE; 
   faultOccuredTime DATE; 
   latestAllowedTime DATE; 
   workOrderAcceptedTime DATE; 
   alarmClearTime DATE; 
   LASTEDITTIME DATE; 
   BIZUNITINSTID number(12); 
   CREATEPERSON VARCHAR2(128); 
   ISOVERTIME number(12); 
   tbcSTATUS number(12); 
   sideeffectService VARCHAR2(255); 
   faultInWhichProvince VARCHAR2(255); 
   faultInWhichCity VARCHAR2(255); 
   faultInWhichCountry VARCHAR2(255); 
   faultInWhichTown VARCHAR2(255); 
   faultType VARCHAR2(255); 
   faultLevel VARCHAR2(255); 
   faultArea VARCHAR2(255); 
   workAcceptedTime FLOAT; 
   troubleshootingTime FLOAT; 
   workOrderProcessingTime FLOAT; 
   AcceptedTimeRate INT; 
   ProcessTimeRate INT; 
   CompletionRate INT; 
   faultCause VARCHAR2(255); 
   baseStationLevel VARCHAR2(255); 
   acceptProfessional VARCHAR2(255); 
   faultGenera VARCHAR(255); 
   done INT DEFAULT 0; 
   networkResourceId INT DEFAULT 0;
   networkResourceType VARCHAR2(255);
BEGIN
  declare
       --类型定义
       cursor c_job
       is SELECT 
          tbc.id,
          tbc.WOID,
          tbc.WOTITLE,
          tbc.CREATETIME,
          urs.faultOccuredTime,
          urs.latestAllowedTime,
          urs.workOrderAcceptedTime,
          urs.alarmClearTime,
          tbc.finalCompleteTime,
          tbc.creatorOrgId,
          tbc.creator,
          tbc.ISOVERTIME,
          tbc.STATUS,
          urs.sideeffectService,
          urs.faultInWhichProvince,
          urs.faultInWhichCity,
          urs.faultInWhichCountry,
          urs.faultInWhichTown,
          urs.faultType,
          urs.faultLevel,
          urs.faultArea,
          urs.faultCause,
          urs.baseStationLevel,
          urs.acceptProfessional,
          urs.faultGenera,
          wwt.networkResourceId,
          wwt.networkResourceType
         FROM 
        wm_workorder  tbc,
        repair_workorder  urs,
        wm_workorderassnetresource  wwt
        WHERE tbc.WOID = urs.woId  AND wwt.woId = tbc.woId
        AND tbc.STATUS = 7
        AND urs.latestAllowedTime >= TRUNC(SYSDATE)
        AND urs.latestAllowedTime < TRUNC(SYSDATE)+1;
  c_row c_job%rowtype;
begin  
  for c_row in c_job loop

            workAcceptedTime := (to_number(c_row.workOrderAcceptedTime-c_row.CREATETIME)*24*60);
            troubleshootingTime := (to_number(c_row.alarmClearTime-c_row.faultOccuredTime)*24*60);
            workOrderProcessingTime := (to_number(c_row.finalCompleteTime-c_row.CREATETIME)*24*60);
            AcceptedTimeRate := (to_number(c_row.latestAllowedTime-c_row.workOrderAcceptedTime)*24*60*60);
            IF AcceptedTimeRate >= 0 THEN  
              AcceptedTimeRate := 1;
            ELSE 
              AcceptedTimeRate := 0;
            END IF; 
            ProcessTimeRate := (to_number(c_row.latestAllowedTime-c_row.finalCompleteTime)*24*60*60); 
            IF ProcessTimeRate >= 0 THEN  
              ProcessTimeRate := 1;
            ELSE  
              ProcessTimeRate := 0;
            END IF; 

            IF to_char(latestAllowedTime,'yyyy-mm')=to_char(sysdate,'yyyy-mm') THEN  
              IF tbcSTATUS = 3 THEN 
              CompletionRate := 1;
              ELSE  
              CompletionRate := 0;
              END IF;          
            END IF;

            INSERT INTO report_urgencyrepair ru
              (ru.id, 
              ru.WOID, 
              ru.WOTITLE, 
              ru.CREATETIME, 
              ru.faultOccuredTime, 
              ru.latestAllowedTime, 
              ru.workOrderAcceptedTime, 
              ru.alarmClearTime, 
              ru.LASTEDITTIME, 
              ru.organizationId, 
              ru.CREATEPERSON,
              ru.ISOVERTIME, 
              ru.STATUS, 
              ru.sideeffectService, 
              ru.faultInWhichProvince, 
              ru.faultInWhichCity, 
              ru.faultInWhichCountry, 
              ru.faultInWhichTown, 
              ru.faultType, 
              ru.faultLevel, 
              ru.faultArea,
              ru.workAcceptedTime, 
              ru.troudleshootingTime, 
              ru.workOrderProcessingTime, 
              ru.AcceptedTimeRate, 
              ru.ProcessTimeRate, 
              ru.CompletionRate,
              ru.faultCause,
              ru.baseStationLevel,
              ru.acceptProfessional,
              ru.faultGenera, 
              ru.networkResourceId, 
              ru.networkResourceType
              )
              VALUES(
              SEQ_REPORT_URGENCYREPAIR.NEXTVAL,
              c_row.WOID,
              c_row.WOTITLE,
              c_row.CREATETIME,
              c_row.faultOccuredTime,
              c_row.latestAllowedTime,
              c_row.workOrderAcceptedTime,
              c_row.alarmClearTime,
              c_row.finalCompleteTime,
              c_row.creatorOrgId,
              c_row.creator,
              c_row.ISOVERTIME,
              c_row.STATUS,
              c_row.sideeffectService,
              c_row.faultInWhichProvince,
              c_row.faultInWhichCity,
              c_row.faultInWhichCountry,
              c_row.faultInWhichTown,
              c_row.faultType,
              c_row.faultLevel,
              c_row.faultArea,
              workAcceptedTime,
              troubleshootingTime,
              workOrderProcessingTime,
              AcceptedTimeRate, 
              ProcessTimeRate, 
              CompletionRate,
              c_row.faultCause,
              c_row.baseStationLevel,
              c_row.acceptProfessional,
              c_row.faultGenera, 
              c_row.networkResourceId, 
              c_row.networkResourceType
              );

      END LOOP;

end;
END;

/

