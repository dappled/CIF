-- From the terminal command line, shut down any existing copy of SQL:
-- sudo /usr/local/mysql/bin/mysqladmin shutdown
 
-- Bring up a new copy of the mysql server:
-- sudo /usr/local/mysql/bin/mysqld_safe
-- Hit control-Z to stop the process you just started
-- Type in the following to make mysql a background process:
-- bg
-- You now have the mysql server running in the background

-- Start a console to your mysql server:
-- /usr/local/mysql/bin/mysql -u root

-- You are now in a mysql client and can issues the following
-- commands.

-- Create a new database for this example and create a prices table in this database

DROP DATABASE priceData;
CREATE DATABASE priceData;
USE priceData;
CREATE TABLE prices ( id INT, ts INT, price FLOAT );

-- Populate database with some data

INSERT INTO prices ( id, ts, price ) VALUES ( 3, 90, 75.00 );

INSERT INTO prices ( id, ts, price ) VALUES ( 1, 30,  89.90 );
INSERT INTO prices ( id, ts, price ) VALUES ( 1, 40,  90.00 );
INSERT INTO prices ( id, ts, price ) VALUES ( 1, 110, 90.10 );

INSERT INTO prices ( id, ts, price ) VALUES ( 2, 50,  45.00 );
INSERT INTO prices ( id, ts, price ) VALUES ( 2, 70,  44.90 );
INSERT INTO prices ( id, ts, price ) VALUES ( 2, 80,  44.85 );

select * from prices order by id, ts;

-- Demonstrate group by / order by constructs

select id, ( max( ts ) ) ts, price from prices where (ts < 60 ) group by id order by ts, id;
select id, ( max( ts ) ) ts, price from prices where (ts < 120 ) group by id order by ts, id;

-- The above has a subtle error because 'group by' does not match with the correct time stamp
-- Demonstrate getting the right price assocaited with the right timestamp

select * from (
  (select id, max(ts) as ts from prices where (ts < 120) group by id) temp1 
  join 
  (select id, ts, price from prices) temp2 using (id,ts) 
);

-- In table results1, compute bucket number, and for each stock id and bucket number
-- select the record with the highest time stamp (Steps 1 and 2)

drop table results1;
create table results1( id INT, ts INT, price FLOAT, bkt INT, type INT );
insert into results1 
  select 
    id, 
    ts, 
    price, 
    bkt, 
    1 
from (
  (select 
    ( prices.id                            ) id, 
    ( max( prices.ts )                     ) ts, 
    ( FLOOR ( ( prices.ts - 1 ) / 60 + 1 ) ) bkt 
    from prices group by id, bkt
  ) tempTab1 join ( select * from prices ) tempTab2 using (id, ts) 
);

select * from results1 order by ts, id;

-- Step 3 - Create filler records for buckets that don't have prices

-- Fill table timeStamps with just the time stamps we're interested in: 60, 120, 180, etc.
-- For this, we'll use a procedure that implements a loop

drop procedure dorepeat;
drop table timeStamps;
create table timeStamps( n INT );
delimiter //
CREATE PROCEDURE dorepeat( p1 INT ) 
  BEGIN SET @x = 0; 
  REPEAT SET @x = @x + 60; 
    insert into timeStamps(n) values ( @x ); 
  UNTIL @x >= p1 END REPEAT; END
//
delimiter ';'
CALL dorepeat( 120 );
select * from timeStamps;

-- Create a table of NULL price records for all ids - These are records that fall exactly on the
-- time stamps we're interested in but contain a value of NULL. We can do this by joining unique
-- stock ids with the above table of bucket boundary time stamps

drop table results2;
create table results2( id INT, ts INT, price FLOAT, idx INT, type INT );
insert into results2 
  select idTab.id, timeTab.n, NULL as price, FLOOR( ( timeTab.n - 1 ) / 60 + 1 ), 2 
  from (
    select id from results1 group by id 
  ) idTab, ( 
    select n from timeStamps
  ) timeTab;
select * from results2 order by ts, id;

-- Step 4 - Append this table to the incomplete results we obtained before

insert into results1 ( select * from results2 );
select * from results1 order by ts, id;

-- Where we have a result of type 1, we don't want to use a result of type 2.
-- Where we don't have a result of type 1, we want to use a result of type 2.

select id, ts, price, bkt, min( type ) from results1 group by id, bkt order by ts, id;

