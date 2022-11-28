"Get meals": curl http://localhost:8080/topjava/rest/meals

"Get meal with id": curl http://localhost:8080/topjava/rest/meals/100009

"Get meals filtered by date and time": curl http://localhost:8080/topjava/rest/meals/filtered?startDate=2020-01-30
&startTime=10:15&endDate=2020-01-30&endTime=22:15

"Create new meal": curl http://localhost:8080/topjava/rest/meals -H "Content-Type: application/json;charset=utf-8"
-d "{\"dateTime\":\"2020-01-30T10:02:00\",\"description\":\"test\",\"calories\":200}"

"Update existing meal": curl -X PUT -H "Content-Type: application/json"
-d "{\"dateTime\":\"2022-01-30T10:02:00\",\"description\":\"updated\",\"calories\":200}"
http://localhost:8080/topjava/rest/meals/100005

"Delete meal": curl -X DELETE http://localhost:8080/topjava/rest/meals/100004