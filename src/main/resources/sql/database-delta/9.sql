--
-- Tag-Fragment
--
ALTER TABLE fragment ADD COLUMN tag_id BIGINT;
ALTER TABLE tag ADD COLUMN fragment_id BIGINT;

--
-- Re-create full text search index for the table "fragment"
-- Without this, an unexpected exception ("Database is already closed")
-- will be thrown when registering multiple fragments
-- 
CALL FT_REINDEX();
