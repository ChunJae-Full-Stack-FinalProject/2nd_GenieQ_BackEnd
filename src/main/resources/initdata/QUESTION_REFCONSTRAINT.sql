--------------------------------------------------------
--  Ref Constraints for Table QUESTION
--------------------------------------------------------

  ALTER TABLE "GENIEQ"."QUESTION" ADD CONSTRAINT "FKGRTFKTOIRAMCOCQ4498AFKO3P" FOREIGN KEY ("PAS_CODE")
	  REFERENCES "GENIEQ"."PASSAGE" ("PAS_CODE") ENABLE
