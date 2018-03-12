--------------------------------------------------------
--  DDL for View V_BASESTATION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BASESTATION" ("BASESTATIONID", "BASESTATIONNAME", "STATIONID", "STATIONNAME", "AREAID", "BASESTATIONTYPE") AS 
  SELECT bs.id basestationId,bs.name basestationName,station.id stationid,station.name stationName,AREA.id areaid ,bs.entity_type basestationType FROM basestation bs,figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station,
figurenode p3_fn,AREA
WHERE bs.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND .id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND station.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = Area.entity_id
UNION
SELECT bs.id basestationId,bs.name basestationName,station.id stationid,station.name stationName,AREA.id areaid ,bs.entity_type basestationType FROM BaseStation_GSM bs,figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station,
figurenode p3_fn,AREA
WHERE bs.entity_id = fn.entityId ANDp1_fn fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND p1_fn.id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND station.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = Area.entity_id
UNION
SELECT bs.id basestationId,bs.name basestationName,station.id stationid,station.name stationName,AREA.id areaid ,bs.entity_type basestationType FROM BaseStation_repeater bs,figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station,
figurenode p3_fn,AREA
WHERE bs.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND p1_fn.id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND station.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = Area.entity_id
UNION
SELECT bs.id basestationId,bs.name basestationName,station.id stationid,station.name stationName,AREA.id areaid ,bs.entity_type basestationType FROM BaseStation_TD bs,figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station,
figurenode p3_fn,AREA
WHERE bs.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND p1_fn.id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND station.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = Area.entity_id
UNION
SELECT bs.id basestationId,bs.name basestationName,station.id stationid,station.name stationName,AREA.id areaid ,bs.entity_type basestationType FROM BaseStation_WLAN bs,figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station,
figurenode p3_fn,AREA
WHERE bs.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND p1_fn.id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND station.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = Area.entity_id

;
--------------------------------------------------------
--  DDL for View V_CELL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CELL" ("CELLID", "CELLNAME", "CELLTYPE", "STATIONID", "STATIONNAME", "AREAID", "AREANAME") AS 
  SELECT bs.id cellid,bs.name cellName,bs.entity_type cellType,st.id stationid,st.id stationName,ar.id areaid,ar.name areaName FROM cell bs,
figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
figurenode p1_fn,figureline p2_fl,
figurenode p2_fn,figureline p3_fl,station st,
figurenode p3_fn,AREA ar
WHERE bs.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.rightId AND p4_fl.linkType='CLAN'
AND p4_fl.leftId = fn.id AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p1_fn.id AND p1_fn.id = p2_fl.rightId AND p2_fl.linkType='CLAN'
AND p2_fl.leftId = p2_fn.id AND p2_fn.id = p3_fl.rightId AND p3_fl.linkType='CLAN' AND st.entity_id = p2_fn.entityId
AND p3_fl.leftId =p3_fn.id AND p3_fn.entityId = ar.entity_id;
--------------------------------------------------------
--  DDL for View V_FIBERCOSSCBINET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FIBERCOSSCBINET" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
fibercrosscabinet  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_FIBERDISTRIBUTIONCABINET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FIBERDISTRIBUTIONCABINET" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
fiberdistributioncabinet  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_FIBERTERMINALCASE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FIBERTERMINALCASE" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
fiberdistributioncabinet  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_HANGWALL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_HANGWALL" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
hangwall  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_INSP_QUESTION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INSP_QUESTION" ("id", "seriousLevel", "questionType", "description", "questionPicture", "resourceType", "resourceId", "creator", "creatorName", "creatorOrgId", "createTime", "handler", "handlerName", "handleTime", "handleResult", "handlePicture", "isOver", "resourceName", "creatorOrgName") AS 
  select iq.id as "id",iq.seriouslevel as "seriousLevel",iq.questiontype as "questionType", iq.description as "description",
  iq.questionpicture as "questionPicture" ,iq.resourcetype as "resourceType" ,iq.resourceid as "resourceId" ,iq.creator as "creator",
  iq.creatorname as "creatorName",iq.creatororgid as "creatorOrgId" , iq.createtime as "createTime" ,iq.handler as "handler",
  iq.handlername as "handlerName",iq.handletime as "handleTime" , iq.handleresult as "handleResult",iq.handlepicture as "handlePicture",
  iq.isover as "isOver" , iq.resourcename as "resourceName"  , iq.creatororgname as "creatorOrgName"
    from INSP_QUESTION iq;
