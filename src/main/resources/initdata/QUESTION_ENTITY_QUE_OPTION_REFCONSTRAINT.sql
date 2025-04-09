--------------------------------------------------------
--  Ref Constraints for Table QUESTION_ENTITY_QUE_OPTION
--------------------------------------------------------

  ALTER TABLE "GENIEQ"."QUESTION_ENTITY_QUE_OPTION" ADD CONSTRAINT "FKHOVHF3U80MVLAFESAYPOGLYV8" FOREIGN KEY ("QUESTION_ENTITY_QUE_CODE")
	  REFERENCES "GENIEQ"."QUESTION" ("QUE_CODE") ENABLE
