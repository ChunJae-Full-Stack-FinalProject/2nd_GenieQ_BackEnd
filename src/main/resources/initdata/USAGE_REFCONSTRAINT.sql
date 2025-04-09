--------------------------------------------------------
--  Ref Constraints for Table USAGE
--------------------------------------------------------

  ALTER TABLE "GENIEQ"."USAGE" ADD CONSTRAINT "FKOUSKPVNC962L5ETSWWIGYOJ8Q" FOREIGN KEY ("MEM_CODE")
	  REFERENCES "GENIEQ"."MEMBER" ("MEM_CODE") ENABLE