--------------------------------------------------------
--  DDL for View V_MANWELL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANWELL" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
manwell  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_MARKPOST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MARKPOST" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
markpost  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_POLE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_POLE" ("AREAID", "RESID", "RESNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
area.id AS areaId  , 
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.longitude AS longitude,
re.latitude AS latitude
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
pole  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_ROOM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ROOM" ("AREAID", "AREANAME", "STATIONID", "STATIONNAME", "ROOMID", "ROOMNAME", "ROOMTYPE", "RESID", "RESNAME", "RESTYPE") AS 
  SELECT 
  area.id AS areaId  , area.name AS areaName
  ,re.id AS stationId , re.name AS stationName
  ,room.id AS roomId , room.name AS roomName
  ,room.entity_type AS roomType
  ,room.id AS resId , room.name AS resName 
  ,room.entity_type AS resType
 FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
station  re,
figurenode p2_fn,figureline p2_fl,
room
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId
AND room.entity_id = p2_fn.entityId AND p2_fl.rightId = p2_fn.id AND fn.id = p2_fl.leftId;
--------------------------------------------------------
--  DDL for View V_STATION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STATION" ("AREAID", "AREANAME", "RESID", "RESNAME", "RESTYPE", "STATIONID", "STATIONNAME", "STATIONTYPE") AS 
  SELECT 
area.id AS areaId  , 
area.name AS areaName ,
re.id AS resId ,
re.name AS resName ,
re.entity_type AS resType,
re.id AS stationId ,
re.name AS stationName ,
re.entity_type AS stationType
FROM 
AREA,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
station  re
WHERE area.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_STATION_ROOM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STATION_ROOM" ("STATIONID", "STATIONNAME", "RESID", "RESNAME", "ROOMID", "ROOMNAME", "RESTYPE", "LONGITUDE", "LATITUDE") AS 
  SELECT 
st.id AS stationId  , 
st.name AS stationName ,
re.id AS resId ,
re.name AS resName ,
re.id AS roomId ,
re.name AS roomName ,
re.entity_type AS resType,
st.longitude AS longitude,
st.latitude AS latitude
FROM 
station st,figurenode p4_fn,figureline p4_fl,
figurenode fn,figureline p1_fl,
room re
WHERE st.entity_id = p4_fn.entityId AND p4_fn.id = p4_fl.leftId AND p4_fl.linkType='CLAN'
AND re.entity_id = fn.entityId AND fn.id = p1_fl.rightId AND p1_fl.linkType='CLAN'
AND p1_fl.leftId = p4_fn.id AND fn.id = p4_fl.rightId ;
--------------------------------------------------------
--  DDL for View V_WM_CAR_TRACEWORKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_CAR_TRACEWORKORDER" ("id", "draftId", "processDefineId", "processInstId", "woId", "woType", "woTitle", "status", "currentTaskId", "currentTaskName", "creator", "creatorName", "creatorOrgId", "createTime", "isOverTime", "requireCompleteTime", "acceptComment", "finalCompleteTime", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "acceptPeople", "acceptPeopleName", "lastReplyPeople", "lastReplyPeopleName", "isRead", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "planUseCarAddress", "useCarPersonId", "useCarPersonName", "planUseCarTime", "criticalClass", "recordHandler") AS 
  SELECT wm_workorder.id AS "id", wm_workorder.draftId AS "draftId", wm_workorder.processDefineId AS "processDefineId", wm_workorder.processInstId AS "processInstId", wm_workorder.woId AS "woId", wm_workorder.woType AS "woType", wm_workorder.woTitle AS "woTitle", wm_workorder. STATUS AS "status", wm_workorder.currentTaskId AS "currentTaskId", wm_workorder.currentTaskName AS "currentTaskName", wm_workorder.creator AS "creator", wm_workorder.creatorName AS "creatorName", wm_workorder.creatorOrgId AS "creatorOrgId", wm_workorder.createTime AS "createTime", wm_workorder.isOverTime AS "isOverTime", wm_workorder.requireCompleteTime AS "requireCompleteTime", wm_workorder.acceptComment AS "acceptComment", wm_workorder.finalCompleteTime AS "finalCompleteTime", wm_workorder.currentHandler AS "currentHandler", wm_workorder.currentHandlerName AS "currentHandlerName", wm_workorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_workorder.acceptPeople AS "acceptPeople", wm_workorder.acceptPeopleName AS "acceptPeopleName", wm_workorder.lastReplyPeople AS "lastReplyPeople", wm_workorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_workorder.isRead AS "isRead", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", car_workorder.planUseCarAddress AS "planUseCarAddress", car_workorder.useCarPersonId AS "useCarPersonId", car_workorder.useCarPersonName AS "useCarPersonName", car_workorder.planUseCarTime AS "planUseCarTime", car_workorder.criticalClass AS "criticalClass", tasktracerecord. HANDLER AS "recordHandler" FROM wm_workorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, car_workorder, tasktracerecord WHERE (( wm_workorder. STATUS = wm_statusreg.id ) AND ( wm_workorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_workorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName ) AND ( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_bizprocess_conf.bizFlag = 'CL' ) AND ( wm_workorder.woId = car_workorder.woId ) AND ( wm_workorder.woId = tasktracerecord.woId ));
