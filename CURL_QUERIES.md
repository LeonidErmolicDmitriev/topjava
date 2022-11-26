curl http://localhost:8080/topjava/rest/meals

curl http://localhost:8080/topjava/rest/meals/100009

curl http://localhost:8080/topjava/rest/meals/by_dates_and_time

curl http://localhost:8080/topjava/rest/meals/by_dates_and_time?startDate=2020-01-30&startTime=10:15&endDate=2020-01-30&endTime=22:15

curl -Method PUT -H @{"Accept"= "application/json"; "content-type"= "application/json"} -Body '{"id":100003,"dateTime":"
2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}'
-uri "http://localhost:8080/topjava/rest/meals/100003"

curl -Method POST -H @{"Accept"= "application/json"; "content-type"= "application/json"} -Body '{"dateTime":"
2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}'
-uri "http://localhost:8080/topjava/rest/meals"

curl -Method DELETE -uri "http://localhost:8080/topjava/rest/meals/100003"