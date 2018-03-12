--------------------------------------------------------
--  DDL for Procedure PRO_REPORT_ROUTINEINSPECTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_REPORT_ROUTINEINSPECTION" 
as
   routineinspectionWoId VARCHAR2(1024);

   planTitle VARCHAR2(1024);
   planStartTime DATE;
   planEndTime DATE;
   remark VARCHAR2(1024);

   orgId number(12);
   templateId number(12);
   "type" number(12,0);
   routineInspectionProfession VARCHAR2(256);
   VIPCOUNT number(12);
   progress number(12,2);
   actualProgress number(12,2);
   averageDeviationDistance number(12,2);
   timelyRate number(12,2);
   status number(12);
   updateOrAdd number(12) DEFAULT 0;
   taskCompleteCount INT DEFAULT 0;
   offCount INT DEFAULT 0;
   totalCount INT DEFAULT 0;
   taskageDeviationDistance number(12,2) DEFAULT 0;
   taskorderCount INT DEFAULT 0;
   taskorderoffCount INT DEFAULT 0;
   dataCount INT DEFAULT 0;
BEGIN
  declare
       --类型定义
       cursor c_job
       is
       SELECT
        rp.routineinspectionWoId,
        rp.planTitle,
        rp.planStartTime,
        rp.planEndTime,
        rp.remark,
        rp.orgId,
        rp.templateId,
        rp.type,
        rp.routineInspectionProfession,
        rp.VIPCOUNT,
	      vrp."status"
         FROM
        insp_planworkorder rp,
        v_wm_insp_workorder  vrp
        WHERE
        rp.routineinspectionWoId = vrp."woId";
       c_row c_job%rowtype;
begin
       for c_row in c_job loop
         progress := ((to_number(sysdate-c_row.planStartTime)*24*60)/(to_number(c_row.planEndTime-c_row.planStartTime)*24*60))*100;
         IF progress > 100 THEN
						progress := 100;
				END IF;
         SELECT COUNT(*) into offCount FROM  v_wm_insp_taskorder WHERE  v_wm_insp_taskorder."woId"  = c_row.routineinspectionWoId AND  v_wm_insp_taskorder."status" = '24';
         SELECT COUNT(*) into totalCount FROM  INSP_TASKORDER WHERE  INSP_TASKORDER.ROUTINEINSPECTIONTOID  = c_row.routineinspectionWoId;
        IF totalCount != 0 THEN
           actualProgress := (offCount/totalCount)*100;
        ELSE
           actualProgress := 0;
        END IF;
        IF actualProgress > 100 THEN
										actualProgress := 100;
				END IF;
        SELECT SUM(insp_taskorder.deviate) into taskageDeviationDistance FROM insp_taskorder WHERE insp_taskorder.routineinspectionWoId  = c_row.routineinspectionWoId;
        SELECT COUNT(*) into taskorderCount FROM insp_taskorder WHERE  insp_taskorder.routineinspectionWoId  = c_row.routineinspectionWoId;
        averageDeviationDistance := (taskageDeviationDistance/taskorderCount);
        SELECT COUNT(*) into taskorderoffCount FROM insp_taskorder WHERE insp_taskorder.routineinspectionWoId  = c_row.routineinspectionWoId AND insp_taskorder.signOutTime < insp_taskorder.taskPlanEndTime AND insp_taskorder.signOutTime IS NOT NULL;
        timelyRate := (taskorderoffCount/taskorderCount);
        SELECT COUNT(*) into updateOrAdd FROM report_routineinspection  rrp  WHERE  rrp.routineinspectionWoId = c_row.routineinspectionWoId;
        IF updateOrAdd > 0 THEN
                  UPDATE report_routineinspection  rrp
                    SET
                    rrp.routineinspectionWoId = c_row.routineinspectionWoId ,
                    rrp.planTitle = c_row.planTitle ,
                    rrp.planStartTime = c_row.planStartTime ,
                    rrp.planEndTime = c_row.planEndTime ,
                    rrp.remark = c_row.remark ,
                    rrp.orgId = c_row.orgId ,
                    rrp.templateId = c_row.templateId,
                    rrp.TYPE = c_row.TYPE ,
                    rrp.routineInspectionProfession = c_row.routineInspectionProfession ,
                    rrp.VIPCOUNT = c_row.VIPCOUNT ,
                    rrp.progress = progress ,
                    rrp.actualProgress = actualProgress ,
                    rrp.averageDeviationDistance = averageDeviationDistance ,
                    rrp.timelyRate = timelyRate ,
                    rrp.status = c_row."status"

                    WHERE
                    rrp.routineinspectionWoId = c_row.routineinspectionWoId;
                    updateOrAdd := 0;
                ELSE
                  INSERT INTO report_routineinspection rradd
                    (rradd.routineinspectionWoId,
                    rradd.planTitle,
                    rradd.planStartTime,
                    rradd.planEndTime,
                    rradd.remark,
                    rradd.orgId,
                    rradd.templateId,
                    rradd.TYPE,
                    rradd.routineInspectionProfession,
                    rradd.VIPCOUNT,
                    rradd.progress,
                    rradd.actualProgress,
                    rradd.averageDeviationDistance,
                    rradd.timelyRate ,
                    rradd.STATUS
                    )
                    VALUES
                    (c_row.routineinspectionWoId,
                    c_row.planTitle,
                    c_row.planStartTime,
                    c_row.planEndTime,
                    c_row.remark,
                    c_row.orgId,
                    c_row.templateId,
                    c_row.TYPE,
                    c_row.routineInspectionProfession,
                    c_row.VIPCOUNT,
                    progress,
                    actualProgress,
                    averageDeviationDistance,
                    timelyRate ,
                    c_row."status"
                    );
                    updateOrAdd := 0;
                END IF;
        end loop;
end;
END;

/