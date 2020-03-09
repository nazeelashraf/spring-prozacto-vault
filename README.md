
# :hospital: Prozacto Patient Vault
A RESTful web service for Prozacto.

## Stack
This app runs on Spring Boot Java framework with embedded tomcat (port 8765) as the server and with H2 as the in memory SQL database.

## Setup
1. Clone the repository.
2. Run the following command in the root path to start the server:
   * ```./mvnw clean install -DskipTests=true spring-boot:run``` for UNIX systems
   * ```.\mvnw.cmd clean install -DskipTests=true spring-boot:run``` for Windows systems
   (Note that this command downloads required jar files for the project, which may take some time. The files will be located by default in ```~/.m2/repository``` in UNIX sytems and ```C:\Users\<username>\.m2\repository``` for Windows systems)
   
3. Use a REST client of your choice (Postman/Insomnia/Curl/...) for testing the endpoints.
   
## H2 Console
To access the H2 console and see the tables, start the server and go to ```http://localhost:8765/h2-console``` and use the following:
1. JDBC URL: ```jdbc:h2:mem:testdb```
2. username: ```sa```
3. password: empty

## Accounts
The following users are already preconfigured with access Roles (```username/password```):
  * Admin:  ```admin/admin```
  * Doctor: ```doctor/doctor```
  * Assistant: ```assistant/assistant```
  * Patient: ```patient/patient```

## Endpoints:
Prefix all endpoints with ```http://localhost:8765```. Step by step endpoint guide:
1. The following endpoint is used to sign-up: POST ```/users/sign-up``` with following request body
   ```
      {
         "username": "jack",
         "password": "sparrow"
      }
   ```
   On successful signup, you will recieve the following response message:
   ```Thank you for signing up! Your unique ID is 5```.
   Make note of this unique ID as it is required to link the role to the user at a later stage.
   
2. Login with the Admin credentials using this endpoint: POST ```/login``` with the same request body as above.

    On successful login, a response with the following header is generated:
```Authorization: Bearer <token>```. This ```token``` should be saved and all future endpoint calls should have this header
    
    If the token has expired, the endpoint POST ```/refresh-token``` with the ```token``` as the request body will generate a new token. Refresh validity is 10 days.
    
3. **Admin Endpoints**: With the header set with admin ```token```, you can access the following APIs:
   * GET ```/clinic```: to get list of all clinics with doctors, assistants, patients, and appointments for each clinic
   * POST ```/clinic```:  to create a new clinic with name ```name```
   ```
   {
        "name": "Awesome Clinic"
   }
   ```
   * POST ```/doctor```: to create a new doctor with a user of id ```id``` and with a clinic of id ```clinicId```
   ```
   {
        "user": {
          "id": 5
        },
        "firstName": "New",
        "lastName": "Doctor",
        "clinic": {
          "clinicId": 7
        },
        "qualification": "M.B.B.S.",
        "doctorType": "Dentist"
    }
   ```
   * GET ```/users/all```: to get the list of all application users
   
4. **Assistant Endpoints**: Log in with the assistant credentials and set the Authorization header with the token.
   * POST ```/appointment```: to create an appointment with patient id ```patientId``` and doctor with doctor id ```doctorId``` and date ```date```
   ```
   {
      "date":"2020-04-09",
      "doctor": {
        "doctorId": 1
      },
      "patient": {
        "patientId": 1
      }
   }
   ```
   * GET ```/appointment```: gets list of all appointments created by this assistant for the clinic to which they are associated
   * GET ```/doctor```: gets list of all doctors that are in the same clinic as the assistant
   * POST ```/patient```: create a patient for user with user id ```id``` for the same clinic as the assistant
   ```
   {
      "user": {
        "id": 6
      },
      "firstName": "New",
      "lastName": "Patient"
   }
   ```
   * GET ```/patient```: gets list of patients that are in the same clinic as the assistant
   
5. **Patient Endpoints**: Log in with the patient credentials and set the Authorization header with the token.
   * GET ```/appointment```: gets list of all appointments for this patient
   * POST ```/patient/upload```: upload a zip file for the patient. Use ```form-data``` with ```file``` as key and the zip file of choice as value
   * GET ```/patient/documents```: gets the zip file which was uploaded by the patient
   * GET ```/patient/clinics```: gets list of clinics (and doctors) which have access to uploaded zip file
   * PUT ```/patient/clinics/{id}```: grants this patient's zip data access to all doctors of clinic id ```id```
   * DELETE ```/patient/clinics/{id}```: revokes this patient's zip data access from all doctors of clinic id ```id```
   
6. **Doctor Endpoints**: Log in with the doctor credentials and set the Authorization header with the token.
   * GET ```/appointment```: gets list of all appointments for this doctor
   * POST ```/assistant```: creates a new assistant who is linked to the user with user id ```id``` for the same clinic as the doctor
   ```
   {
        "firstName": "New",
        "lastName": "Assistant",
        "user": {
          "id": 5
        }
   }
   ````
   * GET ```/assistant```: gets list of all assistants in the same clinic as the doctor
   * GET ```/doctor```: gets list of all doctors that are in the same clinic as the doctor
   * GET ```/patient```: gets list of patients that are in the same clinic as the doctor
   * POST ```/patient```: create a patient for user with user id ```id``` for the same clinic as the doctor
   ```
   {
      "user": {
        "id": 6
      },
      "firstName": "New",
      "lastName": "Patient"
   }
   ```
   * GET ```/patient/documents/{patientId}```: gets the zip file associated with patient id ```patientId``` if the patient has granted access to the file



