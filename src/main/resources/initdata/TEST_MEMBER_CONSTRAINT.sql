--------------------------------------------------------
--  Constraints for Table TEST_MEMBER
--------------------------------------------------------

  ALTER TABLE "GENIEQ"."TEST_MEMBER" ADD CONSTRAINT "UK9EACS5V6VUSRIM2AN55BLU71G" UNIQUE ("NAME")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
  ALTER TABLE "GENIEQ"."TEST_MEMBER" ADD PRIMARY KEY ("MEMBER_CODE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
  ALTER TABLE "GENIEQ"."TEST_MEMBER" MODIFY ("NAME" NOT NULL ENABLE)
  ALTER TABLE "GENIEQ"."TEST_MEMBER" MODIFY ("MEMBER_CODE" NOT NULL ENABLE)
