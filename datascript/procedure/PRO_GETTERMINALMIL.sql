create or replace procedure "PRO_GETTERMINALMIL"(d          in varchar,
                                                 carId      in number,
                                                 gpsmileage out float)

 as
  newlat     number(15, 8);
  newlng     number(15, 8);
  oldlat     number(15, 8);
  oldlng     number(15, 8);
  gismileage float;
  gmileage   float;
begin
  declare
  
    cursor gps_select is
      select jingdu, weidu
        from mobile_gps_location
       where picktime  between to_date(substr(d,1,10)||' 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date(substr(d,1,10)||' 23:59:59','yyyy-mm-dd hh24:mi:ss') and to_char(picktime, 'yyyy-mm-dd hh24') = d
         and car_id = carId
         and iscorrect = 1
       order by picktime;
    gps_row mobile_gps_location%rowtype;
  
    cursor gps_old is
      select *
        from (select jingdu, weidu
                from mobile_gps_location
               where picktime between to_date(substr(d,1,10)||' 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date(substr(d,1,10)||' 23:59:59','yyyy-mm-dd hh24:mi:ss') and  to_char(picktime, 'yyyy-mm-dd hh24') <
                     to_char(to_date(d, 'yyyy-mm-dd hh24'),
                             'yyyy-mm-dd hh24')
                 and car_id = carId
                 and iscorrect = 1
               order by picktime desc) p
       where rownum = 1;
    old_g mobile_gps_location%rowtype;
  begin
    gmileage   := 0;
    gismileage := 0;
  
    for old_g in gps_old loop
      oldlng := old_g.jingdu;
      oldlat := old_g.weidu;
    end loop;
    for gps_row in gps_select loop
      newlng := gps_row.jingdu;
      newlat := gps_row.weidu;
      if oldlng is not null then
        pro_getdistance(oldlat, oldlng, newlat, newlng, gismileage);
      end if;
      oldlng := gps_row.jingdu;
      oldlat := gps_row.weidu;
    
      gmileage := gmileage + gismileage;
    
    end loop;
    gpsmileage := gmileage;
  
  end;
end;