--------------------------------------------------------
--  DDL for View V_WM_CAR_WORKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_CAR_WORKORDER" ("id", "draftId", "processDefineId", "processInstId", "woId", "woType", "woTitle", "status", "currentTaskId", "currentTaskName", "creator", "creatorName", "creatorOrgId", "createTime", "isOverTime", "requireCompleteTime", "acceptComment", "finalCompleteTime", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "acceptPeople", "acceptPeopleName", "lastReplyPeople", "lastReplyPeopleName", "isRead", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "planUseCarAddress", "useCarPersonId", "useCarPersonName", "planUseCarTime", "criticalClass") AS 
  SELECT wm_workorder.id AS "id", wm_workorder.draftId AS "draftId", wm_workorder.processDefineId AS "processDefineId", wm_workorder.processInstId AS "processInstId", wm_workorder.woId AS "woId", wm_workorder.woType AS "woType", wm_workorder.woTitle AS "woTitle", wm_workorder. STATUS AS "status", wm_workorder.currentTaskId AS "currentTaskId", wm_workorder.currentTaskName AS "currentTaskName", wm_workorder.creator AS "creator", wm_workorder.creatorName AS "creatorName", wm_workorder.creatorOrgId AS "creatorOrgId", wm_workorder.createTime AS "createTime", wm_workorder.isOverTime AS "isOverTime", wm_workorder.requireCompleteTime AS "requireCompleteTime", wm_workorder.acceptComment AS "acceptComment", wm_workorder.finalCompleteTime AS "finalCompleteTime", wm_workorder.currentHandler AS "currentHandler", wm_workorder.currentHandlerName AS "currentHandlerName", wm_workorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_workorder.acceptPeople AS "acceptPeople", wm_workorder.acceptPeopleName AS "acceptPeopleName", wm_workorder.lastReplyPeople AS "lastReplyPeople", wm_workorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_workorder.isRead AS "isRead", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", car_workorder.planUseCarAddress AS "planUseCarAddress", car_workorder.useCarPersonId AS "useCarPersonId", car_workorder.useCarPersonName AS "useCarPersonName", car_workorder.planUseCarTime AS "planUseCarTime", car_workorder.criticalClass AS "criticalClass" FROM wm_workorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, car_workorder WHERE (( wm_workorder. STATUS = wm_statusreg.id ) AND ( wm_workorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_workorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName ) AND ( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_bizprocess_conf.bizFlag = 'CL' ) AND ( wm_workorder.woId = car_workorder.woId ));
