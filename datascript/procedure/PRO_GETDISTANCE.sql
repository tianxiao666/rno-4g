--------------------------------------------------------
--  DDL for Procedure PRO_GETDISTANCE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PRO_GETDISTANCE" ( LatBegin in FLOAT , LngBegin in FLOAT , LatEnd in FLOAT , LngEnd in FLOAT , GISmileage out FLOAT)
as
     Distance FLOAT;
     EARTH_RADIUS FLOAT;
     RadLatBegin FLOAT;
     RadLatEnd FLOAT;
     RadLatDiff FLOAT;
     RadLngDiff FLOAT;
begin
     EARTH_RADIUS := 6378.137;
     RadLatBegin := LatBegin * acos(-1) / 180.0;
     RadLatEnd := LatEnd * acos(-1) / 180.0;
     RadLatDiff := RadLatBegin - RadLatEnd;
     RadLngDiff := LngBegin * acos(-1) / 180.0 - LngEnd * acos(-1) / 180.0;
     Distance := 2 * ASIN(SQRT(POWER(SIN(RadLatDiff / 2), 2) + COS(RadLatBegin) * COS(RadLatEnd) * POWER(SIN(RadLngDiff/2),2)));
     Distance := Distance * EARTH_RADIUS ;
     GISmileage := Distance * 1000;
end;

/
