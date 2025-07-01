--Создание таблицы
CREATE TABLE IF NOT EXISTS employees(
ID SERIAL PRIMARY KEY,
NAME VARCHAR(50) NOT NULL,
AGE INT,
DEPARTMENT VARCHAR(50),
IS_ACTIVE BOOLEAN,
SALARY DOUBLE PRECISION
);

-- Добавление данных в таблицу
INSERT INTO employees(name,age)
VALUES (null, 25);

-- Чтение данных из таблицы
SELECT * FROM employees;

SELECT e.name, e.age FROM employees e;

SELECT * FROM employees
WHERE age=30 and lower(NAME) LIKE lower('%di%');

select * from employees
where age between 25 and 30;

select * from employees
order by age asc, name desc;

-- Обновление данных в таблице
UPDATE employees
SET name='Aza'
WHERE id=1;

UPDATE employees
SET name='John', age=21
WHERE id=1;

-- Удаление данных из таблицы
DELETE FROM employees
WHERE id=3;


-- Добавление нового столбца в таблицу
ALTER TABLE employees
ADD COLUMN if not exists hire_date date;

-- Изменение имени столбца
ALTER TABLE employees
RENAME COLUMN hire_date to date_of_hire;

-- Изменение типа данных столбца
ALTER TABLE employees
ALTER COLUMN date_of_hire TYPE varchar(50);

-- Удаление столбца из таблицы
ALTER TABLE employees
DROP COLUMN date_of_hire;

-- Удаление таблицы
DROP TABLE employees;