--------------------------------------------------------
--  DDL for View V_WM_INSP_PENDINGTASKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_INSP_PENDINGTASKORDER" ("id", "processDefineId", "processInstId", "toId", "woId", "parentBizOrderId", "status", "currentTaskId", "currentTaskName", "toTitle", "toDesc", "toType", "assigner", "assignerName", "assignerOrgId", "assignComment", "assignTime", "requireCompleteTime", "lastReplyPeople", "lastReplyPeopleName", "finalCompleteTime", "acceptPeople", "acceptPeopleName", "acceptTime", "acceptComment", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "rejectTime", "rejectComment", "cancelTime", "cancelComment", "reassignTime", "reassignComment", "isOverTime", "isRead", "isSendWillOverTime", "orderOwner", "orderOwnerOrgId", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "planTitle", "type", "orgId", "orgName", "taskPlanBeginTime", "taskPlanEndTime", "longitude", "latitude", "signInTime", "signOutTime", "resourceId", "resourceName", "deviate") AS 
  SELECT DISTINCT wm_taskorder. ID AS "id", wm_taskorder.processDefineId AS "processDefineId", wm_taskorder.processInstId AS "processInstId", wm_taskorder.toId AS "toId", wm_taskorder.woId AS "woId", wm_taskorder.parentBizOrderId AS "parentBizOrderId", wm_taskorder.STATUS AS "status", wm_taskorder.currentTaskId AS "currentTaskId", wm_taskorder.currentTaskName AS "currentTaskName", wm_taskorder.toTitle AS "toTitle", wm_taskorder.toDesc AS "toDesc", wm_taskorder.toType AS "toType", wm_taskorder.assigner AS "assigner", wm_taskorder.assignerName AS "assignerName", wm_taskorder.assignerOrgId AS "assignerOrgId", TO_CHAR(wm_taskorder.assignComment) AS "assignComment", wm_taskorder.assignTime AS "assignTime", wm_taskorder.requireCompleteTime AS "requireCompleteTime", wm_taskorder.lastReplyPeople AS "lastReplyPeople", wm_taskorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_taskorder.finalCompleteTime AS "finalCompleteTime", wm_taskorder.acceptPeople AS "acceptPeople", wm_taskorder.acceptPeopleName AS "acceptPeopleName", wm_taskorder.acceptTime AS "acceptTime", wm_taskorder.acceptComment AS "acceptComment", wm_taskorder.currentHandler AS "currentHandler", wm_taskorder.currentHandlerName AS "currentHandlerName", wm_taskorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_taskorder.rejectTime AS "rejectTime", wm_taskorder.rejectComment AS "rejectComment", wm_taskorder.cancelTime AS "cancelTime", wm_taskorder.cancelComment AS "cancelComment", wm_taskorder.reassignTime AS "reassignTime", wm_taskorder.reassignComment AS "reassignComment", wm_taskorder.isOverTime AS "isOverTime", wm_taskorder.isRead AS "isRead", wm_taskorder.isSendWillOverTime AS "isSendWillOverTime", wm_taskorder.orderOwner AS "orderOwner", wm_taskorder.orderOwnerOrgId AS "orderOwnerOrgId", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", insp_planworkorder.planTitle AS "planTitle", insp_planworkorder. TYPE AS "type", insp_taskorder.orgId AS "orgId", insp_taskorder.orgName AS "orgName", insp_taskorder.taskPlanBeginTime AS "taskPlanBeginTime", insp_taskorder.taskPlanEndTime AS "taskPlanEndTime", insp_taskorder.longitude AS "longitude", insp_taskorder.latitude AS "latitude", insp_taskorder.signInTime AS "signInTime", insp_taskorder.signOutTime AS "signOutTime", insp_taskorder.resourceId AS "resourceId", insp_taskorder.resourceName AS "resourceName", insp_taskorder.deviate AS "deviate" FROM wm_taskorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, insp_planworkorder, insp_taskorder, flow_instance WHERE(( wm_taskorder.STATUS = wm_statusreg. ID) AND( wm_taskorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_taskorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName) AND( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_bizprocess_conf.bizFlag = 'XJ') AND( wm_taskorder.woId = insp_planworkorder.routineinspectionWoId) AND( wm_taskorder.toId = insp_taskorder.routineinspectionToId) AND( wm_taskorder.processInstId = flow_instance.instance_id) AND( flow_instance.current_activity_name <> 'ended') AND(wm_taskorder.STATUS <> 26));
