create or replace procedure pro_gpsMil_everyNight

AS
       today DATE;
       maxDate DATE DEFAULT to_date('2012-07-01','yyyy-mm-dd');
BEGIN
       today := trunc(SYSDATE);
       SELECT  MAX(gpsDate) into  maxDate FROM car_gpsmileage ;

       IF maxDate IS NULL THEN

          maxDate := to_date('2012-07-01','yyyy-mm-dd');
       END IF;

       maxDate := maxDate+1/24;


       WHILE maxDate < today
       LOOP
             pro_calculateGpsByDay (maxDate);
             commit;
             --DBMS_OUTPUT.put_line(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') || ' --> update_gpsMil_everyNight run ' || to_char(MAXDATE,'yyyy-mm-dd hh24:mi:ss'));
             maxDate := maxDate+1/24;
       END LOOP;


END;
