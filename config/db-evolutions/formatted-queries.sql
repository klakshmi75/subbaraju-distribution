<!-- ABSTRAT BY Date-->
SELECT
    date AS "Date",
    SUM(num_cases_imfl + num_cases_beer) AS "Total Cases",
    SUM(form_3) AS "Form 3 (In Amount)"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
GROUP BY date
HAVING date = :date

<!-- ABSTRAT BY Date Range-->

SELECT
    date AS "Date",
    SUM(num_cases_imfl + num_cases_beer) AS "Total Cases",
    SUM(form_3) AS "Form 3 (In Amount)"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
GROUP BY date
HAVING date BETWEEN :startDate AND :endDate

<!-- Outlet Details BY Date-->

SELECT
    date AS "Date",
    vehicle_number AS "Vehicle Number",
    outlet_address AS "Outlet Address",
    Station AS "Station",
    km_from_depo AS "KM From depo",
    om.outlet_code AS "Outlet Code",
    num_cases_imfl AS "IMFL",
    num_cases_beer AS "BEER",
    (num_cases_imfl + num_cases_beer) AS "Total Cases",
    form_3 AS "Form 3 (In Amount)"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
WHERE
    date = :date

<!-- Outlet Details BY Date Range-->

SELECT
    date AS "Date",
    vehicle_number AS "Vehicle Number",
    outlet_address AS "Outlet Address",
    Station AS "Station",
    km_from_depo AS "KM From depo",
    om.outlet_code AS "Outlet Code",
    num_cases_imfl AS "IMFL",
    num_cases_beer AS "BEER",
    (num_cases_imfl + num_cases_beer) AS "Total Cases",
    form_3 AS "Form 3 (In Amount)"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
WHERE
    date BETWEEN :startDate AND :endDate

<!-- Slabwise Details BY Date-->

SELECT
    date AS "date",
    SUM(num_cases_imfl + num_cases_beer) AS "cases",
    sm.slab_code AS "slab_code"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
        JOIN
    slab_master sm ON km_from_depo BETWEEN range_min AND sm.range_max
GROUP BY date , sm.slab_code
HAVING date = :date

<!-- Slabwise Details BY Date Range-->

SELECT
    date AS "date",
    SUM(num_cases_imfl + num_cases_beer) AS "cases",
    sm.slab_code AS "slab_code"
FROM
    trip_details td
        JOIN
    outlet_master om ON td.outlet_code = om.outlet_code
        AND om.depo_name = :depoName
        JOIN
    slab_master sm ON km_from_depo BETWEEN range_min AND sm.range_max
GROUP BY date , sm.slab_code
HAVING date BETWEEN :startDate AND :endDate

<!-- All slabs code and details-->

SELECT
    slab_code AS "slab_code",
    CONCAT(slab_code,
            ' Cases ',
            range_min,
            '-',
            range_max,
            'Km') AS "slab_detail"
FROM
    slab_master