--------------------------------------------------------
--  DDL for View V_WM_INSP_TASKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_INSP_TASKORDER" ("id", "processDefineId", "processInstId", "toId", "woId", "parentBizOrderId", "status", "currentTaskId", "currentTaskName", "toTitle", "toDesc", "toType", "assigner", "assignerName", "assignerOrgId", "assignComment", "assignTime", "requireCompleteTime", "lastReplyPeople", "lastReplyPeopleName", "finalCompleteTime", "acceptPeople", "acceptPeopleName", "acceptTime", "acceptComment", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "rejectTime", "rejectComment", "cancelTime", "cancelComment", "reassignTime", "reassignComment", "isOverTime", "isRead", "isSendWillOverTime", "orderOwner", "orderOwnerOrgId", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "planTitle", "type", "orgId", "orgName", "taskPlanBeginTime", "taskPlanEndTime", "longitude", "latitude", "signInTime", "signOutTime", "resourceId", "resourceName", "deviate") AS 
  SELECT DISTINCT wm_taskorder. ID AS "id", wm_taskorder.processDefineId AS "processDefineId", wm_taskorder.processInstId AS "processInstId", wm_taskorder.toId AS "toId", wm_taskorder.woId AS "woId", wm_taskorder.parentBizOrderId AS "parentBizOrderId", wm_taskorder.STATUS AS "status", wm_taskorder.currentTaskId AS "currentTaskId", wm_taskorder.currentTaskName AS "currentTaskName", wm_taskorder.toTitle AS "toTitle", wm_taskorder.toDesc AS "toDesc", wm_taskorder.toType AS "toType", wm_taskorder.assigner AS "assigner", wm_taskorder.assignerName AS "assignerName", wm_taskorder.assignerOrgId AS "assignerOrgId", TO_CHAR(wm_taskorder.assignComment) AS "assignComment", wm_taskorder.assignTime AS "assignTime", wm_taskorder.requireCompleteTime AS "requireCompleteTime", wm_taskorder.lastReplyPeople AS "lastReplyPeople", wm_taskorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_taskorder.finalCompleteTime AS "finalCompleteTime", wm_taskorder.acceptPeople AS "acceptPeople", wm_taskorder.acceptPeopleName AS "acceptPeopleName", wm_taskorder.acceptTime AS "acceptTime", wm_taskorder.acceptComment AS "acceptComment", wm_taskorder.currentHandler AS "currentHandler", wm_taskorder.currentHandlerName AS "currentHandlerName", wm_taskorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_taskorder.rejectTime AS "rejectTime", wm_taskorder.rejectComment AS "rejectComment", wm_taskorder.cancelTime AS "cancelTime", wm_taskorder.cancelComment AS "cancelComment", wm_taskorder.reassignTime AS "reassignTime", wm_taskorder.reassignComment AS "reassignComment", wm_taskorder.isOverTime AS "isOverTime", wm_taskorder.isRead AS "isRead", wm_taskorder.isSendWillOverTime AS "isSendWillOverTime", wm_taskorder.orderOwner AS "orderOwner", wm_taskorder.orderOwnerOrgId AS "orderOwnerOrgId", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", insp_planworkorder.planTitle AS "planTitle", insp_planworkorder. TYPE AS "type", insp_taskorder.orgId AS "orgId", insp_taskorder.orgName AS "orgName", insp_taskorder.taskPlanBeginTime AS "taskPlanBeginTime", insp_taskorder.taskPlanEndTime AS "taskPlanEndTime", insp_taskorder.longitude AS "longitude", insp_taskorder.latitude AS "latitude", insp_taskorder.signInTime AS "signInTime", insp_taskorder.signOutTime AS "signOutTime", insp_taskorder.resourceId AS "resourceId", insp_taskorder.resourceName AS "resourceName", insp_taskorder.deviate AS "deviate" FROM wm_taskorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, insp_planworkorder, insp_taskorder, flow_instance WHERE(( wm_taskorder.STATUS = wm_statusreg. ID) AND( wm_taskorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_taskorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName) AND( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_bizprocess_conf.bizFlag = 'XJ') AND( wm_taskorder.woId = insp_planworkorder.routineinspectionWoId) AND( wm_taskorder.toId = insp_taskorder.routineinspectionToId) AND( wm_taskorder.processInstId = flow_instance.instance_id) AND(wm_taskorder.STATUS <> 26));
--------------------------------------------------------
--  DDL for View V_WM_INSP_WORKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_INSP_WORKORDER" ("id", "draftId", "processDefineId", "processInstId", "woId", "woType", "woTitle", "status", "currentTaskId", "currentTaskName", "creator", "creatorName", "creatorOrgId", "createTime", "isOverTime", "requireCompleteTime", "acceptComment", "finalCompleteTime", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "acceptPeople", "acceptPeopleName", "lastReplyPeople", "lastReplyPeopleName", "isRead", "isSendWillOverTime", "isSendOverTime", "orderOwner", "orderOwnerOrgId", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "type", "orgId", "planStartTime", "planEndTime") AS 
  SELECT wm_workorder.id AS "id", wm_workorder.draftId AS "draftId", wm_workorder.processDefineId AS "processDefineId", wm_workorder.processInstId AS "processInstId", wm_workorder.woId AS "woId", wm_workorder.woType AS "woType", wm_workorder.woTitle AS "woTitle", wm_workorder. STATUS AS "status", wm_workorder.currentTaskId AS "currentTaskId", wm_workorder.currentTaskName AS "currentTaskName", wm_workorder.creator AS "creator", wm_workorder.creatorName AS "creatorName", wm_workorder.creatorOrgId AS "creatorOrgId", wm_workorder.createTime AS "createTime", wm_workorder.isOverTime AS "isOverTime", wm_workorder.requireCompleteTime AS "requireCompleteTime", wm_workorder.acceptComment AS "acceptComment", wm_workorder.finalCompleteTime AS "finalCompleteTime", wm_workorder.currentHandler AS "currentHandler", wm_workorder.currentHandlerName AS "currentHandlerName", wm_workorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_workorder.acceptPeople AS "acceptPeople", wm_workorder.acceptPeopleName AS "acceptPeopleName", wm_workorder.lastReplyPeople AS "lastReplyPeople", wm_workorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_workorder.isRead AS "isRead", wm_workorder.isSendWillOverTime AS "isSendWillOverTime", wm_workorder.isSendOverTime AS "isSendOverTime", wm_workorder.orderOwner AS "orderOwner", wm_workorder.orderOwnerOrgId AS "orderOwnerOrgId", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", insp_planworkorder.type AS "type", insp_planworkorder.orgId AS "orgId", insp_planworkorder.planStartTime AS "planStartTime", insp_planworkorder.planEndTime AS "planEndTime" FROM wm_workorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, insp_planworkorder WHERE (( wm_workorder. STATUS = wm_statusreg.id ) AND ( wm_workorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_workorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName ) AND ( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_workorder.woId = insp_planworkorder.routineinspectionWoId ) AND ( wm_bizprocess_conf.bizFlag = 'XJ' ) AND (wm_workorder. STATUS <> 25));
