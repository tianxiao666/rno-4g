create or replace procedure pro_calculateGpsByDay(d in date) AS
  cId        number(19);
  tId        number(19);
  imei       VARCHAR(200);
  carnum     VARCHAR(200);
  pp         VARCHAR(200);
  gismileage number;
  seqString  varchar2(1024);
BEGIN
  DECLARE
    CURSOR terminal_select is
      SELECT m.car_id,
             to_char(m.pickTime, 'yyyy-mm-dd hh24') p,
             c.carnumber,
             t.id,
             t.clientimei
        FROM mobile_gps_location m,
             car_terminal        t,
             car_car             c,
             car_carterminalpair p
       WHERE picktime between to_date(to_char(d,'yyyy-mm-dd')||' 00', 'yyyy-mm-dd hh24') and to_date(to_char(d,'yyyy-mm-dd')||' 23', 'yyyy-mm-dd hh24') and  to_char(m.pickTime, 'yyyy-mm-dd hh24') =
             to_char(d, 'yyyy-mm-dd hh24')
         and m.car_id = c.id
         and p.car_id = m.car_id
         and p.terminal_id = t.id
       GROUP BY m.car_id,
                to_char(m.pickTime, 'yyyy-mm-dd hh24'),
                c.carnumber,
                t.id,
                t.clientimei;
    terminal_row mobile_gps_location%rowtype;
  BEGIN

    FOR terminal_row IN terminal_select LOOP
      cId := terminal_row.car_id;
      pp  := terminal_row.p;
      carnum := terminal_row.carnumber;
      imei := terminal_row.clientimei;
      tId := terminal_row.id;
      PRO_GETTERMINALMIL(pp, cId, gismileage);
      seqString := 'INSERT INTO car_gpsmileage( id ,carnumber, terminalId ,terminalimei, gpsDate , GPSMILEAGE,car_id ) VALUES( SEQ_CAR_GPSMILEAGE.nextval ,'''||carnum||''', '||tId||' ,'''||imei||''', to_date(''' || pp ||
                   ''',''yyyy-mm-dd hh24:mi:ss'') , ' || gismileage || ' ,' || cId || ')';
      EXECUTE IMMEDIATE seqString;

    END LOOP;

  END;
END;

  --call pro_calculateGpsByDay(to_date('2013-03-11','yyyy-mm-dd'));
