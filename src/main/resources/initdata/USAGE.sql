--------------------------------------------------------
--  DDL for Table USAGE
--------------------------------------------------------

  CREATE TABLE "GENIEQ"."USAGE" 
   (	"USA_CODE" NUMBER, 
	"USA_TYPE" VARCHAR2(255 CHAR), 
	"USA_DATE" DATE, 
	"USA_COUNT" NUMBER, 
	"USA_BALANCE" NUMBER DEFAULT 5, 
	"MEM_CODE" NUMBER, 
	 CONSTRAINT "PK_USAGE" PRIMARY KEY ("USA_CODE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "FKOUSKPVNC962L5ETSWWIGYOJ8Q" FOREIGN KEY ("MEM_CODE")
	  REFERENCES "GENIEQ"."MEMBER" ("MEM_CODE") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"