--------------------------------------------------------
--  DDL for View V_WM_TASKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_TASKORDER" ("id", "processDefineId", "processInstId", "toId", "woId", "parentBizOrderId", "status", "currentTaskId", "currentTaskName", "toTitle", "toDesc", "toType", "assigner", "assignerName", "assignerOrgId", "assignComment", "assignTime", "requireCompleteTime", "lastReplyPeople", "lastReplyPeopleName", "finalCompleteTime", "acceptPeople", "acceptPeopleName", "acceptTime", "acceptComment", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "rejectTime", "rejectComment", "cancelTime", "cancelComment", "reassignTime", "reassignComment", "isOverTime", "isRead", "isSendWillOverTime", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "baseStationLevel", "acceptProfessional", "faultStationName") AS 
  SELECT wm_taskorder.id AS "id", wm_taskorder.processDefineId AS "processDefineId", wm_taskorder.processInstId AS "processInstId", wm_taskorder.toId AS "toId", wm_taskorder.woId AS "woId", wm_taskorder.parentBizOrderId AS "parentBizOrderId", wm_taskorder. STATUS AS "status", wm_taskorder.currentTaskId AS "currentTaskId", wm_taskorder.currentTaskName AS "currentTaskName", wm_taskorder.toTitle AS "toTitle", wm_taskorder.toDesc AS "toDesc", wm_taskorder.toType AS "toType", wm_taskorder.assigner AS "assigner", wm_taskorder.assignerName AS "assignerName", wm_taskorder.assignerOrgId AS "assignerOrgId", wm_taskorder.assignComment AS "assignComment", wm_taskorder.assignTime AS "assignTime", wm_taskorder.requireCompleteTime AS "requireCompleteTime", wm_taskorder.lastReplyPeople AS "lastReplyPeople", wm_taskorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_taskorder.finalCompleteTime AS "finalCompleteTime", wm_taskorder.acceptPeople AS "acceptPeople", wm_taskorder.acceptPeopleName AS "acceptPeopleName", wm_taskorder.acceptTime AS "acceptTime", wm_taskorder.acceptComment AS "acceptComment", wm_taskorder.currentHandler AS "currentHandler", wm_taskorder.currentHandlerName AS "currentHandlerName", wm_taskorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_taskorder.rejectTime AS "rejectTime", wm_taskorder.rejectComment AS "rejectComment", wm_taskorder.cancelTime AS "cancelTime", wm_taskorder.cancelComment AS "cancelComment", wm_taskorder.reassignTime AS "reassignTime", wm_taskorder.reassignComment AS "reassignComment", wm_taskorder.isOverTime AS "isOverTime", wm_taskorder.isRead AS "isRead", wm_taskorder.isSendWillOverTime AS "isSendWillOverTime", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", repair_workorder.baseStationLevel AS "baseStationLevel", repair_workorder.acceptProfessional AS "acceptProfessional", repair_workorder.faultStationName AS "faultStationName" FROM wm_taskorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, repair_workorder WHERE (( wm_taskorder. STATUS = wm_statusreg.id ) AND ( wm_taskorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_taskorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName ) AND ( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId ) AND ( wm_taskorder.woId = repair_workorder.woId ));
