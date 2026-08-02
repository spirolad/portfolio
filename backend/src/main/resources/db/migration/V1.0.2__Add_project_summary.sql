ALTER TABLE project ADD COLUMN IF NOT EXISTS summary TEXT;
UPDATE project SET summary = description WHERE summary IS NULL;
