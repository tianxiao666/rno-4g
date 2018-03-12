--------------------------------------------------------
--  DDL for Function ISINAREA
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "ISINAREA" (
    rootId          IN VARCHAR2, ---figurenodeId
    areaNodeId      IN VARCHAR2,---area ids
    searchType     IN VARCHAR2 ---查找类型
) return VARCHAR2
AS
    sTemp VARCHAR2(1000); ---返回值
    sTempChd VARCHAR2(1000);---figurenodeId
    sT VARCHAR2(1000);----areaids
    sType VARCHAR2(1000);---查找类型
    s VARCHAR2(1000);---判断变量
    pos  NUMBER;--判断是否在权限区域下
BEGIN
  sTemp := '0';---初始化值
  sTempChd :=rootId;
  sT :=areaNodeId;
  sType:=searchType;
  s:='0';
  BEGIN
    SELECT to_char(fn.id)
      INTO s ---无主资源
      from figurenode fn
     where fn.entityType = sType
       and fn.id = sTempChd
       and fn.id  in
           (select rightId from figureline where righttype = sType); ---查询是否在figureline（无上级资源）
  EXCEPTION WHEN OTHERS THEN
     s := null;--- 
  END;

  IF s  IS  NULL THEN--是无上级资源，返回 '1'
    sTemp := '1';
  ELSE  --不是无上级资源，循环查询figureline  看上级区域是否在area ids里面

    WHILE    sTempChd  IS NOT NULL loop           
           BEGIN
             BEGIN
               SELECT to_char(fl.leftId)
                 INTO sTempChd
                 from figureline fl
                where fl.rightId = sTempChd
                  and fl.linkType = 'CLAN';
             EXCEPTION
               WHEN OTHERS THEN
                 sTempChd := NULL;
             END;

            IF  sTempChd  IS NULL THEN --上级不存在返回‘0’
               sTemp := '0';   
            ELSE  ---上级资源在area ids里返回‘1’
              BEGIN
               sTemp := '1';
               pos := INSTR(sT,concat(sTempChd,'Area' ));
               IF pos>0 THEN                   
                 sTempChd:=NULL;
               END IF;
              END;
            END IF; 
           END;
       END loop;
  END IF;
 RETURN  sTemp;
EXCEPTION
    WHEN OTHERS THEN 
    RETURN '0' ;
END;

/