--------------------------------------------------------
--  DDL for View V_WM_URGENTREPAIR_TASKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_URGENTREPAIR_TASKORDER" ("id", "processDefineId", "processInstId", "toId", "woId", "parentBizOrderId", "status", "currentTaskId", "currentTaskName", "toTitle", "toDesc", "toType", "assigner", "assignerName", "assignerOrgId", "assignComment", "assignTime", "requireCompleteTime", "lastReplyPeople", "lastReplyPeopleName", "finalCompleteTime", "acceptPeople", "acceptPeopleName", "acceptTime", "acceptComment", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "rejectTime", "rejectComment", "cancelTime", "cancelComment", "reassignTime", "reassignComment", "isOverTime", "isRead", "isSendWillOverTime", "orderOwner", "orderOwnerOrgId", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "baseStationLevel", "acceptProfessional", "faultStationName") AS 
  SELECT DISTINCT wm_taskorder. ID AS "id", wm_taskorder.processDefineId AS "processDefineId", wm_taskorder.processInstId AS "processInstId", wm_taskorder.toId AS "toId", wm_taskorder.woId AS "woId", wm_taskorder.parentBizOrderId AS "parentBizOrderId", wm_taskorder.STATUS AS "status", wm_taskorder.currentTaskId AS "currentTaskId", wm_taskorder.currentTaskName AS "currentTaskName", wm_taskorder.toTitle AS "toTitle", wm_taskorder.toDesc AS "toDesc", wm_taskorder.toType AS "toType", wm_taskorder.assigner AS "assigner", wm_taskorder.assignerName AS "assignerName", wm_taskorder.assignerOrgId AS "assignerOrgId", TO_CHAR(wm_taskorder.assignComment) AS "assignComment", wm_taskorder.assignTime AS "assignTime", wm_taskorder.requireCompleteTime AS "requireCompleteTime", wm_taskorder.lastReplyPeople AS "lastReplyPeople", wm_taskorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_taskorder.finalCompleteTime AS "finalCompleteTime", wm_taskorder.acceptPeople AS "acceptPeople", wm_taskorder.acceptPeopleName AS "acceptPeopleName", wm_taskorder.acceptTime AS "acceptTime", wm_taskorder.acceptComment AS "acceptComment", wm_taskorder.currentHandler AS "currentHandler", wm_taskorder.currentHandlerName AS "currentHandlerName", wm_taskorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_taskorder.rejectTime AS "rejectTime", wm_taskorder.rejectComment AS "rejectComment", wm_taskorder.cancelTime AS "cancelTime", wm_taskorder.cancelComment AS "cancelComment", wm_taskorder.reassignTime AS "reassignTime", wm_taskorder.reassignComment AS "reassignComment", wm_taskorder.isOverTime AS "isOverTime", wm_taskorder.isRead AS "isRead", wm_taskorder.isSendWillOverTime AS "isSendWillOverTime", wm_taskorder.orderOwner AS "orderOwner", wm_taskorder.orderOwnerOrgId AS "orderOwnerOrgId", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", repair_workorder.baseStationLevel AS "baseStationLevel", repair_workorder.acceptProfessional AS "acceptProfessional", repair_workorder.faultStationName AS "faultStationName" FROM wm_taskorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, repair_workorder WHERE(( wm_taskorder.STATUS = wm_statusreg. ID) AND( wm_taskorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_taskorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName) AND( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_taskorder.woId = repair_workorder.woId) AND( wm_bizprocess_conf.bizFlag = 'QX'));
