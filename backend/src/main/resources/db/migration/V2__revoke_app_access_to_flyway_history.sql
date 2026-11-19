-- The application role never needs Flyway's bookkeeping table.
REVOKE ALL ON TABLE public.flyway_schema_history FROM hiresense_app;
