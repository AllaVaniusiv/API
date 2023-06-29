I created an api that allows you to receive and send information about transport that works in quarries, basic information about quarries and
about transport drivers and the transport schedule. Wrote the necessary classes for creating an API, used the lombok library
to reduce the amount of code. Implemented REST services for all entities using Spring Boot.
I also implemented operations: GET/POST/PUT/DELETE operation GET/2 - returns an entity with an id equal to 2. Operation /GET - returns all entities that are present in the system.
I divided the code into controllers, services, and the level of data access. I connected controllers, services, and the level of data access using dependency inversion.
Implemented data storage and reading them from a csv file. Each file contains headers (corresponding to the names of the attributes of the designed classes) only in the first line.
When the application is launched, all entities are read from the file and saved in the hash map. When reading data, a search is made for all files for the entity that were created in the current month.
Checked the code using Spotbugs and Checkstyle. The code contains unit tests for checking the logic of writing and searching for files on the file system.
