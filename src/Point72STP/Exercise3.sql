
-- SQL: Detecting Potential Payment Fraud in an Online Marketplace
--
-- The company needs a report identifying users who have failed transactions
-- using different payment methods. Failed transactions are those where the
-- status field is set to "Failed".
--
-- The result should include the following columns:
-- - user_id: the user attempting multiple failed transactions
-- - failed_transactions: total number of failed transactions
-- - distinct_payment_methods: total number of unique payment methods used
--
-- Only users who have made more than 5 failed transactions across the entire
-- dataset should be included in the report.
--
-- The query should aggregate failed transactions by user and count both:
-- - total failed attempts
-- - distinct payment methods involved
--
-- Expected Output Columns:
-- user_id | failed_transactions | distinct_payment_methods

SELECT
    user_id,
    COUNT(*) AS failed_transactions,
    COUNT(DISTINCT payment_method) AS distinct_payment_methods
FROM transactions
WHERE status = 'Failed'
GROUP BY user_id
HAVING COUNT(*) > 5;