--------------------------------------------------------
--  DDL for View V_WM_URGENTREPAIR_WORKORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_WM_URGENTREPAIR_WORKORDER" ("id", "draftId", "processDefineId", "processInstId", "woId", "woType", "woTitle", "status", "currentTaskId", "currentTaskName", "creator", "creatorName", "creatorOrgId", "createTime", "isOverTime", "requireCompleteTime", "acceptComment", "finalCompleteTime", "currentHandler", "currentHandlerName", "currentHandlerOrgId", "acceptPeople", "acceptPeopleName", "lastReplyPeople", "lastReplyPeopleName", "isRead", "isSendWillOverTime", "isSendOverTime", "orderOwner", "orderOwnerOrgId", "statusName", "formUrl", "terminalFormUrl", "bizProcessName", "baseStationLevel", "acceptProfessional", "faultStationName", "netElementName", "faultArea", "stationName", "faultStationAddress", "faultType", "faultLevel", "faultOccuredTime", "latestAllowedTime", "faultDescription", "finalReplyTime", "faultGenera", "faultCause", "sideeffectService", "affectedServiceName", "faultDealResult", "foreseeResolveTime", "resonForDelayApply", "isAlarmClear", "faultSolveTime", "customerWoId", "customerWoTitle", "customerSenderDepartment", "customerWoSender", "customerWoCurrentDepartment", "customerWoCurrentHandler", "isEmergencyFault", "sendWoWay", "customerSideeffectService", "customerAffectedServiceName", "alarmFormalName", "alarmNetManageSource", "alarmLogicalClass", "alarmLogicalSubClass", "alarmClass", "netmanageAlarmLevel", "alarmAssociatedId", "subAlarmNumber", "subAlarmInfo") AS 
  SELECT wm_workorder. ID AS "id", wm_workorder.draftId AS "draftId", wm_workorder.processDefineId AS "processDefineId", wm_workorder.processInstId AS "processInstId", wm_workorder.woId AS "woId", wm_workorder.woType AS "woType", wm_workorder.woTitle AS "woTitle", wm_workorder.STATUS AS "status", wm_workorder.currentTaskId AS "currentTaskId", wm_workorder.currentTaskName AS "currentTaskName", wm_workorder.creator AS "creator", wm_workorder.creatorName AS "creatorName", wm_workorder.creatorOrgId AS "creatorOrgId", wm_workorder.createTime AS "createTime", wm_workorder.isOverTime AS "isOverTime", wm_workorder.requireCompleteTime AS "requireCompleteTime", wm_workorder.acceptComment AS "acceptComment", wm_workorder.finalCompleteTime AS "finalCompleteTime", wm_workorder.currentHandler AS "currentHandler", wm_workorder.currentHandlerName AS "currentHandlerName", wm_workorder.currentHandlerOrgId AS "currentHandlerOrgId", wm_workorder.acceptPeople AS "acceptPeople", wm_workorder.acceptPeopleName AS "acceptPeopleName", wm_workorder.lastReplyPeople AS "lastReplyPeople", wm_workorder.lastReplyPeopleName AS "lastReplyPeopleName", wm_workorder.isRead AS "isRead", wm_workorder.isSendWillOverTime AS "isSendWillOverTime", wm_workorder.isSendOverTime AS "isSendOverTime", wm_workorder.orderOwner AS "orderOwner", wm_workorder.orderOwnerOrgId AS "orderOwnerOrgId", wm_statusreg.statusName AS "statusName", wm_bizproc_taskinfo_conf.formUrl AS "formUrl", wm_bizproc_taskinfo_conf.terminalFormUrl AS "terminalFormUrl", wm_bizprocess_conf.bizProcessName AS "bizProcessName", repair_workorder.baseStationLevel AS "baseStationLevel", repair_workorder.acceptProfessional AS "acceptProfessional", repair_workorder.faultStationName AS "faultStationName", repair_workorder.netElementName AS "netElementName", repair_workorder.faultArea AS "faultArea", repair_workorder.stationName AS "stationName", repair_workorder.faultStationAddress AS "faultStationAddress", repair_workorder.faultType AS "faultType", repair_workorder.faultLevel AS "faultLevel", repair_workorder.faultOccuredTime AS "faultOccuredTime", repair_workorder.latestAllowedTime AS "latestAllowedTime", repair_workorder.faultDescription AS "faultDescription", repair_workorder.finalReplyTime AS "finalReplyTime", repair_workorder.faultGenera AS "faultGenera", repair_workorder.faultCause AS "faultCause", repair_workorder.sideeffectService AS "sideeffectService", repair_workorder.affectedServiceName AS "affectedServiceName", repair_workorder.faultDealResult AS "faultDealResult", repair_workorder.foreseeResolveTime AS "foreseeResolveTime", repair_workorder.resonForDelayApply AS "resonForDelayApply", repair_workorder.isAlarmClear AS "isAlarmClear", repair_workorder.faultSolveTime AS "faultSolveTime", repair_customerworkorder.customerWoId AS "customerWoId", repair_customerworkorder.customerWoTitle AS "customerWoTitle", repair_customerworkorder.customerSenderDepartment AS "customerSenderDepartment", repair_customerworkorder.customerWoSender AS "customerWoSender", repair_customerworkorder.customerWoCurrentDepartment AS "customerWoCurrentDepartment", repair_customerworkorder.customerWoCurrentHandler AS "customerWoCurrentHandler", repair_customerworkorder.isEmergencyFault AS "isEmergencyFault", repair_customerworkorder.sendWoWay AS "sendWoWay", repair_customerworkorder.sideeffectService AS "customerSideeffectService", repair_customerworkorder.affectedServiceName AS "customerAffectedServiceName", repair_customerworkorder.alarmFormalName AS "alarmFormalName", repair_customerworkorder.alarmNetManageSource AS "alarmNetManageSource", repair_customerworkorder.alarmLogicalClass AS "alarmLogicalClass", repair_customerworkorder.alarmLogicalSubClass AS "alarmLogicalSubClass", repair_customerworkorder.alarmClass AS "alarmClass", repair_customerworkorder.netmanageAlarmLevel AS "netmanageAlarmLevel", repair_customerworkorder.alarmAssociatedId AS "alarmAssociatedId", repair_customerworkorder.subAlarmNumber AS "subAlarmNumber", repair_customerworkorder.subAlarmInfo AS "subAlarmInfo" FROM wm_workorder, wm_statusreg, wm_bizproc_taskinfo_conf, wm_bizprocess_conf, repair_workorder, repair_customerworkorder WHERE(( wm_workorder.STATUS = wm_statusreg. ID) AND( wm_workorder.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_workorder.currentTaskName = wm_bizproc_taskinfo_conf.taskName) AND( wm_bizprocess_conf.processDefineId = wm_bizproc_taskinfo_conf.ownerProcessDefineId) AND( wm_workorder.woId = repair_workorder.woId) AND( wm_bizprocess_conf.bizFlag = 'QX') AND( repair_workorder.woId = repair_customerworkorder.woId));
